package com.hana8.hanaro.service;

import com.hana8.hanaro.common.enums.AccountType;
import com.hana8.hanaro.common.enums.ProductType;
import com.hana8.hanaro.common.enums.SubscriptionStatus;
import com.hana8.hanaro.dto.subscription.CancelSubscriptionRequest;
import com.hana8.hanaro.dto.subscription.InterestResponse;
import com.hana8.hanaro.dto.subscription.SubscriptionCreateRequest;
import com.hana8.hanaro.dto.subscription.SubscriptionResponse;
import com.hana8.hanaro.dto.subscription.SubscriptionSearchRequest;
import com.hana8.hanaro.entity.Account;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.entity.Subscription;
import com.hana8.hanaro.mapper.SubscriptionMapper;
import com.hana8.hanaro.repository.AccountRepository;
import com.hana8.hanaro.repository.MemberRepository;
import com.hana8.hanaro.repository.ProductRepository;
import com.hana8.hanaro.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository repository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final SubscriptionMapper mapper;
    private final InterestCalculatorService interestCalculatorService;
    private final AccountNumberGeneratorService accountNumberGeneratorService;

    public List<SubscriptionResponse> getSubscriptions(SubscriptionSearchRequest dto) {
        List<Subscription> subscriptions;

        if (dto.getMemberId() != null && dto.getStatus() != null) {
            subscriptions = repository.findByMemberIdAndStatus(dto.getMemberId(), dto.getStatus());
        } else if (dto.getMemberId() != null && dto.getProductId() != null) {
            subscriptions = repository.findByMemberIdAndProductId(dto.getMemberId(), dto.getProductId());
        } else if (dto.getMemberId() != null) {
            subscriptions = repository.findByMemberId(dto.getMemberId());
        } else if (dto.getProductId() != null) {
            subscriptions = repository.findByProductId(dto.getProductId());
        } else if (dto.getStatus() != null) {
            subscriptions = repository.findByStatus(dto.getStatus());
        } else {
            subscriptions = repository.findAll();
        }

        return mapper.toResponseList(subscriptions);
    }

    public SubscriptionResponse getSubscription(Long id) {
        Subscription subscription = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription #%d is not found!".formatted(id)));

        return mapper.toResponse(subscription);
    }

    public SubscriptionResponse registSubscription(SubscriptionCreateRequest dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member #%d is not found!".formatted(dto.getMemberId())));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product #%d is not found!".formatted(dto.getProductId())));

        Account withdrawAccount = accountRepository.findById(dto.getWithdrawAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account #%d is not found!".formatted(dto.getWithdrawAccountId())));

        String accountNumber = accountNumberGeneratorService.generate(dto.getDesiredAccountNumber());
        AccountType accountType = product.getProductType() == ProductType.SAVINGS
                ? AccountType.SAVINGS
                : AccountType.TERM_DEPOSIT;

        Account subscriptionAccount = Account.open(member, accountNumber, accountType, 0L);
        accountRepository.save(subscriptionAccount);

        long expectedMaturityAmount = interestCalculatorService.calculateExpectedMaturityAmount(dto.getPrincipalAmount(), product);

        Subscription subscription = Subscription.builder()
                .member(member)
                .product(product)
                .withdrawAccount(withdrawAccount)
                .subscriptionAccount(subscriptionAccount)
                .status(SubscriptionStatus.ACTIVE)
                .startedAt(LocalDate.now())
                .maturityDate(LocalDate.now().plusMonths(product.getPeriodMonths()))
                .principalAmount(dto.getPrincipalAmount())
                .accumulatedInterest(0L)
                .totalPaidAmount(0L)
                .expectedMaturityAmount(expectedMaturityAmount)
                .nextPaymentDate(null)
                .build();

        return mapper.toResponse(repository.save(subscription));
    }

    public SubscriptionResponse cancelSubscription(Long id, CancelSubscriptionRequest dto) {
        Subscription subscription = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription #%d is not found!".formatted(id)));

        long accumulatedInterest = interestCalculatorService.calculateEarlyTerminationInterest(subscription);
        subscription.terminate(accumulatedInterest);

        return mapper.toResponse(repository.save(subscription));
    }

    public InterestResponse getInterest(Long id) {
        Subscription subscription = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription #%d is not found!".formatted(id)));

        return interestCalculatorService.calculate(subscription);
    }
}
