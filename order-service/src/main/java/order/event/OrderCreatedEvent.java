package order.event;

import lombok.Getter;

/**
 * OrderCreatedEvent 클래스는 새로운 주문이 생성되었을 때 발생하는 이벤트를 나타냅니다.
 */
@Getter
public class OrderCreatedEvent {
    private final Long orderId;
    private final String customerId;

    public OrderCreatedEvent(Long orderId, String customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
    }
}