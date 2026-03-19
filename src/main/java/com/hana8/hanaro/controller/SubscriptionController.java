package com.hana8.hanaro.controller;

import com.hana8.hanaro.common.response.ApiResponse;
import com.hana8.hanaro.dto.subscription.CancelSubscriptionRequest;
import com.hana8.hanaro.dto.subscription.InterestResponse;
import com.hana8.hanaro.dto.subscription.SubscriptionCreateRequest;
import com.hana8.hanaro.dto.subscription.SubscriptionResponse;
import com.hana8.hanaro.dto.subscription.SubscriptionSearchRequest;
import com.hana8.hanaro.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@Tag(name = "User - Subscription", description = "사용자 상품 가입 관리 API")
public class SubscriptionController {

    private final SubscriptionService service;

    @GetMapping({"", "/"})
    @Operation(summary = "가입 내역 조회", description = "사용자의 상품 가입 내역을 조회합니다. 검색 조건을 지원합니다.")
    public ResponseEntity<ApiResponse<List<SubscriptionResponse>>> getSubscriptions(SubscriptionSearchRequest dto) {
        List<SubscriptionResponse> response = service.getSubscriptions(dto);
        return ResponseEntity.ok(ApiResponse.success(response, "Subscriptions retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "가입 상세 조회", description = "특정 상품 가입 건의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> getSubscription(@PathVariable Long id) {
        SubscriptionResponse response = service.getSubscription(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Subscription details retrieved"));
    }

    @PostMapping({"", "/"})
    @Operation(summary = "상품 가입", description = "선택한 상품에 가입합니다. 연결 계좌에서 최초 납입금이 이체됩니다. 성공 시 success: true를 반환합니다.")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> registSubscription(@Valid @RequestBody SubscriptionCreateRequest dto) {
        SubscriptionResponse response = service.registSubscription(dto);
        return ResponseEntity.ok(ApiResponse.success(response, "Subscription registered successfully"));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "상품 해지", description = "가입된 상품을 중도 해지하거나 만기 해지합니다. 원금과 이자가 연결 계좌로 입금됩니다. 성공 시 success: true를 반환합니다.")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> cancelSubscription(@PathVariable Long id,
                                                  @RequestBody CancelSubscriptionRequest dto) {
        SubscriptionResponse response = service.cancelSubscription(id, dto);
        return ResponseEntity.ok(ApiResponse.success(response, "Subscription cancelled successfully"));
    }

    @GetMapping("/{id}/interest")
    @Operation(summary = "예상 이자 조회", description = "현재 시점 또는 만기 시점의 예상 이자를 계산하여 조회합니다.")
    public ResponseEntity<ApiResponse<InterestResponse>> getInterest(@PathVariable Long id) {
        InterestResponse response = service.getInterest(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Interest calculation retrieved"));
    }
}
