package com.hana8.hanaro.entity;

import com.hana8.hanaro.common.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "subscriptions")
public class Subscription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "withdraw_account_id", nullable = false)
    private Account withdrawAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_account_id", nullable = false)
    private Account subscriptionAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(nullable = false)
    private LocalDate startedAt;

    @Column(nullable = false)
    private LocalDate maturityDate;

    @Column(nullable = false)
    private Long principalAmount;

    @Column(nullable = false)
    private Long accumulatedInterest;

    @Column(nullable = false)
    private Long totalPaidAmount;

    @Column(nullable = false)
    private Long expectedMaturityAmount;

    private LocalDate nextPaymentDate;

    public void terminate(long accumulatedInterest) {
        this.status = SubscriptionStatus.TERMINATED;
        this.accumulatedInterest = accumulatedInterest;
    }

    public void mature(long accumulatedInterest, long expectedMaturityAmount) {
        this.status = SubscriptionStatus.MATURED;
        this.accumulatedInterest = accumulatedInterest;
        this.expectedMaturityAmount = expectedMaturityAmount;
    }
}
