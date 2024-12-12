package shared.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResultCode {
    // Common
    OK(HttpStatus.OK, "C0001", "OK"),
    OK_CREATED(HttpStatus.CREATED, "C0002", "성공적으로 생성되었습니다"),
    REQUEST_ERROR_INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "R0001", "잘못된 요청입니다."),
    REQUEST_ERROR_FORBIDDEN(HttpStatus.FORBIDDEN, "R0003", "권한이 없는 사용자 입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C9999", "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ResultCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
