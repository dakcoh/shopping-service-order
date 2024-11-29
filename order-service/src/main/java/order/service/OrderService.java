
package order.service;

import jakarta.transaction.Transactional;
import shared.dto.OrderItemRequest;
import shared.dto.OrderRequest;
import order.dto.OrderResponse;
import order.entity.OrderDetail;
import order.entity.Orders;
import order.repository.OrderRepository;
import shared.dto.PaymentRequest; // 결제 요청 DTO
import order.entity.OrderStatus;
import order.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static order.entity.OrderStatus.PENDING;

/**
 * OrderService 클래스는 주문에 대한 비즈니스 로직을 처리합니다.
 * 모든 주문 관련 작업은 이 클래스에서 구현됩니다.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryService orderStatusHistoryService;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderStatusHistoryService orderStatusHistoryService,
                        RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.orderStatusHistoryService = orderStatusHistoryService;
        this.restTemplate = restTemplate;
    }

    /**
     * 모든 주문을 조회합니다.
     * @return 주문 목록을 OrderResponse 형태로 반환합니다.
     */
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    /**
     * 특정 주문을 ID를 통해 조회합니다.
     * @param id 주문의 고유 ID
     * @return 주문의 상세 정보를 OrderResponse 형태로 반환합니다.
     */
    public OrderResponse getOrderById(Long id) {
        Optional<Orders> order = orderRepository.findById(id);
        return order.map(this::convertToOrderResponse)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
    }

    /**
     * 새로운 주문을 생성하고 결제 요청을 전송합니다.
     * @param orderRequest 주문 요청 객체
     * @return 생성된 주문의 상세 정보를 OrderResponse 형태로 반환합니다.
     */
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {

        if (orderRequest.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be null or empty");
        }

        // 1. 주문 생성
        Orders order = new Orders();
        order.setOrderDate(LocalDate.now().atStartOfDay());
        order.setCustomerId(orderRequest.getCustomerId());
        order.setStatus(PENDING);

        int totalQuantity = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;


        for (OrderItemRequest item : orderRequest.getItems()) {
            OrderDetail detail = new OrderDetail();
            detail.setProductOptionId(item.getProductOptionId());
            detail.setQuantity(item.getQuantity());
            detail.setAmount(item.getAmount());

            order.addOrderDetail(detail);

            totalQuantity += item.getQuantity();
            totalAmount = totalAmount.add(item.getAmount());
        }

        order.setTotalQuantity(totalQuantity);
        order.setTotalAmount(totalAmount);


        Orders savedOrder = orderRepository.save(order);

        // 2. 상태 이력 기록
        orderStatusHistoryService.create(savedOrder.getId(), savedOrder.getCustomerId(), savedOrder.getStatus());

        // 3. 결제 요청 전송
        sendPaymentRequest(savedOrder);

        return convertToOrderResponse(savedOrder);
    }

    /**
     * 주문 상태를 업데이트합니다.
     * @param orderId 주문의 고유 ID
     * @param status 변경할 주문 상태
     * @return 업데이트된 주문의 상세 정보를 OrderResponse 형태로 반환합니다.
     */
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        order.setStatus(status);
        Orders updatedOrder = orderRepository.save(order);

        // 상태 이력 기록
        orderStatusHistoryService.create(updatedOrder.getId(), updatedOrder.getCustomerId(), updatedOrder.getStatus());

        return convertToOrderResponse(updatedOrder);
    }

    /**
     * 결제 요청을 전송합니다.
     * @param order 주문 객체
     */
    private void sendPaymentRequest(Orders order) {
        PaymentRequest paymentRequest = new PaymentRequest(
                order.getId(),
                order.getTotalAmount(),
                "CARD" // 예: 결제 방식
        );

        String paymentServiceUrl = "http://payment-service/payments";
        restTemplate.postForEntity(paymentServiceUrl, paymentRequest, Void.class);
    }

    /**
     * Order 엔티티를 OrderResponse DTO로 변환합니다.
     * @param order 변환할 Order 엔티티
     * @return 변환된 OrderResponse DTO
     */
    private OrderResponse convertToOrderResponse(Orders order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setStatus(order.getStatus());
        return response;
    }
}
