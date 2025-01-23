package order.service;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.transaction.Transactional;
import order.dto.OrderItemRequest;
import order.dto.OrderRequest;
import order.dto.OrderResponse;
import order.entity.Orders;
import order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class) // Mockito를 사용한 테스트 환경 구성
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService; // 테스트 대상 클래스

    @Mock
    private OrderRepository orderRepository; // Mock Repository
    /**
     * 주문 생성 성공 테스트
     */
    @Test
    @DisplayName("주문 생성 성공 테스트")
    void createOrder_Success() {
        // Given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId("dakcoh");
        List<OrderItemRequest> items = List.of(
                new OrderItemRequest(101L, 2, new BigDecimal("5000")), // 2개, 5000원
                new OrderItemRequest(102L, 1, new BigDecimal("3000"))  // 1개, 3000원
        );
        orderRequest.setItems(items);

        // When
        OrderResponse response = orderService.createOrder(orderRequest);

        // Then
        assertNotNull(response); // 응답이 null이 아닌지 확인

        // 데이터베이스에 저장된 데이터 확인
        Orders savedOrder = orderRepository.findById(response.getId())
                .orElseThrow(() -> new AssertionError("Order not found in database"));
        assertEquals("dakcoh", savedOrder.getCustomerId()); // 저장된 데이터 검증
        assertEquals(3, savedOrder.getTotalQuantity());
        assertEquals(new BigDecimal("13000"), savedOrder.getTotalAmount());
    }
}
