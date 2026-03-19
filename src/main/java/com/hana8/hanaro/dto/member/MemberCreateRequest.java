package com.hana8.hanaro.dto.member;

import com.hana8.hanaro.common.enums.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class MemberCreateRequest {

    @NotBlank
    @Size(max = 15)
    private String nickname;

    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 8, max = 255)
    private String password;

    private Boolean isActive;
    private Set<MemberRole> roles;
}
