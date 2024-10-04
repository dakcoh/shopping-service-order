package order.dto;

import lombok.Getter;
import lombok.Setter;
import order.entity.OrderItem;

import java.util.List;

/**
 * 주문 요청을 위한 데이터 전송 객체 (DTO).
 * 사용자 ID와 주문 항목 목록을 포함합니다.
 */
@Getter
@Setter
public class OrderRequest {
    private Long userId;
    private List<OrderItem> items;
}