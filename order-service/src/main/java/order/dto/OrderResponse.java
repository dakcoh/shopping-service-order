package order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import order.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    @NotNull
    private Long id;
    @NotNull
    private LocalDateTime order_date;
    private String customer_id;
    private OrderStatus status;
    private Integer total_quantity;
    private BigDecimal total_amount;
}