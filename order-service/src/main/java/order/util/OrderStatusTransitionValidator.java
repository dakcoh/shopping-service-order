package order.util;

import order.entity.OrderStatus;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class OrderStatusTransitionValidator {

    // 상태 전환 규칙을 정의하는 맵
    private static final Map<OrderStatus, Set<OrderStatus>> VALID_TRANSITIONS = new EnumMap<>(OrderStatus.class);

    static {
        // 초기 상태 -> 가능한 전환 상태 정의
        VALID_TRANSITIONS.put(OrderStatus.PENDING, EnumSet.of(OrderStatus.PAID, OrderStatus.CANCELLED));
        VALID_TRANSITIONS.put(OrderStatus.PAID, EnumSet.of(OrderStatus.SHIPPED, OrderStatus.CANCELLED));
        VALID_TRANSITIONS.put(OrderStatus.SHIPPED, EnumSet.of(OrderStatus.COMPLETED, OrderStatus.RETURNED));
        VALID_TRANSITIONS.put(OrderStatus.COMPLETED, EnumSet.noneOf(OrderStatus.class)); // 완료 상태는 변경 불가
        VALID_TRANSITIONS.put(OrderStatus.CANCELLED, EnumSet.noneOf(OrderStatus.class)); // 취소 상태는 변경 불가
        VALID_TRANSITIONS.put(OrderStatus.RETURNED, EnumSet.noneOf(OrderStatus.class)); // 반품 상태는 변경 불가
    }

    /**
     * 상태 전환이 유효한지 확인합니다.
     *
     * @param currentStatus 현재 상태
     * @param newStatus     변경하려는 상태
     * @return 상태 전환이 유효하면 true, 그렇지 않으면 false
     */
    public static boolean isValidTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // 현재 상태에서 가능한 전환 상태 확인
        Set<OrderStatus> validTransitions = VALID_TRANSITIONS.getOrDefault(currentStatus, EnumSet.noneOf(OrderStatus.class));
        return validTransitions.contains(newStatus);
    }
}
