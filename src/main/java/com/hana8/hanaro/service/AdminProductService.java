package com.hana8.hanaro.service;

import com.hana8.hanaro.dto.product.ProductCreateRequest;
import com.hana8.hanaro.dto.product.ProductResponse;
import com.hana8.hanaro.dto.product.ProductUpdateRequest;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.mapper.ProductMapper;
import com.hana8.hanaro.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductResponse registProduct(ProductCreateRequest dto) {
        Product product = mapper.toEntity(dto);
        return mapper.toResponse(repository.save(product));
    }

    public ProductResponse editProduct(ProductUpdateRequest dto) {
        Product product = repository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Product #%d is not found!".formatted(dto.getId())));

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
                dto.getRepresentativeImagePath()
        );

        return mapper.toResponse(repository.save(product));
    }

    public int removeProduct(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product #%d is not found!".formatted(id)));

        repository.delete(product);
        return 1;
    }
}
