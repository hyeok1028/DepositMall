package com.hana8.hanaro.dto.product;

import com.hana8.hanaro.common.enums.PaymentCycle;
import com.hana8.hanaro.common.enums.ProductType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductUpdateRequest {

    @NotNull
    private Long id;

    private String name;
    private ProductType productType;
    private Long paymentAmount;
    private PaymentCycle paymentCycle;
    private Integer periodMonths;

    @DecimalMin("0.0")
    private Double maturityInterestRate;

    @DecimalMin("0.0")
    private Double earlyTerminationInterestRate;

    private String description;
    private Boolean isActive;
    private String representativeImagePath;
}
