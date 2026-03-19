package com.hana8.hanaro.controller;

import com.hana8.hanaro.common.response.ApiResponse;
import com.hana8.hanaro.dto.account.AccountResponse;
import com.hana8.hanaro.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "User - Account", description = "사용자 계좌 관리 API")
public class AccountController {

    private final AccountService service;

    @GetMapping("/member/{memberId}")
    @Operation(summary = "회원별 계좌 목록 조회", description = "특정 회원이 보유한 모든 계좌(자유입출금, 예금, 적금) 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAccountsByMemberId(@PathVariable Long memberId) {
        List<AccountResponse> response = service.getAccountsByMemberId(memberId);
        return ResponseEntity.ok(ApiResponse.success(response, "Accounts retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "계좌 상세 조회", description = "특정 계좌의 잔액 및 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(@PathVariable Long id) {
        AccountResponse response = service.getAccount(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Account details retrieved"));
    }
}
