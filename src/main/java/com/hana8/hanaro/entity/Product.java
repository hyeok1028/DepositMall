package com.hana8.hanaro.entity;

import com.hana8.hanaro.common.enums.PaymentCycle;
import com.hana8.hanaro.common.enums.ProductType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "products")
public class Product extends BaseEntity {

    @Id
    @Tsid
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductType productType;

    @Column(nullable = false)
    private Long paymentAmount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentCycle paymentCycle;

    @Column(nullable = false)
    private Integer periodMonths;

    @Column(nullable = false)
    private Double maturityInterestRate;

    @Column(nullable = false)
    private Double earlyTerminationInterestRate;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    private String representativeImagePath;

    @OneToMany(mappedBy = "product")
    @Builder.Default
    private List<ProductImage> images = new ArrayList<>();

    public void update(
            String name,
            ProductType productType,
            Long paymentAmount,
            PaymentCycle paymentCycle,
            Integer periodMonths,
            Double maturityInterestRate,
            Double earlyTerminationInterestRate,
            String description,
            Boolean isActive,
            String representativeImagePath
    ) {
        if (name != null) {
            this.name = name;
        }
        if (productType != null) {
            this.productType = productType;
        }
        if (paymentAmount != null) {
            this.paymentAmount = paymentAmount;
        }
        if (paymentCycle != null) {
            this.paymentCycle = paymentCycle;
        }
        if (periodMonths != null) {
            this.periodMonths = periodMonths;
        }
        if (maturityInterestRate != null) {
            this.maturityInterestRate = maturityInterestRate;
        }
        if (earlyTerminationInterestRate != null) {
            this.earlyTerminationInterestRate = earlyTerminationInterestRate;
        }
        if (description != null) {
            this.description = description;
        }
        if (isActive != null) {
            this.isActive = isActive;
        }
        if (representativeImagePath != null) {
            this.representativeImagePath = representativeImagePath;
        }
    }
}
