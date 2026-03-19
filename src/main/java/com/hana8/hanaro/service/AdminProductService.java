package com.hana8.hanaro.service;

import com.hana8.hanaro.dto.product.ProductCreateRequest;
import com.hana8.hanaro.dto.product.ProductResponse;
import com.hana8.hanaro.dto.product.ProductUpdateRequest;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.mapper.ProductMapper;
import com.hana8.hanaro.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private static final Logger productLogger = LoggerFactory.getLogger("productLogger");

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final FileUploadService fileUploadService;

    @Transactional
    public ProductResponse registProduct(ProductCreateRequest dto, MultipartFile image) {
        String imagePath = fileUploadService.uploadImage(image);
        
        Product product = mapper.toEntity(dto);
        product.update(
                dto.getName(),
                dto.getProductType(),
                dto.getPaymentAmount(),
                dto.getPaymentCycle(),
                dto.getPeriodMonths(),
                dto.getMaturityInterestRate(),
                dto.getEarlyTerminationInterestRate(),
                dto.getDescription(),
                dto.getIsActive(),
                imagePath != null ? imagePath : null
        );
        
        Product saved = repository.save(product);
        productLogger.info("상품 등록 성공 - admin=system, productId={}, name={}", saved.getId(), saved.getName());
        return mapper.toResponse(saved);
    }

    @Transactional
    public ProductResponse editProduct(ProductUpdateRequest dto, MultipartFile image) {
        Product product = repository.findById(dto.getId())
                .orElseThrow(() -> {
                    productLogger.warn("상품 수정 실패 - 존재하지 않는 상품: productId={}", dto.getId());
                    return new IllegalArgumentException("Product #%d is not found!".formatted(dto.getId()));
                });

        String imagePath = fileUploadService.uploadImage(image);
        
        product.update(
                dto.getName(),
                dto.getProductType(),
                dto.getPaymentAmount(),
                dto.getPaymentCycle(),
                dto.getPeriodMonths(),
                dto.getMaturityInterestRate(),
                dto.getEarlyTerminationInterestRate(),
                dto.getDescription(),
                dto.getIsActive(),
                imagePath != null ? imagePath : product.getRepresentativeImagePath()
        );

        Product updated = repository.save(product);
        productLogger.info("상품 수정 성공 - admin=system, productId={}, name={}", updated.getId(), updated.getName());
        return mapper.toResponse(updated);
    }

    @Transactional
    public int removeProduct(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> {
                    productLogger.warn("상품 삭제 실패 - 존재하지 않는 상품: productId={}", id);
                    return new IllegalArgumentException("Product #%d is not found!".formatted(id));
                });

        repository.delete(product);
        productLogger.info("상품 삭제 성공 - admin=system, productId={}", id);
        return 1;
    }
}
