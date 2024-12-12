package payment.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PaymentResultCode {
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "P0001", "결제를 찾을 수 없습니다."),
    PAYMENT_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "P0002", "이미 처리된 결제입니다."),
    PAYMENT_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "P0003", "결제 처리 중 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    PaymentResultCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
