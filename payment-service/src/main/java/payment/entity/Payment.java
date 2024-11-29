package payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import payment.dto.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 결제 ID

    @Column(name = "order_id", nullable = false)
    private Long orderId; // 주문 ID (Orders의 ID를 참조)

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status; // PENDING, SUCCESS, FAILED

    @Column(name = "amount", nullable = false)
    private BigDecimal amount; // 결제 금액

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    // 생성자
    public Payment(Long orderId, PaymentStatus status, BigDecimal amount, LocalDateTime paymentDate) {
        this.orderId = orderId;
        this.status = status;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }
}
