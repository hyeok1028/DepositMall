package com.hana8.hanaro.service;

import com.hana8.hanaro.common.enums.AccountType;
import com.hana8.hanaro.common.enums.MemberRole;
import com.hana8.hanaro.common.exception.ApiException;
import com.hana8.hanaro.common.exception.ErrorCode;
import com.hana8.hanaro.dto.auth.LoginRequestDTO;
import com.hana8.hanaro.dto.auth.LoginResponseDTO;
import com.hana8.hanaro.dto.auth.SignUpRequestDTO;
import com.hana8.hanaro.dto.member.MemberResponse;
import com.hana8.hanaro.entity.Account;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.mapper.MemberMapper;
import com.hana8.hanaro.repository.AccountRepository;
import com.hana8.hanaro.repository.MemberRepository;
import com.hana8.hanaro.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberMapper memberMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountNumberGeneratorService accountNumberGeneratorService;

    @InjectMocks
    private AuthService authService;

    private SignUpRequestDTO signUpDto;
    private LoginRequestDTO loginDto;
    private Member member;

    @BeforeEach
    void setUp() {
        signUpDto = SignUpRequestDTO.builder()
                .email("test@test.com")
                .password("password")
                .nickname("tester")
                .tid("tid123")
                .build();

        loginDto = LoginRequestDTO.builder()
                .email("test@test.com")
                .password("password")
                .build();

        member = Member.builder()
                .id(1L)
                .email("test@test.com")
                .password("encodedPassword")
                .nickname("tester")
                .isActive(true)
                .roles(Set.of(MemberRole.ROLE_USER))
                .build();
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUp_Success() {
        given(memberRepository.existsByEmail(signUpDto.getEmail())).willReturn(false);
        given(passwordEncoder.encode(signUpDto.getPassword())).willReturn("encodedPassword");
        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(accountNumberGeneratorService.generate(null)).willReturn("12345678901");
        given(memberMapper.toResponse(any(Member.class))).willReturn(new MemberResponse());

        MemberResponse result = authService.signUp(signUpDto);

        assertThat(result).isNotNull();
        verify(memberRepository).save(any(Member.class));
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 이메일")
    void signUp_DuplicateEmail() {
        given(memberRepository.existsByEmail(signUpDto.getEmail())).willReturn(true);

        assertThatThrownBy(() -> authService.signUp(signUpDto))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CONFLICT)
                .hasFieldOrPropertyWithValue("status", HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("회원가입 실패 - TID 누락")
    void signUp_NoTid() {
        signUpDto.setTid(null);
        given(memberRepository.existsByEmail(signUpDto.getEmail())).willReturn(false);
        given(passwordEncoder.encode(signUpDto.getPassword())).willReturn("encodedPassword");
        given(memberRepository.save(any(Member.class))).willReturn(member);

        assertThatThrownBy(() -> authService.signUp(signUpDto))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.VALIDATION_ERROR);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void login_Success() {
        given(memberRepository.findByEmail(loginDto.getEmail())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(loginDto.getPassword(), member.getPassword())).willReturn(true);
        given(jwtTokenProvider.createAccessToken(member.getId(), member.getEmail(), member.getRoles()))
                .willReturn("testToken");

        LoginResponseDTO result = authService.login(loginDto);

        assertThat(result.getAccessToken()).isEqualTo("testToken");
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    void login_UserNotFound() {
        given(memberRepository.findByEmail(loginDto.getEmail())).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginDto))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.UNAUTHORIZED);
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_InvalidPassword() {
        given(memberRepository.findByEmail(loginDto.getEmail())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(loginDto.getPassword(), member.getPassword())).willReturn(false);

        assertThatThrownBy(() -> authService.login(loginDto))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.UNAUTHORIZED);
    }

    @Test
    @DisplayName("로그인 실패 - 비활성 계정")
    void login_InactiveMember() {
        member.setIsActive(false);
        given(memberRepository.findByEmail(loginDto.getEmail())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(loginDto.getPassword(), member.getPassword())).willReturn(true);

        assertThatThrownBy(() -> authService.login(loginDto))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.FORBIDDEN);
    }

    @Test
    @DisplayName("내 정보 조회 성공")
    void me_Success() {
        given(memberRepository.findByEmail(member.getEmail())).willReturn(Optional.of(member));
        given(memberMapper.toResponse(member)).willReturn(new MemberResponse());

        MemberResponse result = authService.me(member.getEmail());

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("내 정보 조회 실패 - 회원 없음")
    void me_NotFound() {
        given(memberRepository.findByEmail("none@test.com")).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.me("none@test.com"))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_FOUND);
    }
}
