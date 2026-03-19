package com.hana8.hanaro.dto.product;

import com.hana8.hanaro.common.enums.PaymentCycle;
import com.hana8.hanaro.common.enums.ProductType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private ProductType productType;
    private Long paymentAmount;
    private PaymentCycle paymentCycle;
    private Integer periodMonths;
    private Double maturityInterestRate;
    private Double earlyTerminationInterestRate;
    private String description;
    private Boolean isActive;
    private String representativeImagePath;
}
