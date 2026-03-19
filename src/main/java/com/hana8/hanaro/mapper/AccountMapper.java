package com.hana8.hanaro.mapper;

import com.hana8.hanaro.dto.account.AccountCreateRequest;
import com.hana8.hanaro.dto.account.AccountResponse;
import com.hana8.hanaro.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "memberId", source = "member.id")
    AccountResponse toResponse(Account account);

    @Mapping(target = "member", ignore = true)
    Account toEntity(AccountCreateRequest dto);

    List<AccountResponse> toResponseList(List<Account> accounts);
}
