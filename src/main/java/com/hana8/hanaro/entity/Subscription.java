package com.hana8.hanaro.entity;

import com.hana8.hanaro.common.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_account_id", nullable = false)
    private Account subscriptionAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubscriptionStatus status;

    private LocalDate startedAt;

    private LocalDate maturityDate;

    @Column(nullable = false)
    private Long principalAmount;

    @Builder.Default
    private Long accumulatedInterest = 0L;

    @Builder.Default
    private Long totalPaidAmount = 0L;

    private Long expectedMaturityAmount;

    private LocalDate nextPaymentDate;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PaymentHistory> paymentHistories = new ArrayList<>();

    public void terminate(Long accumulatedInterest) {
        this.status = SubscriptionStatus.TERMINATED;
        this.accumulatedInterest = accumulatedInterest;
    }

    public void mature(Long accumulatedInterest) {
        this.status = SubscriptionStatus.MATURED;
        this.accumulatedInterest = accumulatedInterest;
    }

    public void updatePaidAmount(Long amount) {
        this.totalPaidAmount += amount;
    }

    public void updateNextPaymentDate(LocalDate nextPaymentDate) {
        this.nextPaymentDate = nextPaymentDate;
    }
}
