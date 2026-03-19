package com.hana8.hanaro.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "회원 검색 요청")
public class MemberSearchRequest {
    @Schema(description = "닉네임 (부분 일치)", example = "하나")
    private String nickname;

    @Schema(description = "이메일 (부분 일치)", example = "hana@hanabank.com")
    private String email;

    @Schema(description = "활성 상태 여부", example = "true")
    private Boolean isActive;

    @Schema(description = "가입 경과 일수 (최근 N일 이내 가입자 조회)", example = "7")
    private Integer days;
}
