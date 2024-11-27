package shared.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderRequest {
    @NotNull("Customer ID is required")
    private Long customerId;

    @NotNull("Order items cannot be null")
    private List<OrderItemRequest> items; // 주문 상세 리스트
}