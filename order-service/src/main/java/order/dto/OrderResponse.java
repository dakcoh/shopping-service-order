package order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import order.entity.OrderStatus;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderResponse {
    public OrderResponse() {

    }

    @NotNull
    private Long id;
    @NotNull
    private LocalDateTime order_date;
    private Long customer_Id;
    private OrderStatus status;
    private Integer total_quantity;
    private Double total_amount;

}