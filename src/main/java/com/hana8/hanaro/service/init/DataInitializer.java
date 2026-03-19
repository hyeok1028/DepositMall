package com.hana8.hanaro.service.init;

import com.hana8.hanaro.common.enums.AccountType;
import com.hana8.hanaro.common.enums.MemberRole;
import com.hana8.hanaro.config.AdminProperties;
import com.hana8.hanaro.entity.Account;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.repository.AccountRepository;
import com.hana8.hanaro.repository.MemberRepository;
import com.hana8.hanaro.service.AccountNumberGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;
    private final AccountNumberGeneratorService accountNumberGeneratorService;

    @Override
    public void run(ApplicationArguments args) {
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
            Account account = Account.open(saved, accountNumber, AccountType.FREE_DEPOSIT, 0L);
            accountRepository.save(account);
        }
    }
}
