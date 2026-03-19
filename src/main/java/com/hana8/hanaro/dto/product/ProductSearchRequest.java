package com.hana8.hanaro.dto.product;

import com.hana8.hanaro.common.enums.ProductType;
import lombok.Data;

@Data
public class ProductSearchRequest {
    private String name;
    private ProductType productType;
    private Boolean isActive;
}
