package common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "message"})
public class ErrorResponse {
    @JsonProperty("code")
    private final String code;
    @JsonProperty("message")
    private final String message;
}