package com.hana8.hanaro.controller;

import com.hana8.hanaro.dto.account.AccountCreateRequest;
import com.hana8.hanaro.dto.account.AccountResponse;
import com.hana8.hanaro.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @GetMapping({"", "/"})
    public List<AccountResponse> getAccounts() {
        return service.getAccounts();
    }

    @GetMapping("/{id}")
    public AccountResponse getAccount(@PathVariable Long id) {
        return service.getAccount(id);
    }

    @PostMapping({"", "/"})
    public AccountResponse registAccount(@Valid @RequestBody AccountCreateRequest dto) {
        return service.registAccount(dto);
    }

    @PatchMapping("/{id}/close")
    public int closeAccount(@PathVariable Long id) {
        return service.closeAccount(id);
    }
}
