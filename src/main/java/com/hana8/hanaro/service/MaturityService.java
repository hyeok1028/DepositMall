package com.hana8.hanaro.service;

import com.hana8.hanaro.common.enums.SubscriptionStatus;
import com.hana8.hanaro.entity.Account;
import com.hana8.hanaro.entity.Subscription;
import com.hana8.hanaro.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaturityService {

    private static final Logger serviceLogger = LoggerFactory.getLogger("serviceLogger");

    private final SubscriptionRepository subscriptionRepository;
    private final InterestCalculatorService interestCalculatorService;

    /**
     * 매일 자정 만기 도래한 가입 건 처리
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void processAllMaturedSubscriptions() {
        List<Subscription> matured = subscriptionRepository.findByStatusAndMaturityDateLessThanEqual(
                SubscriptionStatus.ACTIVE, LocalDate.now());

        for (Subscription sub : matured) {
            processMaturity(sub);
        }
    }

    @Transactional
    public void processMaturity(Subscription subscription) {
        if (subscription.getStatus() != SubscriptionStatus.ACTIVE) {
            return;
        }

        long principal = subscription.getSubscriptionAccount().getBalance();
        long interest = interestCalculatorService.calculate(subscription).getExpectedInterest();
        long totalAmount = principal + interest;

        // 원금 + 만기이자를 연결 계좌로 입금
        Account withdrawAccount = subscription.getWithdrawAccount();
        withdrawAccount.deposit(totalAmount);

        // 가입 계좌 폐쇄
        subscription.getSubscriptionAccount().close(java.time.LocalDateTime.now());

        // 상태 변경
        subscription.terminate(interest); // terminate 메서드가 상태를 변경함
        // terminate가 TERMINATED로 바꾼다면, MATURED로 바꾸는 메서드가 필요할 수 있음. 
        // Subscription 엔티티를 확인하여 mature() 메서드 추가 필요.
        subscription.mature(interest); 

        serviceLogger.info("상품 만기 처리 성공 - memberId={}, subscriptionId={}, interest={}, totalPayout={}", 
                subscription.getMember().getId(), subscription.getId(), interest, totalAmount);
    }
}
