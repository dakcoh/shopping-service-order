package shared.request;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequest {

    // 상품 옵션 ID
    @NotNull("Product option ID is required")
    private Long productOptionId;

    // 주문 수량
    @NotNull("Quantity is required")
    private Integer quantity;

    // 금액 (단가 * 수량)
    @NotNull("Amount is required")
    private BigDecimal amount;
}