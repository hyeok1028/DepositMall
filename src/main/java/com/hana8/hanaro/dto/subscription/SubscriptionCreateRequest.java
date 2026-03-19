package com.hana8.hanaro.dto.subscription;

import com.hana8.hanaro.common.validator.AccountNumber;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionCreateRequest {

    @NotNull
    private Long memberId;

    @NotNull
    private Long productId;

    @NotNull
    private Long withdrawAccountId;

    @NotNull
    @Min(1)
    private Long principalAmount;

    @AccountNumber
    private String desiredAccountNumber;
}
