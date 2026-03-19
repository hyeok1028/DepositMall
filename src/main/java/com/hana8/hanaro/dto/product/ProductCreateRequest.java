package com.hana8.hanaro.dto.product;

import com.hana8.hanaro.common.enums.PaymentCycle;
import com.hana8.hanaro.common.enums.ProductType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductCreateRequest {

    @NotBlank
    private String name;

    @NotNull
    private ProductType productType;

    @NotNull
    @Min(1)
    private Long paymentAmount;

    private PaymentCycle paymentCycle;

    @NotNull
    @Min(1)
    private Integer periodMonths;

    @NotNull
    @DecimalMin("0.0")
    private Double maturityInterestRate;

    @NotNull
    @DecimalMin("0.0")
    private Double earlyTerminationInterestRate;

    private String description;
    private Boolean isActive;
}
