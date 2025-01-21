package common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "message", "data"})
public class SuccessResponse<T> {
    @JsonProperty("code")
    private final String code;
    @JsonProperty("message")
    private final String message;
    @JsonProperty("data")
    private final T data; // 성공 데이터 (예: OrderResponse)
}
