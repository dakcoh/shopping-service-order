
package shared.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Long orderId;
    private BigDecimal amount;
    private String paymentMethod;

    public PaymentRequest(Long id, BigDecimal totalAmount, String card) {
    }
}
