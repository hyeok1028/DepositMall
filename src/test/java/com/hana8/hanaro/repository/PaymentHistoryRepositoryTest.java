package com.hana8.hanaro.repository;

import com.hana8.hanaro.common.enums.*;
import com.hana8.hanaro.config.QuerydslConfig;
import com.hana8.hanaro.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Import(QuerydslConfig.class)
class PaymentHistoryRepositoryTest {

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AccountRepository accountRepository;

    private Subscription testSubscription;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .email("test@test.com")
                .password("password")
                .nickname("tester")
                .roles(Set.of(MemberRole.ROLE_USER))
                .build();
        memberRepository.save(member);

        Product product = Product.builder()
                .name("Test Product")
                .productType(ProductType.SAVINGS)
                .paymentAmount(1000L)
                .periodMonths(12)
                .maturityInterestRate(3.0)
                .earlyTerminationInterestRate(1.0)
                .isActive(true)
                .build();
        productRepository.save(product);

        Account account = Account.builder()
                .accountNumber("12345678901")
                .accountType(AccountType.FREE_DEPOSIT)
                .balance(10000L)
                .status(AccountStatus.ACTIVE)
                .openedAt(LocalDateTime.now())
                .member(member)
                .build();
        accountRepository.save(account);

        testSubscription = Subscription.builder()
                .member(member)
                .product(product)
                .withdrawAccount(account)
                .subscriptionAccount(account)
                .status(SubscriptionStatus.ACTIVE)
                .principalAmount(1000L)
                .build();
        subscriptionRepository.save(testSubscription);
    }

    @Test
    @DisplayName("결제 내역 저장 및 가입 ID별 조회 테스트")
    void saveAndFindBySubscriptionId() {
        PaymentHistory history = PaymentHistory.builder()
                .subscription(testSubscription)
                .amount(1000L)
                .status(PaymentStatus.SUCCESS)
                .scheduledDate(LocalDate.now())
                .paymentDate(LocalDate.now())
                .build();
        paymentHistoryRepository.save(history);

        List<PaymentHistory> histories = paymentHistoryRepository.findBySubscriptionId(testSubscription.getId());
        assertThat(histories).hasSize(1);
        assertThat(histories.get(0).getAmount()).isEqualTo(1000L);
    }
}
