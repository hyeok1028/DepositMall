package com.hana8.hanaro.dto.product;

import com.hana8.hanaro.common.enums.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "상품 검색 요청")
public class ProductSearchRequest {
    @Schema(description = "상품명 (부분 일치)", example = "하나로")
    private String name;

    @Schema(description = "상품 타입", example = "SAVINGS")
    private ProductType productType;

    @Schema(description = "활성 상태 여부", example = "true")
    private Boolean isActive;
}
