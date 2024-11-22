package order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_status_history")
@Getter
@Setter
@NoArgsConstructor
public class OrderStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    @Column(nullable = false, name = "order_id")
    private Long orderId; // 주문 ID (FK로 설정 가능)

    @Column(nullable = false, name = "order_date")
    private LocalDateTime orderDate; // 주문 일자

    @Column(nullable = false, name = "customer_id")
    private Long customerId; // 고객 ID (FK로 설정 가능)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}