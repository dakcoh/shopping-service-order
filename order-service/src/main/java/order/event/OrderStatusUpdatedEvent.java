package order.event;

import lombok.Getter;
import order.entity.OrderStatus;

/**
 * OrderStatusUpdatedEvent 클래스는 주문 상태가 변경되었을 때 발생하는 이벤트를 나타냅니다.
 */
@Getter
public class OrderStatusUpdatedEvent {
    private final Long orderId;
    private final OrderStatus status;

    public OrderStatusUpdatedEvent(Long orderId, OrderStatus status) {
        this.orderId = orderId;
        this.status = status;
    }

}
