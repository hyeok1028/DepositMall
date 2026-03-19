package com.hana8.hanaro.controller;

import com.hana8.hanaro.common.response.ApiResponse;
import com.hana8.hanaro.dto.auth.LoginRequestDTO;
import com.hana8.hanaro.dto.auth.LoginResponseDTO;
import com.hana8.hanaro.dto.auth.SignUpRequestDTO;
import com.hana8.hanaro.dto.member.MemberResponse;
import com.hana8.hanaro.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 및 회원가입 관련 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "새로운 회원을 등록하고 자유입출금 계좌를 자동 생성합니다. 성공 시 success: true를 반환합니다.")
    public ResponseEntity<ApiResponse<MemberResponse>> signUp(@Valid @RequestBody SignUpRequestDTO dto) {
        MemberResponse response = authService.signUp(dto);
        return ResponseEntity.ok(ApiResponse.success(response, "Sign up successful"));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "아이디와 비밀번호를 검증하고 JWT 토큰을 발급합니다. 성공 시 success: true를 반환합니다.")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
    }

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "로그인된 현재 사용자의 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<MemberResponse>> me(Principal principal) {
        MemberResponse response = authService.me(principal.getName());
        return ResponseEntity.ok(ApiResponse.success(response, "Member info retrieved"));
    }
}
