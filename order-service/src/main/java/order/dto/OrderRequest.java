package order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {
    @NotNull
    private Long customerId;

    @NotNull
    private Double totalAmount;
}