package product.service;

import product.dto.ProductOptionRequest;
import product.dto.ProductOptionResponse;
import product.entity.ProductOption;
import product.exception.ResourceNotFoundException;
import product.repository.ProductOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ProductOption 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
public class ProductOptionService implements BaseService<ProductOption, Long> {

    @Autowired
    private ProductOptionRepository repository;

    /**
     * 새로운 상품 옵션을 생성합니다.
     *
     * @param request 생성할 상품 옵션의 요청 데이터
     * @return 생성된 상품 옵션의 응답 DTO
     */
    public ProductOptionResponse createProductOption(ProductOptionRequest request) {
        ProductOption option = ProductOption.builder()
                .productId(request.getProductId())
                .colorId(request.getColorId())
                .sizeId(request.getSizeId())
                .categoryId(request.getCategoryId())
                .brandId(request.getBrandId())
                .currentPrice(request.getCurrentPrice())
                .stockQty(request.getStockQty())
                .build();

        return mapToResponse(repository.save(option));
    }

    /**
     * 상품 옵션 ID로 단일 상품 옵션을 조회합니다.
     *
     * @param id 조회할 상품 옵션의 ID
     * @return 조회된 상품 옵션의 응답 DTO
     * @throws ResourceNotFoundException 해당 ID의 상품 옵션이 존재하지 않을 경우 발생
     */
    public ProductOptionResponse getProductOptionById(Long id) {
        ProductOption option = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductOption not found with id: " + id));
        return mapToResponse(option);
    }

    /**
     * 모든 상품 옵션 목록을 조회합니다.
     *
     * @return 모든 상품 옵션의 응답 DTO 목록
     */
    public List<ProductOptionResponse> getAllProductOptions() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 상품 옵션 정보를 업데이트합니다.
     *
     * @param id      업데이트할 상품 옵션의 ID
     * @param request 업데이트할 데이터가 포함된 요청 DTO
     * @return 업데이트된 상품 옵션의 응답 DTO
     * @throws ResourceNotFoundException 해당 ID의 상품 옵션이 존재하지 않을 경우 발생
     */
    public ProductOptionResponse updateProductOption(Long id, ProductOptionRequest request) {
        ProductOption option = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductOption not found with id: " + id));

        option.setProductId(request.getProductId());
        option.setColorId(request.getColorId());
        option.setSizeId(request.getSizeId());
        option.setCategoryId(request.getCategoryId());
        option.setBrandId(request.getBrandId());
        option.setCurrentPrice(request.getCurrentPrice());
        option.setStockQty(request.getStockQty());
        option.setUpdatedAt(option.getUpdatedAt());

        return mapToResponse(repository.save(option));
    }

    /**
     * 상품 옵션을 삭제합니다.
     *
     * @param id 삭제할 상품 옵션의 ID
     * @throws ResourceNotFoundException 해당 ID의 상품 옵션이 존재하지 않을 경우 발생
     */
    public void deleteProductOption(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ProductOption not found with id: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * ProductOption 엔티티를 ProductOptionResponse DTO로 변환합니다.
     *
     * @param option 변환할 ProductOption 엔티티
     * @return 변환된 ProductOptionResponse DTO
     */
    private ProductOptionResponse mapToResponse(ProductOption option) {
        ProductOptionResponse response = new ProductOptionResponse();
        response.setId(option.getId());
        response.setProductId(option.getProductId());
        response.setColorId(option.getColorId());
        response.setSizeId(option.getSizeId());
        response.setCategoryId(option.getCategoryId());
        response.setBrandId(option.getBrandId());
        response.setCurrentPrice(option.getCurrentPrice());
        response.setStockQty(option.getStockQty());
        response.setCreatedAt(option.getCreatedAt());
        response.setUpdatedAt(option.getUpdatedAt());
        return response;
    }
}
