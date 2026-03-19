package com.hana8.hanaro.entity;

import com.hana8.hanaro.common.enums.PaymentStatus;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payment_history")
public class PaymentHistory extends BaseEntity {

    @Id
    @Tsid
    private Long id;

    private LocalDate scheduledDate;

    private LocalDate paymentDate;

    @Column(nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(length = 255)
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    public void markSuccess(LocalDate paymentDate) {
        this.status = PaymentStatus.SUCCESS;
        this.paymentDate = paymentDate;
    }

    public void markFailed(String memo) {
        this.status = PaymentStatus.FAILED;
        this.memo = memo;
    }

    public void markSkipped(String memo) {
        this.status = PaymentStatus.SKIPPED;
        this.memo = memo;
    }
}
