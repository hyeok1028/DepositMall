package com.hana8.hanaro.controller;

import com.hana8.hanaro.dto.auth.LoginRequestDTO;
import com.hana8.hanaro.dto.auth.LoginResponseDTO;
import com.hana8.hanaro.dto.auth.SignUpRequestDTO;
import com.hana8.hanaro.dto.member.MemberResponse;
import com.hana8.hanaro.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/signup")
    public MemberResponse signUp(@Valid @RequestBody SignUpRequestDTO dto) {
        return service.signUp(dto);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO dto) {
        return service.login(dto);
    }

    @GetMapping("/me")
    public MemberResponse me(Authentication authentication) {
        return service.me(authentication.getName());
    }
}
