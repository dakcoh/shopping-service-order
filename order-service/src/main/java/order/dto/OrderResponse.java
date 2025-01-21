package order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
public class OrderResponse {
    @JsonProperty("orderItemResponse")
    private OrderItemResponse orderItemResponse;
}