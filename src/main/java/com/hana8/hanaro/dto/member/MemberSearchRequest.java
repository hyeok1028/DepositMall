package com.hana8.hanaro.dto.member;

import lombok.Data;

@Data
public class MemberSearchRequest {
    private String nickname;
    private String email;
    private Boolean isActive;
}
