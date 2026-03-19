package com.hana8.hanaro.dto.subscription;

import com.hana8.hanaro.common.enums.SubscriptionStatus;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponse {
    private Long id;
    private SubscriptionStatus status;
    private LocalDate startedAt;
    private LocalDate maturityDate;
    private Long principalAmount;
    private Long accumulatedInterest;
    private Long totalPaidAmount;
    private Long expectedMaturityAmount;
    private LocalDate nextPaymentDate;
    private Long memberId;
    private Long productId;
    private Long withdrawAccountId;
    private Long subscriptionAccountId;
}
