package product.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductOptionRequest {
    private Long productId;
    private Long colorId;
    private Long sizeId;
    private Long categoryId;
    private Long brandId;
    private BigDecimal currentPrice;
    private Integer stockQty;
}
