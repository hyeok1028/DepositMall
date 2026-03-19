package com.hana8.hanaro.dto.account;

import com.hana8.hanaro.common.enums.AccountType;
import com.hana8.hanaro.common.validator.AccountNumber;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountCreateRequest {

    @AccountNumber
    private String accountNumber;

    private AccountType accountType;

    @Min(0)
    private Long balance;

    @NotNull
    private Long memberId;
}
