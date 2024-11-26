package product.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductOptionResponse {
    private Long id;
    private Long productId;
    private Long colorId;
    private Long sizeId;
    private Long categoryId;
    private Long brandId;
    private BigDecimal currentPrice;
    private Integer stockQty;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
