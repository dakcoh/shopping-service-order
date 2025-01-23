package order.domain;

import order.dto.OrderResponse;
import order.entity.Orders;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class toOrderResponse {
    public OrderResponse toDto(Orders order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customer_id(order.getCustomerId())
                .order_date(order.getOrderDate())
                .status(order.getStatus())
                .total_quantity(order.getTotalQuantity())
                .total_amount(order.getTotalAmount())
                .build();
    }
}