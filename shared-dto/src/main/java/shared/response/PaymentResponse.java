package shared.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PaymentResponse {
    private String orderId;
    private String paymentStatus; // COMPLETED, FAILED 등
    private String transactionId;
    private String failureReason;
    private BigDecimal amount;
}
