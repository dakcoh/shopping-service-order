package shared.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class PaymentRequest {
    private Long orderId;
    private BigDecimal amount;

    /**
     * 객체를 JSON 문자열로 변환합니다.
     *
     * @return JSON 문자열
     */
    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert PaymentRequest to JSON", e);
        }
    }
}
