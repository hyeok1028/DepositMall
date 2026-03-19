package com.hana8.hanaro.service;

import com.hana8.hanaro.common.enums.ProductType;
import com.hana8.hanaro.dto.subscription.InterestResponse;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.entity.Subscription;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class InterestCalculatorService {

    /**
     * 만기 이자 계산
     * 예금: principal * rate * (months/12)
     * 적금: Simple model (회차별 가중치 합산)
     */
    public InterestResponse calculate(Subscription subscription) {
        Product product = subscription.getProduct();
        long principal = subscription.getPrincipalAmount() != null ? subscription.getPrincipalAmount() : 0L;
        int periodMonths = product.getPeriodMonths() != null ? product.getPeriodMonths() : 0;
        double rate = product.getMaturityInterestRate() != null ? product.getMaturityInterestRate() : 0.0;
        
        long expectedInterest;
        if (product.getProductType() == ProductType.SAVINGS) {
            // 적금 월납입 기준 단순 모델: (월납입액 * 이율/100 * n(n+1)/24)
            expectedInterest = Math.round(principal * (rate / 100.0) * (periodMonths * (periodMonths + 1.0) / 24.0));
        } else {
            // 예금: principal * (rate/100) * (months/12)
            expectedInterest = Math.round(principal * (rate / 100.0) * (periodMonths / 12.0));
        }

        long maturityAmount = (product.getProductType() == ProductType.SAVINGS ? principal * periodMonths : principal) + expectedInterest;

        return InterestResponse.builder()
                .principal(principal)
                .rate(rate)
                .expectedInterest(expectedInterest)
                .maturityAmount(maturityAmount)
                .build();
    }

    public long calculateExpectedMaturityAmount(long principal, Product product) {
        int periodMonths = product.getPeriodMonths() != null ? product.getPeriodMonths() : 0;
        double rate = product.getMaturityInterestRate() != null ? product.getMaturityInterestRate() : 0.0;
        
        long expectedInterest;
        if (product.getProductType() == ProductType.SAVINGS) {
            expectedInterest = Math.round(principal * (rate / 100.0) * (periodMonths * (periodMonths + 1.0) / 24.0));
            return (principal * periodMonths) + expectedInterest;
        } else {
            expectedInterest = Math.round(principal * (rate / 100.0) * (periodMonths / 12.0));
            return principal + expectedInterest;
        }
    }

    /**
     * 중도해지 이자 계산
     * 현재 가입 기간에 따른 중도해지 이율 적용 (간소화를 위해 기간 비례 계산)
     */
    public long calculateEarlyTerminationInterest(Subscription subscription) {
        Product product = subscription.getProduct();
        double earlyRate = product.getEarlyTerminationInterestRate() != null
                ? product.getEarlyTerminationInterestRate()
                : 0.0;
        
        LocalDate now = LocalDate.now();
        LocalDate start = subscription.getStartedAt();
        Period period = Period.between(start, now);
        int elapsedMonths = period.getYears() * 12 + period.getMonths();
        if (elapsedMonths <= 0) elapsedMonths = 1; // 최소 1개월치

        long principal = subscription.getPrincipalAmount() != null ? subscription.getPrincipalAmount() : 0L;
        
        if (product.getProductType() == ProductType.SAVINGS) {
            // 적금 중도해지: (월납입액 * 해지이율/100 * m(m+1)/24) m=경과월수
            return Math.round(principal * (earlyRate / 100.0) * (elapsedMonths * (elapsedMonths + 1.0) / 24.0));
        } else {
            // 예금 중도해지: (원금 * 해지이율/100 * 경과월수/12)
            return Math.round(principal * (earlyRate / 100.0) * (elapsedMonths / 12.0));
        }
    }
}
