
package order.controller;

import order.dto.OrderRequest;

import order.service.OrderService;
import order.dto.OrderResponse;
import order.entity.OrderStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 주문 관련 REST API를 처리하는 컨트롤러 클래스.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 새로운 주문을 생성하는 엔드포인트.
     * @param orderRequest 주문 요청 정보 (사용자 ID 및 주문 항목)
     * @return 생성된 주문 응답 객체
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse newOrder = orderService.createOrder(orderRequest.getUserId(), orderRequest.getItems());
        return ResponseEntity.ok(newOrder);
    }

    /**
     * 주문 상태를 업데이트하는 엔드포인트.
     * @param orderId 주문 ID
     * @param status 변경할 주문 상태
     * @return 상태가 업데이트된 주문 응답 객체
     */
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatus status) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * 특정 사용자 ID로 주문 목록을 조회하는 엔드포인트.
     * @param userId 사용자 ID
     * @return 해당 사용자의 주문 응답 목록
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponse> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }
}