package product.controller;

import product.entity.ProductOption;
import product.service.ProductOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProductOption API 요청을 처리하는 컨트롤러 클래스.
 */
@RestController
@RequestMapping("/api/product-options")
public class ProductOptionController {

    @Autowired
    private ProductOptionService service;

    /**
     * 새로운 상품 옵션을 생성합니다.
     *
     * @param productOption 생성할 상품 옵션
     * @return 생성된 상품 옵션
     */
    @PostMapping
    public ProductOption createProductOption(@RequestBody ProductOption productOption) {
        return service.create(productOption);
    }

    /**
     * 상품 옵션 ID로 단일 상품 옵션을 조회합니다.
     *
     * @param id 조회할 상품 옵션의 ID
     * @return 조회된 상품 옵션
     */
    @GetMapping("/{id}")
    public ProductOption getProductOptionById(@PathVariable Long id) {
        return service.getById(id);
    }

    /**
     * 모든 상품 옵션 목록을 조회합니다.
     *
     * @return 상품 옵션 목록
     */
    @GetMapping
    public List<ProductOption> getAllProductOptions() {
        return service.getAll();
    }

    /**
     * 상품 옵션 정보를 업데이트합니다.
     *
     * @param id 업데이트할 상품 옵션의 ID
     * @param productOption 업데이트할 상품 옵션 데이터
     * @return 업데이트된 상품 옵션
     */
    @PutMapping("/{id}")
    public ProductOption updateProductOption(
            @PathVariable Long id,
            @RequestBody ProductOption productOption
    ) {
        return service.update(id, productOption);
    }

    /**
     * 상품 옵션을 삭제합니다.
     *
     * @param id 삭제할 상품 옵션의 ID
     */
    @DeleteMapping("/{id}")
    public void deleteProductOption(@PathVariable Long id) {
        service.delete(id);
    }

    /**
     * 특정 상품 ID에 해당하는 옵션 목록을 조회합니다.
     *
     * @param productId 상품 ID
     * @return 해당 상품의 옵션 목록
     */
    @GetMapping("/by-product/{productId}")
    public List<ProductOption> getOptionsByProductId(@PathVariable Long productId) {
        return service.getByProductId(productId);
    }
}
