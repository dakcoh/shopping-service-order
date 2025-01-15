package order.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.transaction.Transactional;
import order.entity.Orders;
import order.entity.OrderStatus;
import order.exception.OrderNotFoundException;
import order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.annotation.Rollback;
import shared.request.OrderItemRequest;
import shared.request.OrderRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class) // Mockito를 사용한 테스트 환경 구성
public class OrderServiceTest {

    private Orders mockOrder; // 공통적으로 사용되는 Mock 주문 객체

    @InjectMocks
    private OrderService orderService; // 테스트 대상 클래스

    @Mock
    private OrderRepository orderRepository; // Mock Repository

    @Mock
    private OrderStatusHistoryService orderStatusHistoryService; // Mock 상태 이력 서비스

    @Mock
    private ApplicationEventPublisher eventPublisher; // Mock 이벤트 발행기

    /**
     * 공통적으로 사용될 테스트 데이터 초기화
     */
    @BeforeEach
    public void setup() {
        // Mock 주문 데이터 생성
        mockOrder = new Orders();
        mockOrder.setId(1L);
        mockOrder.setCustomerId("dakcoh");
        mockOrder.setOrderDate(LocalDateTime.now());
        mockOrder.setStatus(OrderStatus.PENDING);
        mockOrder.setTotalQuantity(2);
        mockOrder.setTotalAmount(BigDecimal.valueOf(100.0));
    }

    /**
     * 헬퍼 메서드: OrderItemRequest 객체 생성
     *
     * @param productOptionId 제품 옵션 ID
     * @param quantity         수량
     * @param amount           금액
     * @return 생성된 OrderItemRequest 객체
     */
    private OrderItemRequest createOrderItemRequest(Long productOptionId, int quantity, BigDecimal amount) {
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductOptionId(productOptionId);
        itemRequest.setQuantity(quantity);
        itemRequest.setAmount(amount);
        return itemRequest;
    }

    /**
     * 헬퍼 메서드: OrderRequest 객체 생성
     *
     * @param customerId 고객 ID
     * @param items      주문 항목 배열
     * @return 생성된 OrderRequest 객체
     */
    private OrderRequest createOrderRequest(String customerId, OrderItemRequest... items) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(customerId);
        orderRequest.setItems(Arrays.asList(items));
        return orderRequest;
    }

    /**
     * 주문 생성 성공 테스트
     */
    @Transactional
    @Rollback(false) // 롤백 비활성화
    @Test
    public void testCreateOrder_Success() {
        // Given: 주문 요청 생성
        OrderItemRequest itemRequest = createOrderItemRequest(1L, 2, BigDecimal.valueOf(50.0));
        OrderRequest orderRequest = createOrderRequest("dakcoh", itemRequest);

        when(orderRepository.save(any(Orders.class))).thenReturn(mockOrder); // 저장 동작 Mocking

        // When: createOrder 호출
        var response = orderService.createOrder(orderRequest);

        // Then: 결과 검증
        assertNotNull(response, "Order response should not be null");
        assertEquals(OrderStatus.PENDING, response.getStatus(), "Order status should be PENDING");
        assertEquals(mockOrder.getId(), response.getId(), "Order ID should match the mock order ID");

        // Mock 메서드 호출 검증
        verify(orderRepository, times(1)).save(any(Orders.class));
        verify(orderStatusHistoryService, times(1)).create(anyLong(), anyString(), eq(OrderStatus.PENDING));
        //verify(eventPublisher, times(1)).publishEvent(any());
    }

    /**
     * 주문 조회 실패 테스트
     */
    @Test
    public void testGetOrderById_NotFound() {
        // Given: Repository에서 아무것도 반환하지 않도록 설정
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then: OrderNotFoundException이 발생해야 함
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(1L));
    }

    /**
     * 주문 상태 업데이트 성공 테스트
     */
    @Test
    public void testUpdateOrderStatus_Success() {
        // Given: Mock 데이터 설정
        when(orderRepository.findById(mockOrder.getId())).thenReturn(Optional.of(mockOrder));
        when(orderRepository.save(any(Orders.class))).thenReturn(mockOrder);

        // When: updateOrderStatus 호출
        var response = orderService.updateOrderStatus(mockOrder.getId(), OrderStatus.COMPLETED);

        // Then: 결과 검증
        assertNotNull(response, "Order response should not be null");
        assertEquals(OrderStatus.COMPLETED, response.getStatus(), "Order status should be COMPLETED");

        // Mock 메서드 호출 검증
        verify(orderStatusHistoryService, times(1)).create(anyLong(), anyString(), eq(OrderStatus.COMPLETED));
        verify(eventPublisher, times(1)).publishEvent(any());
    }

    /**
     * 주문 상태를 PAID로 업데이트 성공 테스트
     */
    @Test
    public void testUpdateOrderStatusToPaid_Success() {
        // Given: Mock 데이터 설정
        when(orderRepository.findById(mockOrder.getId())).thenReturn(Optional.of(mockOrder));

        // When: updateOrderStatusToPaid 호출
        orderService.updateOrderStatusToPaid(String.valueOf(mockOrder.getId()));

        // Then: 검증
        verify(orderRepository, times(1)).save(mockOrder);
        assertEquals(OrderStatus.PAID, mockOrder.getStatus(), "Order status should be updated to PAID");

        // Mock 메서드 호출 검증
        verify(orderStatusHistoryService, times(1)).create(anyLong(), anyString(), eq(OrderStatus.PAID));
        verify(eventPublisher, times(1)).publishEvent(any());
    }

    /**
     * 주문 상태를 CANCELLED로 업데이트 성공 테스트
     */
    @Test
    public void testUpdateOrderStatusToCancelled_Success() {
        // Given: Mock 데이터 설정
        when(orderRepository.findById(mockOrder.getId())).thenReturn(Optional.of(mockOrder));

        // When: updateOrderStatusToCancelled 호출
        orderService.updateOrderStatusToCancelled(String.valueOf(mockOrder.getId()), "Payment failed");

        // Then: 검증
        verify(orderRepository, times(1)).save(mockOrder);
        assertEquals(OrderStatus.CANCELLED, mockOrder.getStatus(), "Order status should be updated to CANCELLED");

        // Mock 메서드 호출 검증
        verify(orderStatusHistoryService, times(1)).create(anyLong(), anyString(), eq(OrderStatus.CANCELLED));
        verify(eventPublisher, times(1)).publishEvent(any());
    }
}
