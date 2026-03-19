package com.hana8.hanaro.service;

import com.hana8.hanaro.common.enums.*;
import com.hana8.hanaro.dto.subscription.CancelSubscriptionRequest;
import com.hana8.hanaro.dto.subscription.InterestResponse;
import com.hana8.hanaro.dto.subscription.SubscriptionCreateRequest;
import com.hana8.hanaro.dto.subscription.SubscriptionResponse;
import com.hana8.hanaro.dto.subscription.SubscriptionSearchRequest;
import com.hana8.hanaro.entity.*;
import com.hana8.hanaro.mapper.SubscriptionMapper;
import com.hana8.hanaro.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private static final Logger serviceLogger = LoggerFactory.getLogger("serviceLogger");

    private final SubscriptionRepository repository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final SubscriptionMapper mapper;
    private final InterestCalculatorService interestCalculatorService;
    private final AccountNumberGeneratorService accountNumberGeneratorService;
    private final HolidayService holidayService;

    public List<SubscriptionResponse> getSubscriptions(SubscriptionSearchRequest dto) {
        List<Subscription> subscriptions = repository.searchSubscriptions(dto);
        return mapper.toResponseList(subscriptions);
    }

    public SubscriptionResponse getSubscription(Long id) {
        Subscription subscription = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription #%d is not found!".formatted(id)));

        return mapper.toResponse(subscription);
    }

    @Transactional
    public SubscriptionResponse registSubscription(SubscriptionCreateRequest dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member #%d is not found!".formatted(dto.getMemberId())));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product #%d is not found!".formatted(dto.getProductId())));

        Account withdrawAccount = accountRepository.findById(dto.getWithdrawAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account #%d is not found!".formatted(dto.getWithdrawAccountId())));

        // 1. 계좌 생성 (계좌 상태는 ACTIVE, 잔액은 0)
        String accountNumber = accountNumberGeneratorService.generate(dto.getDesiredAccountNumber());
        AccountType accountType = product.getProductType() == ProductType.SAVINGS
                ? AccountType.SAVINGS
                : AccountType.TERM_DEPOSIT;

        Account subscriptionAccount = Account.open(member, accountNumber, accountType, 0L);
        accountRepository.save(subscriptionAccount);

        // 2. 가입 정보 구성 (최초 납입 전 상태)
        long principal = dto.getPrincipalAmount();
        long expectedMaturityAmount = interestCalculatorService.calculateExpectedMaturityAmount(principal, product);

        Subscription subscription = Subscription.builder()
                .member(member)
                .product(product)
                .withdrawAccount(withdrawAccount)
                .subscriptionAccount(subscriptionAccount)
                .status(SubscriptionStatus.ACTIVE)
                .startedAt(LocalDate.now())
                .maturityDate(LocalDate.now().plusMonths(product.getPeriodMonths()))
                .principalAmount(principal)
                .accumulatedInterest(0L)
                .totalPaidAmount(0L) // 실제 이체 후 합산
                .expectedMaturityAmount(expectedMaturityAmount)
                .build();

        Subscription savedSubscription = repository.save(subscription);

        // 3. 실제 이체 (최초 1회차 납입)
        try {
            withdrawAccount.withdraw(principal);
            subscriptionAccount.deposit(principal);
            
            // 납입 이력 기록
            PaymentHistory history = PaymentHistory.builder()
                    .subscription(savedSubscription)
                    .scheduledDate(LocalDate.now())
                    .paymentDate(LocalDate.now())
                    .amount(principal)
                    .status(PaymentStatus.SUCCESS)
                    .memo("Initial deposit")
                    .build();
            paymentHistoryRepository.save(history);
            
            // 가입 정보 업데이트 (총 납입액)
            savedSubscription.updatePaidAmount(principal);
            
            // 적금인 경우 다음 납입일 계산
            if (product.getProductType() == ProductType.SAVINGS) {
                LocalDate initialDate = LocalDate.now();
                if (product.getPaymentCycle() == PaymentCycle.WEEKLY) {
                    initialDate = initialDate.plusWeeks(1);
                } else {
                    initialDate = initialDate.plusMonths(1);
                }
                LocalDate nextPaymentDate = holidayService.toNextBusinessDay(initialDate);
                savedSubscription.updateNextPaymentDate(nextPaymentDate);
            }
            
            serviceLogger.info("상품 가입 성공 - memberId={}, subscriptionId={}, productId={}, amount={}", 
                    member.getId(), savedSubscription.getId(), product.getId(), principal);
        } catch (Exception e) {
            serviceLogger.error("상품 가입 시 최초 이체 실패: memberId={}, subscriptionId={}, error={}", 
                    member.getId(), savedSubscription.getId(), e.getMessage());
            throw new IllegalArgumentException("failed to process initial deposit: " + e.getMessage());
        }

        return mapper.toResponse(repository.save(savedSubscription));
    }

    @Transactional
    public SubscriptionResponse cancelSubscription(Long id, CancelSubscriptionRequest dto) {
        Subscription subscription = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription #%d is not found!".formatted(id)));

        if (subscription.getStatus() != SubscriptionStatus.ACTIVE) {
            serviceLogger.error("상품 해지 실패 - 활성 상태 아님: subscriptionId={}, status={}", id, subscription.getStatus());
            throw new IllegalStateException("subscription is not active");
        }

        long principal = subscription.getSubscriptionAccount().getBalance();
        long accumulatedInterest = interestCalculatorService.calculateEarlyTerminationInterest(subscription);
        long totalAmount = principal + accumulatedInterest;

        // 원금 + 이자를 연결 계좌(출금 계좌)로 돌려줌
        Account withdrawAccount = subscription.getWithdrawAccount();
        withdrawAccount.deposit(totalAmount);
        
        // 가입 계좌 폐쇄
        subscription.getSubscriptionAccount().close(java.time.LocalDateTime.now());
        
        // 상태 변경
        subscription.terminate(accumulatedInterest);

        serviceLogger.info("상품 해지 성공 - memberId={}, subscriptionId={}, interest={}, totalPayout={}", 
                subscription.getMember().getId(), subscription.getId(), accumulatedInterest, totalAmount);
        
        return mapper.toResponse(repository.save(subscription));
    }

    public InterestResponse getInterest(Long id) {
        Subscription subscription = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription #%d is not found!".formatted(id)));

        return interestCalculatorService.calculate(subscription);
    }
}
