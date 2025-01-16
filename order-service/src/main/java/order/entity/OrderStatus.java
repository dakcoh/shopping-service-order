package order.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PENDING("대기"),
    PAID("결제"),
    SHIPPED("발송"),
    COMPLETED("완료"),
    RETURNED("반품"),
    CANCELLED("취소")
    ;

    private final String value;
}