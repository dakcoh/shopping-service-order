package order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 주문 ID

    private Long userId;  // 주문한 사용자 ID
    private LocalDateTime orderDate;  // 주문 날짜 및 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status;  // 주문 상태 (예: PENDING, SHIPPED 등)

    // 주문 항목과의 1:N 관계 설정 (주문 삭제 시, 주문 항목도 함께 삭제됨)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();
}
