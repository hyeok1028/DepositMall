package com.hana8.hanaro.repository;

import com.hana8.hanaro.common.enums.AccountStatus;
import com.hana8.hanaro.common.enums.AccountType;
import com.hana8.hanaro.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

    List<Account> findByMemberId(Long memberId);

    List<Account> findByStatus(AccountStatus status);

    List<Account> findByAccountType(AccountType accountType);

    List<Account> findByMemberIdAndStatus(Long memberId, AccountStatus status);

}