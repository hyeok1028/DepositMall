package com.hana8.hanaro.controller;

import com.hana8.hanaro.dto.product.ProductResponse;
import com.hana8.hanaro.dto.product.ProductSearchRequest;
import com.hana8.hanaro.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping({"", "/"})
    public List<ProductResponse> getProducts(ProductSearchRequest dto) {
        return service.getProducts(dto);
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable Long id) {
        return service.getProduct(id);
    }
}
