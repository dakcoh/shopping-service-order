package order.entity;

import common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_detail")
@Getter
@Setter
@NoArgsConstructor
public class OrderDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)  // FK
    private Orders orders;

    @Column(name = "product_option_id", nullable = false)
    private Long productOptionId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal amount;

    // 편의 메서드 (Order와 관계 설정)
    public void setOrder(Orders orders) {
        this.orders = orders;
        if (!orders.getOrderDetails().contains(this)) {
            orders.getOrderDetails().add(this);
        }
    }
}