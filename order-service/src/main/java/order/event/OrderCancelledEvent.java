package order.event;

import lombok.Getter;

/**
 * OrderCancelledEvent 클래스는 주문이 취소되었을 때 발생하는 이벤트를 나타냅니다.
 */
@Getter
public class OrderCancelledEvent {
    private final Long orderId;
    private final String reason;

    public OrderCancelledEvent(Long orderId, String reason) {
        this.orderId = orderId;
        this.reason = reason;
    }

}
