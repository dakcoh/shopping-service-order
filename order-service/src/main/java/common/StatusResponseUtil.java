package common;

import org.springframework.http.ResponseEntity;

public class StatusResponseUtil {

    // 에러 응답 처리
    public static ResponseEntity<ErrorResponse> toErrorResponse(OrderResultCode resultCode) {
        return ResponseEntity
                .status(resultCode.getStatus())
                .body(new ErrorResponse(resultCode.getCode(), resultCode.getMessage()));
    }

    // 성공 응답 처리 (공통 메시지 포함)
    public static <T> ResponseEntity<SuccessResponse<T>> toSuccessResponse(OrderResultCode resultCode, T data) {
        return ResponseEntity
                .status(resultCode.getStatus())
                .body(new SuccessResponse<>(resultCode.getCode(), resultCode.getMessage(), data));
    }
}
