package com.hana8.hanaro.repository;

import com.hana8.hanaro.common.enums.AccountStatus;
import com.hana8.hanaro.common.enums.AccountType;
import com.hana8.hanaro.common.enums.MemberRole;
import com.hana8.hanaro.config.QuerydslConfig;
import com.hana8.hanaro.entity.Account;
import com.hana8.hanaro.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Import(QuerydslConfig.class)
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .email("test@test.com")
                .password("password")
                .nickname("tester")
                .roles(Set.of(MemberRole.ROLE_USER))
                .build();
        memberRepository.save(testMember);
    }

    @Test
    @DisplayName("계좌 생성 및 회원별 조회 테스트")
    void saveAndFindByMemberId() {
        Account account = Account.builder()
                .accountNumber("01234567890")
                .accountType(AccountType.FREE_DEPOSIT)
                .balance(1000L)
                .openedAt(LocalDateTime.now())
                .status(AccountStatus.ACTIVE)
                .member(testMember)
                .build();
        accountRepository.save(account);

        List<Account> accounts = accountRepository.findByMemberId(testMember.getId());
        assertThat(accounts).hasSize(1);
        assertThat(accounts.get(0).getAccountNumber()).isEqualTo("01234567890");
    }

    @Test
    @DisplayName("계좌번호로 조회 테스트")
    void findByAccountNumber() {
        Account account = Account.builder()
                .accountNumber("11122233344")
                .accountType(AccountType.SAVINGS)
                .balance(0L)
                .status(AccountStatus.ACTIVE)
                .openedAt(LocalDateTime.now())
                .member(testMember)
                .build();
        accountRepository.save(account);

        Optional<Account> found = accountRepository.findByAccountNumber("11122233344");
        assertThat(found).isPresent();
        assertThat(found.get().getAccountNumber()).isEqualTo("11122233344");
    }
}
