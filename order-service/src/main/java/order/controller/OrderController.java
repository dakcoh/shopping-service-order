
package order.controller;

import common.StatusResponseUtil;
import common.OrderResultCode;
import order.dto.OrderRequest;
import order.entity.OrderStatus;
import util.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import order.dto.OrderResponse;
import order.service.OrderService;
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

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 모든 주문을 조회합니다.
     * @return 주문 목록을 ResponseEntity 형태로 반환합니다.
     */
    @GetMapping("/search")
    public ResponseEntity<?> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        //return ResponseEntity.ok(orders);
        // 성공 응답 처리
        return StatusResponseUtil.toSuccessResponse(OrderResultCode.ORDER_SEARCH_COMPLETED, orders);
    }

    /**
     * 주문 ID를 기반으로 특정 주문을 조회합니다.
     * @param orderId 조회할 주문 ID
     * @return 주문 정보를 포함한 ResponseEntity
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
        log.info("IN : {}", GsonUtil.GSON.toJson(orderId));
        try {
            OrderResponse order = orderService.getOrderById(orderId);
            // 성공 응답 처리
            return StatusResponseUtil.toSuccessResponse(OrderResultCode.ORDER_COMPLETED, order);
        } catch (Exception e) {
            log.error("ERROR : {}", e.getMessage());
            return StatusResponseUtil.toErrorResponse(OrderResultCode.ORDER_NOT_FOUND);
        }
    }

    /**
     * 주문을 생성합니다.
     * @param orderRequest 생성할 주문 정보
     * @return 생성된 주문 정보와 상태 코드
     */
    @ResponseBody
    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        log.info("IN : {}", GsonUtil.GSON.toJson(orderRequest));
        try {
            OrderResponse createdOrder = orderService.createOrder(orderRequest);
            // 성공 응답 처리
            return StatusResponseUtil.toSuccessResponse(OrderResultCode.ORDER_COMPLETED, createdOrder);
        } catch (IllegalArgumentException e) {
            // 잘못된 입력 처리
            log.error("ERROR : {}", e.getMessage());
            return StatusResponseUtil.toErrorResponse(OrderResultCode.INVALID_ORDER_STATUS);
        } catch (Exception e) {
            // 기타 예외 처리
            log.error("ERROR : {}", e.getMessage());
            return StatusResponseUtil.toErrorResponse(OrderResultCode.ORDER_CREATION_FAILED);
        }
    }

    /**
     * 주문 상태를 업데이트합니다.
     * @param id 주문의 고유 ID
     * @param status 업데이트할 주문 상태
     * @return 업데이트된 주문의 상세 정보
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        try {
            OrderResponse updatedOrder = orderService.updateOrderStatus(id, status);
            // 성공 응답 처리
            return StatusResponseUtil.toSuccessResponse(OrderResultCode.ORDER_UPDATE_COMPLETED, updatedOrder);
        } catch (Exception e) {
            // 기타 예외 처리
            log.error("ERROR : {}", e.getMessage());
            return StatusResponseUtil.toErrorResponse(OrderResultCode.ORDER_UPDATE_FAILED);
        }
    }
}