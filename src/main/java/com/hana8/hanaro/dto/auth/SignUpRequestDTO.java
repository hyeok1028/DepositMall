package com.hana8.hanaro.dto.auth;

import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDTO {
    private String email;
    private String password;
    private String nickname;
    private Set<String> roles;
    private String tid;
}
