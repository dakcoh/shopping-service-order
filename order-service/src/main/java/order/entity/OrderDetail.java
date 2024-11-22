package order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(schema = "order_detail")
@Getter
@Setter
@NoArgsConstructor
public class OrderDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)  // FK
    private Order order_id;

    private Long product_option_id;

    private Integer quantity;

    private BigDecimal amount;

    // 편의 메서드 (Order와 관계 설정)
    public void setOrder(Order order) {
        this.order_id = order;
        if (!order.getOrderDetails().contains(this)) {
            order.getOrderDetails().add(this);
        }
    }
}