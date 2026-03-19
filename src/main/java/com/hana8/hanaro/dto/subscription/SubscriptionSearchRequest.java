package com.hana8.hanaro.dto.subscription;

import com.hana8.hanaro.common.enums.SubscriptionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "가입 내역 검색 요청")
public class SubscriptionSearchRequest {
    @Schema(description = "회원 ID", example = "123456789")
    private Long memberId;

    @Schema(description = "상품 ID", example = "987654321")
    private Long productId;

    @Schema(description = "가입 상태", example = "ACTIVE")
    private SubscriptionStatus status;
}
