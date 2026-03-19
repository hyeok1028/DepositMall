package com.hana8.hanaro.dto.subscription;

import com.hana8.hanaro.common.enums.SubscriptionStatus;
import lombok.Data;

@Data
public class SubscriptionSearchRequest {
    private Long memberId;
    private Long productId;
    private SubscriptionStatus status;
}
