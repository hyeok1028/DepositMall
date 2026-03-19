package com.hana8.hanaro.repository;

import com.hana8.hanaro.common.enums.*;
import com.hana8.hanaro.config.QuerydslConfig;
import com.hana8.hanaro.dto.subscription.SubscriptionSearchRequest;
import com.hana8.hanaro.entity.Account;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.entity.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Import(QuerydslConfig.class)
class SubscriptionRepositoryTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AccountRepository accountRepository;

    private Member testMember;
    private Product testProduct;
    private Account testAccount;
    private Subscription testSubscription;

    @BeforeEach
    void setUp() {
        subscriptionRepository.deleteAll();
        accountRepository.deleteAll();
        productRepository.deleteAll();
        memberRepository.deleteAll();
        
        testMember = Member.builder()
                .email("test@test.com")
                .password("password")
                .nickname("tester")
                .roles(Set.of(MemberRole.ROLE_USER))
                .build();
        memberRepository.save(testMember);

        testProduct = Product.builder()
                .name("Test Product")
                .productType(ProductType.SAVINGS)
                .paymentAmount(1000L)
                .periodMonths(12)
                .maturityInterestRate(3.0)
                .earlyTerminationInterestRate(1.0)
                .isActive(true)
                .build();
        productRepository.save(testProduct);

        testAccount = Account.builder()
                .accountNumber("12345678901")
                .accountType(AccountType.FREE_DEPOSIT)
                .balance(10000L)
                .status(AccountStatus.ACTIVE)
                .openedAt(LocalDateTime.now())
                .member(testMember)
                .build();
        accountRepository.save(testAccount);

        testSubscription = Subscription.builder()
                .member(testMember)
                .product(testProduct)
                .withdrawAccount(testAccount)
                .subscriptionAccount(testAccount) // Simple setup for test
                .status(SubscriptionStatus.ACTIVE)
                .principalAmount(1000L)
                .build();
        subscriptionRepository.save(testSubscription);
    }

    @Test
    @DisplayName("전체 조건으로 가입 내역 검색")
    void searchSubscriptions_AllConditions() {
        SubscriptionSearchRequest request = SubscriptionSearchRequest.builder()
                .memberId(testMember.getId())
                .productId(testProduct.getId())
                .status(SubscriptionStatus.ACTIVE)
                .build();

        List<Subscription> results = subscriptionRepository.searchSubscriptions(request);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getMember().getId()).isEqualTo(testMember.getId());
    }

    @Test
    @DisplayName("회원 ID 조건으로 가입 내역 검색")
    void searchSubscriptions_MemberId() {
        SubscriptionSearchRequest request = SubscriptionSearchRequest.builder()
                .memberId(testMember.getId())
                .build();

        List<Subscription> results = subscriptionRepository.searchSubscriptions(request);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getMember().getId()).isEqualTo(testMember.getId());
    }

    @Test
    @DisplayName("상품 ID 조건으로 가입 내역 검색")
    void searchSubscriptions_ProductId() {
        SubscriptionSearchRequest request = SubscriptionSearchRequest.builder()
                .productId(testProduct.getId())
                .build();

        List<Subscription> results = subscriptionRepository.searchSubscriptions(request);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getProduct().getId()).isEqualTo(testProduct.getId());
    }

    @Test
    @DisplayName("상태 조건으로 가입 내역 검색")
    void searchSubscriptions_Status() {
        SubscriptionSearchRequest request = SubscriptionSearchRequest.builder()
                .status(SubscriptionStatus.ACTIVE)
                .build();

        List<Subscription> results = subscriptionRepository.searchSubscriptions(request);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getStatus()).isEqualTo(SubscriptionStatus.ACTIVE);

        SubscriptionSearchRequest requestTerminated = SubscriptionSearchRequest.builder()
                .status(SubscriptionStatus.TERMINATED)
                .build();
        List<Subscription> terminatedResults = subscriptionRepository.searchSubscriptions(requestTerminated);
        assertThat(terminatedResults).isEmpty();
    }

    @Test
    @DisplayName("빈 조건으로 가입 내역 검색")
    void searchSubscriptions_EmptyRequest() {
        SubscriptionSearchRequest request = SubscriptionSearchRequest.builder().build();
        List<Subscription> results = subscriptionRepository.searchSubscriptions(request);
        assertThat(results).hasSize(1);
    }
}
