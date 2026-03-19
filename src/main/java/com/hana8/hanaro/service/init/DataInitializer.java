package com.hana8.hanaro.service.init;

import com.hana8.hanaro.common.enums.AccountType;
import com.hana8.hanaro.common.enums.MemberRole;
import com.hana8.hanaro.common.enums.ProductType;
import com.hana8.hanaro.common.enums.PaymentCycle;
import com.hana8.hanaro.config.AdminProperties;
import com.hana8.hanaro.entity.Account;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.repository.AccountRepository;
import com.hana8.hanaro.repository.MemberRepository;
import com.hana8.hanaro.repository.ProductRepository;
import com.hana8.hanaro.service.AccountNumberGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

// @Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;
    private final AccountNumberGeneratorService accountNumberGeneratorService;

    @Override
    public void run(ApplicationArguments args) {
        initAdmin();
        initProducts();
    }

    private void initAdmin() {
        if (!memberRepository.existsByEmail(adminProperties.getEmail())) {
            Member admin = Member.builder()
                    .email(adminProperties.getEmail())
                    .password(passwordEncoder.encode(adminProperties.getPassword()))
                    .nickname(adminProperties.getNickname())
                    .isActive(true)
                    .roles(Set.of(MemberRole.ROLE_ADMIN))
                    .build();
            Member saved = memberRepository.save(admin);

            String accountNumber = accountNumberGeneratorService.generate(null);
            Account account = Account.open(saved, accountNumber, AccountType.FREE_DEPOSIT, 0L, "INIT_ADMIN");
            accountRepository.save(account);
        }
    }

    private void initProducts() {
        if (productRepository.count() == 0) {
            Product p1 = Product.builder()
                    .name("하나로 자유적금(월)")
                    .productType(ProductType.SAVINGS)
                    .paymentAmount(100000L)
                    .paymentCycle(PaymentCycle.MONTHLY)
                    .periodMonths(12)
                    .maturityInterestRate(4.5)
                    .earlyTerminationInterestRate(1.5)
                    .description("매월 납입하는 자유로운 적금 상품입니다.")
                    .isActive(true)
                    .build();

            Product p2 = Product.builder()
                    .name("하나로 주거래적금(주)")
                    .productType(ProductType.SAVINGS)
                    .paymentAmount(50000L)
                    .paymentCycle(PaymentCycle.WEEKLY)
                    .periodMonths(24)
                    .maturityInterestRate(5.0)
                    .earlyTerminationInterestRate(2.0)
                    .description("매주 납입하여 목돈을 만드는 상품입니다.")
                    .isActive(true)
                    .build();

            Product p3 = Product.builder()
                    .name("하나로 정기예금(6개월)")
                    .productType(ProductType.DEPOSIT)
                    .paymentAmount(1000000L)
                    .periodMonths(6)
                    .maturityInterestRate(3.8)
                    .earlyTerminationInterestRate(1.2)
                    .description("6개월 단기 정기예금 상품입니다.")
                    .isActive(true)
                    .build();

            Product p4 = Product.builder()
                    .name("하나로 정기예금(12개월)")
                    .productType(ProductType.DEPOSIT)
                    .paymentAmount(5000000L)
                    .periodMonths(12)
                    .maturityInterestRate(4.0)
                    .earlyTerminationInterestRate(1.5)
                    .description("12개월 고금리 정기예금 상품입니다.")
                    .isActive(true)
                    .build();

            Product p5 = Product.builder()
                    .name("하나로 청년적금")
                    .productType(ProductType.SAVINGS)
                    .paymentAmount(200000L)
                    .paymentCycle(PaymentCycle.MONTHLY)
                    .periodMonths(36)
                    .maturityInterestRate(6.0)
                    .earlyTerminationInterestRate(2.5)
                    .description("청년을 위한 우대금리 적금 상품입니다.")
                    .isActive(true)
                    .build();

            productRepository.saveAll(List.of(p1, p2, p3, p4, p5));
        }
    }
}
