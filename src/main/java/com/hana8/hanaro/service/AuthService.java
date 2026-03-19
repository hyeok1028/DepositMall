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
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger userLogger = LoggerFactory.getLogger("userLogger");

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountRepository accountRepository;
    private final AccountNumberGeneratorService accountNumberGeneratorService;

    @Transactional
    public MemberResponse signUp(SignUpRequestDTO dto) {
        if (memberRepository.existsByEmail(dto.getEmail())) {
            userLogger.error("회원가입 실패 - 중복 이메일: email={}", dto.getEmail());
            throw new ApiException(ErrorCode.CONFLICT, HttpStatus.CONFLICT, "email already in use");
        }

        Member member = Member.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .isActive(true)
                .roles(Set.of(MemberRole.ROLE_USER))
                .build();

        Member savedMember = memberRepository.save(member);

        String accountNumber = accountNumberGeneratorService.generate(null);
        Account account = Account.open(savedMember, accountNumber, AccountType.FREE_DEPOSIT, 0L);
        accountRepository.save(account);

        userLogger.info("회원가입 성공 - memberId={}, email={}, nickname={}", savedMember.getId(), savedMember.getEmail(), savedMember.getNickname());
        return memberMapper.toResponse(savedMember);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> {
                    userLogger.warn("로그인 실패 - 존재하지 않는 이메일: email={}", dto.getEmail());
                    return new ApiException(ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED, "invalid credentials");
                });

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            userLogger.warn("로그인 실패 - 비밀번호 불일치: email={}", dto.getEmail());
            throw new ApiException(ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED, "invalid credentials");
        }

        if (!Boolean.TRUE.equals(member.getIsActive())) {
            userLogger.warn("로그인 실패 - 비활성 계정: email={}", dto.getEmail());
            throw new ApiException(ErrorCode.FORBIDDEN, HttpStatus.FORBIDDEN, "inactive member");
        }

        String token = jwtTokenProvider.createAccessToken(member.getEmail(), member.getRoles());

        userLogger.info("로그인 성공 - memberId={}, email={}", member.getId(), member.getEmail());
        return LoginResponseDTO.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .memberId(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

    public MemberResponse me(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND, "member not found"));

        return memberMapper.toResponse(member);
    }
}
