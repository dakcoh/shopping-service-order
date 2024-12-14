package order.exception;

import lombok.Getter;
import order.common.OrderResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 주문을 찾을 수 없을 때 발생하는 커스텀 예외 클래스.
 */
@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    public OrderNotFoundException(OrderResultCode resultCode, Long id) {
        super(resultCode.getMessage() + " (ID: " + id + ")");
        this.status = resultCode.getStatus();
        this.code = resultCode.getCode();
    }

}
