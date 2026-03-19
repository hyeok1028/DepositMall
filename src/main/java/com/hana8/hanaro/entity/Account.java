package com.hana8.hanaro.entity;

import com.hana8.hanaro.common.enums.AccountStatus;
import com.hana8.hanaro.common.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "accounts",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_account_number", columnNames = "accountNumber")
        })
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 11)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountType accountType;

    @Column(nullable = false)
    @Builder.Default
    private Long balance = 0L;

    private LocalDateTime openedAt;

    private LocalDateTime closedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void close(LocalDateTime closedAt) {
        this.status = AccountStatus.CLOSED;
        this.closedAt = closedAt;
    }

    public void deposit(Long amount) {
        if (amount == null || amount < 0) throw new IllegalArgumentException("invalid amount");
        this.balance += amount;
    }

    public void withdraw(Long amount) {
        if (amount == null || amount < 0) throw new IllegalArgumentException("invalid amount");
        if (this.balance < amount) throw new IllegalArgumentException("insufficient balance");
        this.balance -= amount;
    }

    public static Account open(Member member, String accountNumber, AccountType accountType, Long balance) {
        return Account.builder()
                .member(member)
                .accountNumber(accountNumber)
                .accountType(accountType)
                .balance(balance)
                .openedAt(LocalDateTime.now())
                .status(AccountStatus.ACTIVE)
                .build();
    }
}
