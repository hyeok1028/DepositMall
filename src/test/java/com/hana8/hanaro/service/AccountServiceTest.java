package com.hana8.hanaro.service;

import com.hana8.hanaro.common.enums.AccountType;
import com.hana8.hanaro.dto.account.AccountCreateRequest;
import com.hana8.hanaro.dto.account.AccountResponse;
import com.hana8.hanaro.entity.Account;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.mapper.AccountMapper;
import com.hana8.hanaro.repository.AccountRepository;
import com.hana8.hanaro.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository repository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private AccountMapper mapper;
    @Mock
    private AccountNumberGeneratorService accountNumberGeneratorService;

    @InjectMocks
    private AccountService accountService;

    private Member testMember;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        testMember = Member.builder().id(1L).email("test@test.com").build();
        testAccount = Account.builder()
                .id(1L)
                .member(testMember)
                .accountNumber("12345678901")
                .accountType(AccountType.FREE_DEPOSIT)
                .balance(1000L)
                .build();
    }

    @Test
    @DisplayName("모든 계좌 조회 테스트")
    void getAccounts() {
        given(repository.findAll()).willReturn(List.of(testAccount));
        given(mapper.toResponseList(anyList())).willReturn(List.of(new AccountResponse()));

        List<AccountResponse> result = accountService.getAccounts();

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("회원 ID로 계좌 조회 테스트")
    void getAccountsByMemberId() {
        given(repository.findByMemberId(1L)).willReturn(List.of(testAccount));
        given(mapper.toResponseList(anyList())).willReturn(List.of(new AccountResponse()));

        List<AccountResponse> result = accountService.getAccountsByMemberId(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("계좌 상세 조회 - 성공")
    void getAccount_Success() {
        given(repository.findById(1L)).willReturn(Optional.of(testAccount));
        given(mapper.toResponse(testAccount)).willReturn(new AccountResponse());

        AccountResponse result = accountService.getAccount(1L);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("계좌 상세 조회 - 실패 (존재하지 않음)")
    void getAccount_NotFound() {
        given(repository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccount(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account #99 is not found!");
    }

    @Test
    @DisplayName("계좌 등록 - 성공 (전체 필드)")
    void registAccount_Success() {
        AccountCreateRequest dto = new AccountCreateRequest();
        dto.setMemberId(1L);
        dto.setAccountNumber("11112222333");
        dto.setAccountType(AccountType.SAVINGS);
        dto.setBalance(5000L);

        given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
        given(accountNumberGeneratorService.generate("11112222333")).willReturn("11112222333");
        given(repository.save(any(Account.class))).willReturn(testAccount);
        given(mapper.toResponse(any(Account.class))).willReturn(new AccountResponse());

        AccountResponse result = accountService.registAccount(dto);

        assertThat(result).isNotNull();
        verify(repository).save(any(Account.class));
    }

    @Test
    @DisplayName("계좌 등록 - 성공 (기본값)")
    void registAccount_DefaultValues() {
        AccountCreateRequest dto = new AccountCreateRequest();
        dto.setMemberId(1L);

        given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
        given(accountNumberGeneratorService.generate(null)).willReturn("12345678901");
        given(repository.save(any(Account.class))).willReturn(testAccount);
        given(mapper.toResponse(any(Account.class))).willReturn(new AccountResponse());

        AccountResponse result = accountService.registAccount(dto);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("계좌 등록 - 실패 (회원 없음)")
    void registAccount_MemberNotFound() {
        AccountCreateRequest dto = new AccountCreateRequest();
        dto.setMemberId(99L);

        given(memberRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.registAccount(dto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("계좌 해지 - 성공")
    void closeAccount_Success() {
        given(repository.findById(1L)).willReturn(Optional.of(testAccount));

        int result = accountService.closeAccount(1L);

        assertThat(result).isEqualTo(1);
        assertThat(testAccount.getClosedAt()).isNotNull();
        verify(repository).save(testAccount);
    }
}
