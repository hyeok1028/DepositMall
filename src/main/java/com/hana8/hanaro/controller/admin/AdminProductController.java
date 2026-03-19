package com.hana8.hanaro.controller.admin;

import com.hana8.hanaro.common.response.ApiResponse;
import com.hana8.hanaro.dto.product.ProductCreateRequest;
import com.hana8.hanaro.dto.product.ProductResponse;
import com.hana8.hanaro.dto.product.ProductUpdateRequest;
import com.hana8.hanaro.service.AdminProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@Tag(name = "Admin - Product", description = "관리자 전용 상품 관리 API")
public class AdminProductController {

    private final AdminProductService service;

    @PostMapping(value = {"", "/"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "상품 등록", description = "관리자가 새로운 예/적금 상품을 등록합니다. 대표 이미지 업로드를 지원합니다. 성공 시 success: true를 반환합니다.")
    public ResponseEntity<ApiResponse<ProductResponse>> registProduct(
            @Valid @RequestPart("product") ProductCreateRequest dto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        ProductResponse response = service.registProduct(dto, image);
        return ResponseEntity.ok(ApiResponse.success(response, "Product registered successfully"));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "상품 수정", description = "관리자가 기존 상품 정보를 수정합니다. 성공 시 success: true를 반환합니다.")
    public ResponseEntity<ApiResponse<ProductResponse>> editProduct(
            @PathVariable Long id,
            @Valid @RequestPart("product") ProductUpdateRequest dto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        dto.setId(id);
        ProductResponse response = service.editProduct(dto, image);
        return ResponseEntity.ok(ApiResponse.success(response, "Product updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "상품 삭제", description = "관리자가 상품을 삭제합니다. 성공 시 success: true를 반환합니다.")
    public ResponseEntity<ApiResponse<Integer>> removeProduct(@PathVariable Long id) {
        int result = service.removeProduct(id);
        return ResponseEntity.ok(ApiResponse.success(result, "Product removed successfully"));
    }
}
