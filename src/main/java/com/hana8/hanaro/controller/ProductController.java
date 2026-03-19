package com.hana8.hanaro.controller;

import com.hana8.hanaro.common.response.ApiResponse;
import com.hana8.hanaro.dto.product.ProductResponse;
import com.hana8.hanaro.dto.product.ProductSearchRequest;
import com.hana8.hanaro.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "User - Product", description = "사용자용 상품 조회 API")
public class ProductController {

    private final ProductService service;

    @GetMapping({"", "/"})
    @Operation(summary = "상품 목록 조회", description = "모든 활성 상품 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProducts() {
        List<ProductResponse> response = service.getProducts();
        return ResponseEntity.ok(ApiResponse.success(response, "Product list retrieved"));
    }

    @GetMapping("/search")
    @Operation(summary = "상품 검색", description = "상품명, 타입 등을 기준으로 상품을 검색합니다.")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProducts(ProductSearchRequest dto) {
        List<ProductResponse> response = service.searchProducts(dto);
        return ResponseEntity.ok(ApiResponse.success(response, "Product search result retrieved"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "상품 상세 조회", description = "특정 상품의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable Long id) {
        ProductResponse response = service.getProduct(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Product details retrieved"));
    }
}
