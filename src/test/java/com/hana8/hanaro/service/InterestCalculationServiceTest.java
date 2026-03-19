package com.hana8.hanaro.service;

import com.hana8.hanaro.common.enums.ProductType;
import com.hana8.hanaro.dto.subscription.InterestResponse;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.entity.Subscription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class InterestCalculationServiceTest {

    private final InterestCalculatorService service = new InterestCalculatorService();

    @Test
    @DisplayName("정기예금 만기 이자 계산 - 정상 케이스")
    void calculateDepositInterest() {
        Product product = Product.builder()
                .productType(ProductType.DEPOSIT)
                .periodMonths(12)
                .maturityInterestRate(4.0)
                .build();

        Subscription subscription = Subscription.builder()
                .product(product)
                .principalAmount(1000000L)
                .build();

        InterestResponse result = service.calculate(subscription);

        // 1,000,000 * 0.04 * (12/12) = 40,000
        assertThat(result.getExpectedInterest()).isEqualTo(40000L);
        assertThat(result.getMaturityAmount()).isEqualTo(1040000L);
    }

    @Test
    @DisplayName("정기적금 만기 이자 계산 - 정상 케이스")
    void calculateSavingsInterest() {
        Product product = Product.builder()
                .productType(ProductType.SAVINGS)
                .periodMonths(12)
                .maturityInterestRate(4.0)
                .build();

        Subscription subscription = Subscription.builder()
                .product(product)
                .principalAmount(100000L)
                .build();

        InterestResponse result = service.calculate(subscription);

        // n=12, rate=4.0
        // Interest = 100,000 * 0.04 * (12 * 13 / 24) = 4,000 * 6.5 = 26,000
        assertThat(result.getExpectedInterest()).isEqualTo(26000L);
        assertThat(result.getMaturityAmount()).isEqualTo(1226000L);
    }

    @Test
    @DisplayName("예상 만기 수령액 계산 - 예금")
    void calculateExpectedMaturityAmountDeposit() {
        Product product = Product.builder()
                .productType(ProductType.DEPOSIT)
                .periodMonths(6)
                .maturityInterestRate(3.0)
                .build();

        long result = service.calculateExpectedMaturityAmount(1000000L, product);

        // 1,000,000 * 0.03 * (6/12) = 15,000. Total = 1,015,000
        assertThat(result).isEqualTo(1015000L);
    }

    @Test
    @DisplayName("예상 만기 수령액 계산 - 적금")
    void calculateExpectedMaturityAmountSavings() {
        Product product = Product.builder()
                .productType(ProductType.SAVINGS)
                .periodMonths(12)
                .maturityInterestRate(4.0)
                .build();

        long result = service.calculateExpectedMaturityAmount(100000L, product);

        assertThat(result).isEqualTo(1226000L);
    }

    @Test
    @DisplayName("중도해지 이자 계산 - 예금")
    void calculateEarlyTerminationInterestDeposit() {
        Product product = Product.builder()
                .productType(ProductType.DEPOSIT)
                .earlyTerminationInterestRate(1.0)
                .build();

        // 6개월 경과 가정
        Subscription subscription = Subscription.builder()
                .product(product)
                .principalAmount(1000000L)
                .startedAt(LocalDate.now().minusMonths(6))
                .build();

        long interest = service.calculateEarlyTerminationInterest(subscription);

        // 1,000,000 * 0.01 * (6/12) = 5,000
        assertThat(interest).isEqualTo(5000L);
    }

    @Test
    @DisplayName("중도해지 이자 계산 - 적금")
    void calculateEarlyTerminationInterestSavings() {
        Product product = Product.builder()
                .productType(ProductType.SAVINGS)
                .earlyTerminationInterestRate(2.0)
                .build();

        // 6개월 경과 가정
        Subscription subscription = Subscription.builder()
                .product(product)
                .principalAmount(100000L)
                .startedAt(LocalDate.now().minusMonths(6))
                .build();

        long interest = service.calculateEarlyTerminationInterest(subscription);

        // n=6, rate=2.0
        // Interest = 100,000 * 0.02 * (6 * 7 / 24) = 2,000 * 1.75 = 3,500
        assertThat(interest).isEqualTo(3500L);
    }

    @Test
    @DisplayName("중도해지 이자 계산 - 경과 기간이 0일 때 (최소 1개월 적용)")
    void calculateEarlyTerminationInterestZeroElapsed() {
        Product product = Product.builder()
                .productType(ProductType.DEPOSIT)
                .earlyTerminationInterestRate(1.0)
                .build();

        Subscription subscription = Subscription.builder()
                .product(product)
                .principalAmount(1000000L)
                .startedAt(LocalDate.now())
                .build();

        long interest = service.calculateEarlyTerminationInterest(subscription);

        // 1개월치 적용: 1,000,000 * 0.01 * (1/12) = 833.33... -> 833
        assertThat(interest).isEqualTo(833L);
    }
}
