package com.hana8.hanaro.dto.account;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hana8.hanaro.common.enums.AccountStatus;
import com.hana8.hanaro.common.enums.AccountType;
import com.hana8.hanaro.common.serializer.AccountNumberSerializer;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private Long id;

    @JsonSerialize(using = AccountNumberSerializer.class)
    private String accountNumber;

    private AccountType accountType;
    private Long balance;
    private AccountStatus status;
    private String tid;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private Long memberId;
}
