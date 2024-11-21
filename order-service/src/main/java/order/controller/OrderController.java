
package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OrderController 클래스는 주문 관련 API 요청을 처리합니다.
 * 이 클래스는 주문 생성, 조회, 상태 업데이트와 같은 작업을 제공합니다.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 모든 주문을 조회합니다.
     * @return 주문 목록을 ResponseEntity 형태로 반환합니다.
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * 주문 ID를 기반으로 특정 주문을 조회합니다.
     * @param id 주문의 고유 ID
     * @return 주문 상세 정보를 ResponseEntity 형태로 반환합니다.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * 새로운 주문을 생성합니다.
     * @param orderRequest 주문 요청 정보 (사용자 ID와 항목 리스트 포함)
     * @return 생성된 주문의 상세 정보를 ResponseEntity 형태로 반환합니다.
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse newOrder = orderService.createOrder(orderRequest.getUserId(), orderRequest.getItems());
        return ResponseEntity.ok(newOrder);
    }

    /**
     * 주문 상태를 업데이트합니다.
     * @param id 주문의 고유 ID
     * @param status 업데이트할 주문 상태
     * @return 업데이트된 주문의 상세 정보
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(id, status);
        return updatedOrder != null ? ResponseEntity.ok(updatedOrder) : ResponseEntity.notFound().build();
    }
}
