package com.hana8.hanaro.service;

import com.hana8.hanaro.dto.subscription.InterestResponse;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.entity.Subscription;
import org.springframework.stereotype.Service;

@Service
public class InterestCalculatorService {

    public InterestResponse calculate(Subscription subscription) {
        Product product = subscription.getProduct();

        long principal = subscription.getPrincipalAmount() != null ? subscription.getPrincipalAmount() : 0L;
        double rate = product.getMaturityInterestRate() != null ? product.getMaturityInterestRate() : 0.0;
        long expectedInterest = Math.round(principal * (rate / 100.0));
        long maturityAmount = principal + expectedInterest;

        return InterestResponse.builder()
                .principal(principal)
                .rate(rate)
                .expectedInterest(expectedInterest)
                .maturityAmount(maturityAmount)
                .build();
    }

    public long calculateExpectedMaturityAmount(long principal, Product product) {
        double rate = product.getMaturityInterestRate() != null ? product.getMaturityInterestRate() : 0.0;
        long expectedInterest = Math.round(principal * (rate / 100.0));
        return principal + expectedInterest;
    }

    public long calculateEarlyTerminationInterest(Subscription subscription) {
        Product product = subscription.getProduct();
        double rate = product.getEarlyTerminationInterestRate() != null
                ? product.getEarlyTerminationInterestRate()
                : 0.0;
        long principal = subscription.getPrincipalAmount() != null ? subscription.getPrincipalAmount() : 0L;
        return Math.round(principal * (rate / 100.0));
    }
}
