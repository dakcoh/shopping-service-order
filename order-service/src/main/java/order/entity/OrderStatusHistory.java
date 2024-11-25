package order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders_status_history")
@Getter
@Setter
@NoArgsConstructor
public class OrderStatusHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders orders;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate; // 주문 일자

    @Column(name = "customer_id", nullable = false)
    private Long customerId; // 고객 ID (FK로 설정 가능)

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    // 편의 메서드 (Order와 관계 설정)
    public void setOrder(Orders orders) {
        this.orders = orders;
        if (!orders.getStatusHistories().contains(this)) {
            orders.getStatusHistories().add(this);
        }
    }
}