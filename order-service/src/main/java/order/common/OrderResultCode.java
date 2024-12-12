package order.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public enum OrderResultCode {
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "O0001", "해당 주문을 찾을 수 없습니다."),
    ORDER_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "O0002", "이미 취소된 주문입니다."),
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "O0003", "잘못된 주문 상태입니다."),
    ORDER_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "O0004", "주문 생성에 실패했습니다."),
    ORDER_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "O0005", "주문 업데이트에 실패했습니다."),
    ORDER_CANCELLED(HttpStatus.OK, "O0006", "주문이 성공적으로 취소되었습니다."),
    ORDER_COMPLETED(HttpStatus.OK, "O0007", "주문이 성공적으로 완료되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    OrderResultCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
