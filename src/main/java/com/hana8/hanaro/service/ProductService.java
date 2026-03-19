package com.hana8.hanaro.service;

import com.hana8.hanaro.dto.product.ProductResponse;
import com.hana8.hanaro.dto.product.ProductSearchRequest;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.mapper.ProductMapper;
import com.hana8.hanaro.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public List<ProductResponse> getProducts(ProductSearchRequest dto) {
        List<Product> products;

        if (dto.getName() != null && !dto.getName().isBlank() && dto.getIsActive() != null) {
            products = repository.findByNameContainingAndIsActive(dto.getName(), dto.getIsActive());
        } else if (dto.getProductType() != null && dto.getIsActive() != null) {
            products = repository.findByProductTypeAndIsActive(dto.getProductType(), dto.getIsActive());
        } else if (dto.getName() != null && !dto.getName().isBlank()) {
            products = repository.findByNameContaining(dto.getName());
        } else if (dto.getProductType() != null) {
            products = repository.findByProductType(dto.getProductType());
        } else if (dto.getIsActive() != null) {
            products = repository.findByIsActive(dto.getIsActive());
        } else {
            products = repository.findAll();
        }

        return mapper.toResponseList(products);
    }

    public ProductResponse getProduct(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product #%d is not found!".formatted(id)));

        return mapper.toResponse(product);
    }
}
