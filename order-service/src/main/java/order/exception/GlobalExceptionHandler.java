package order.exception;

import order.common.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleOrderNotFoundException(OrderNotFoundException e) {
        return ResponseEntity.status(e.getStatus())
                .body(new ErrorResponse(e.getCode(), e.getMessage()));
    }
}