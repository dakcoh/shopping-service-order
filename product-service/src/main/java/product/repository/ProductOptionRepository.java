package product.repository;

import product.entity.ProductOption;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOptionRepository extends BaseRepository<ProductOption, Long> {
    List<ProductOption> findByProductId(Long productId);
    List<ProductOption> findByColorId(Long colorId);
    List<ProductOption> findBySizeId(Long sizeId);
    List<ProductOption> findByCategoryId(Long categoryId);
    List<ProductOption> findByBrandId(Long brandId);
}
