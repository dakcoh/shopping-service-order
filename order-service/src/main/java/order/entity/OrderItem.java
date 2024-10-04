package order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 주문 항목 ID

    private Long productId;  // 상품 ID
    private int quantity;  // 주문 수량
    private double price;  // 상품 가격

    // 주문과의 N:1 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;  // 주문과의 연관 관계

    // Getters and Setters
}