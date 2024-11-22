
package order.service;

import jakarta.transaction.Transactional;
import order.dto.OrderItemRequest;
import order.dto.OrderRequest;
import order.entity.Order;
import order.entity.OrderDetail;
import order.repository.OrderItemRepository;
import order.repository.OrderRepository;
import order.dto.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public OrderService(OrderRepository orderRepository, OrderStatusHistoryService orderStatusHistoryService) {
        this.orderRepository = orderRepository;
        this.orderStatusHistoryService = orderStatusHistoryService;
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
        Optional<Order> order = orderRepository.findById(id);
        return order.map(this::convertToOrderResponse).orElse(null);
    }

    /**
     * 새로운 주문을 생성합니다.
     * @param orderRequest 주문 요청 목록
     * @return 생성된 주문의 상세 정보를 OrderResponse 형태로 반환합니다.
     */
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        // 1. 유효성 검증
        if (orderRequest.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be null or empty");
        }

        // 2. 주문 생성
        Order order = new Order();
        order.setOrderDate(LocalDate.now().atStartOfDay());
        order.setCustomerId(orderRequest.getCustomerId());
        order.setStatus(PENDING);

        int totalQuantity = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 3. 주문 상세 추가
        for (OrderItemRequest item : orderRequest.getItems()) {
            OrderDetail detail = new OrderDetail();
            detail.setProduct_option_id(item.getProductOptionId());
            detail.setQuantity(item.getQuantity());
            detail.setAmount(item.getAmount());

            order.addOrderDetail(detail);  // 양방향 관계 설정

            totalQuantity += item.getQuantity();
            totalAmount = totalAmount.add(item.getAmount());
        }

        order.setTotalQuantity(totalQuantity);
        order.setTotalAmount(totalAmount);

        // 4. 저장 (cascade로 OrderDetail도 저장)
        Order saveOrder = orderRepository.save(order);

        // 5. 상태 이력 기록
        orderStatusHistoryService.create(saveOrder.getId(), saveOrder.getCustomerId(), saveOrder.getStatus());

        return convertToOrderResponse(saveOrder);
    }

    /**
     * 주문의 상태를 업데이트합니다.
     * @param orderId 주문의 고유 ID
     * @param status 변경할 주문 상태
     * @return 업데이트된 주문의 상세 정보를 OrderResponse 형태로 반환합니다.
     */
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(PENDING);
            Order updatedOrder = orderRepository.save(order);
            return convertToOrderResponse(updatedOrder);
        }
        return null; // 또는 예외를 던질 수도 있습니다.
    }

    /**
     * Order 엔티티를 OrderResponse DTO로 변환합니다.
     * @param order 변환할 Order 엔티티
     * @return 변환된 OrderResponse DTO
     */
    private OrderResponse convertToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setStatus(order.getStatus());
        return response;
    }

//    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
//        Optional<Order> orderOptional = orderRepository.findById(orderId);
//        if (orderOptional.isPresent()) {
//            Order order = orderOptional.get();
//            order.setStatus(newStatus);
//            orderRepository.save(order);
//        } else {
//            throw new OrderNotFoundException("Order not found with id: " + orderId);
//        }
//    }

}
