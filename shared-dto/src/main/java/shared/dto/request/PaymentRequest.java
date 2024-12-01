
package shared.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRequest {
    private Long orderId;
    private BigDecimal amount;
    private String paymentMethod;
}
