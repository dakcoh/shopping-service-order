package order.dto;

import lombok.*;
import order.entity.OrderStatus;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponse {
    @NotNull
    private Long id;
    @NotNull
    private LocalDateTime order_date;
    private String customer_Id;
    private OrderStatus status;
    private Integer total_quantity;
    private BigDecimal total_amount;
}