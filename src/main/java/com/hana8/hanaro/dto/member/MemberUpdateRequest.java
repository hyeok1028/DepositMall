package com.hana8.hanaro.dto.member;

import com.hana8.hanaro.common.enums.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class MemberUpdateRequest {

    @NotNull
    private Long id;

    @Size(max = 15)
    private String nickname;

    @Email
    @Size(max = 100)
    private String email;

    @Size(min = 8, max = 255)
    private String password;

    private Boolean isActive;
    private Set<MemberRole> roles;
}
