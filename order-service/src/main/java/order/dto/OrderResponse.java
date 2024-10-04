package order.dto;

import lombok.Getter;
import lombok.Setter;
import order.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 주문에 대한 응답을 클라이언트에게 전달하는 DTO 클래스.
 * 주문 정보 및 각 주문 항목에 대한 정보를 포함합니다.
 */
@Getter
@Setter
public class OrderResponse {

    private Long orderId;                // 주문 ID
    private Long userId;                 // 사용자 ID
    private LocalDateTime orderDate;     // 주문 날짜 및 시간
    private OrderStatus status;          // 주문 상태
    private List<OrderItemResponse> items; // 주문 항목 목록

    // 주문 항목에 대한 응답 클래스 (내부 클래스)
    @Getter
    @Setter
    public static class OrderItemResponse {
        private Long productId;          // 상품 ID
        private int quantity;            // 주문 수량
        private double price;            // 상품 가격

        // 생성자
        public OrderItemResponse(Long productId, int quantity, double price) {
            this.productId = productId;
            this.quantity = quantity;
            this.price = price;
        }
    }
}