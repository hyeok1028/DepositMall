package com.hana8.hanaro.service;

import com.hana8.hanaro.common.enums.AccountType;
import com.hana8.hanaro.dto.account.AccountCreateRequest;
import com.hana8.hanaro.dto.account.AccountResponse;
import com.hana8.hanaro.entity.Account;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.mapper.AccountMapper;
import com.hana8.hanaro.repository.AccountRepository;
import com.hana8.hanaro.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final MemberRepository memberRepository;
    private final AccountMapper mapper;
    private final AccountNumberGeneratorService accountNumberGeneratorService;

    public List<AccountResponse> getAccounts() {
        return mapper.toResponseList(repository.findAll());
    }

    public List<AccountResponse> getAccountsByMemberId(Long memberId) {
        return mapper.toResponseList(repository.findByMemberId(memberId));
    }

    public AccountResponse getAccount(Long id) {
        Account account = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account #%d is not found!".formatted(id)));

        return mapper.toResponse(account);
    }

    public AccountResponse registAccount(AccountCreateRequest dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member #%d is not found!".formatted(dto.getMemberId())));

        String accountNumber = accountNumberGeneratorService.generate(dto.getAccountNumber());

        Account account = Account.open(
                member,
                accountNumber,
                dto.getAccountType() != null ? dto.getAccountType() : AccountType.FREE_DEPOSIT,
                dto.getBalance() != null ? dto.getBalance() : 0L
        );

        return mapper.toResponse(repository.save(account));
    }

    public int closeAccount(Long id) {
        Account account = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account #%d is not found!".formatted(id)));

        account.close(LocalDateTime.now());
        repository.save(account);
        return 1;
    }
}
