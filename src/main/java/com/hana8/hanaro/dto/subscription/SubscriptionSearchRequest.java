package com.hana8.hanaro.dto.subscription;

import com.hana8.hanaro.common.enums.SubscriptionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "상품 가입 내역 검색 요청")
public class SubscriptionSearchRequest {
    @Schema(description = "회원 식별자(ID)", example = "1")
    private Long memberId;

    @Schema(description = "상품 식별자(ID)", example = "5")
    private Long productId;

    @Schema(description = "가입 상태 (ACTIVE, MATURED, TERMINATED)", example = "ACTIVE")
    private SubscriptionStatus status;
}
