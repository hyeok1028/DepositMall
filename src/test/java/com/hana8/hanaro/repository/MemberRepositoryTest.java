package com.hana8.hanaro.repository;

import com.hana8.hanaro.common.enums.MemberRole;
import com.hana8.hanaro.config.QuerydslConfig;
import com.hana8.hanaro.dto.member.MemberSearchRequest;
import com.hana8.hanaro.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Import(QuerydslConfig.class)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    private Member activeMember;
    private Member inactiveMember;

    @BeforeEach
    void setUp() {
        paymentHistoryRepository.deleteAll();
        subscriptionRepository.deleteAll();
        accountRepository.deleteAll();
        memberRepository.deleteAll();

        activeMember = Member.builder()
                .email("active@test.com")
                .password("password")
                .nickname("active")
                .isActive(true)
                .roles(Set.of(MemberRole.ROLE_USER))
                .build();
        
        inactiveMember = Member.builder()
                .email("inactive@test.com")
                .password("password")
                .nickname("inactive")
                .isActive(false)
                .roles(Set.of(MemberRole.ROLE_USER))
                .build();

        memberRepository.save(activeMember);
        memberRepository.save(inactiveMember);
    }

    @Test
    @DisplayName("회원 저장 및 이메일 조회 테스트")
    void saveAndFindByEmail() {
        Optional<Member> found = memberRepository.findByEmail("active@test.com");
        assertThat(found).isPresent();
        assertThat(found.get().getNickname()).isEqualTo("active");
    }

    @Test
    @DisplayName("전체 조건으로 회원 검색")
    void searchMembers_AllConditions() {
        MemberSearchRequest request = MemberSearchRequest.builder()
                .nickname("active")
                .email("active@test.com")
                .isActive(true)
                .days(1)
                .build();

        List<Member> results = memberRepository.searchMembers(request);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getEmail()).isEqualTo("active@test.com");
    }

    @Test
    @DisplayName("닉네임 조건으로 회원 검색")
    void searchMembers_Nickname() {
        MemberSearchRequest request = MemberSearchRequest.builder()
                .nickname("inactive")
                .build();

        List<Member> results = memberRepository.searchMembers(request);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getNickname()).isEqualTo("inactive");
    }

    @Test
    @DisplayName("이메일 조건으로 회원 검색")
    void searchMembers_Email() {
        MemberSearchRequest request = MemberSearchRequest.builder()
                .email("inactive@test.com")
                .build();

        List<Member> results = memberRepository.searchMembers(request);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getEmail()).isEqualTo("inactive@test.com");
    }

    @Test
    @DisplayName("활성화 상태 조건으로 회원 검색")
    void searchMembers_IsActive() {
        MemberSearchRequest requestActive = MemberSearchRequest.builder()
                .isActive(true)
                .build();
        List<Member> activeResults = memberRepository.searchMembers(requestActive);
        assertThat(activeResults).hasSize(1);
        assertThat(activeResults.get(0).getIsActive()).isTrue();

        MemberSearchRequest requestInactive = MemberSearchRequest.builder()
                .isActive(false)
                .build();
        List<Member> inactiveResults = memberRepository.searchMembers(requestInactive);
        assertThat(inactiveResults).hasSize(1);
        assertThat(inactiveResults.get(0).getIsActive()).isFalse();
    }

    @Test
    @DisplayName("가입 기간 조건으로 회원 검색")
    void searchMembers_Days() {
        MemberSearchRequest request = MemberSearchRequest.builder()
                .days(1)
                .build();

        List<Member> results = memberRepository.searchMembers(request);
        assertThat(results).hasSize(2); // Both were just created
    }

    @Test
    @DisplayName("빈 조건으로 회원 검색")
    void searchMembers_EmptyRequest() {
        MemberSearchRequest request = MemberSearchRequest.builder().build();
        List<Member> results = memberRepository.searchMembers(request);
        assertThat(results).hasSize(2);
    }

    @Test
    @DisplayName("잘못된 기간 조건(음수)으로 회원 검색")
    void searchMembers_InvalidDays() {
        MemberSearchRequest request = MemberSearchRequest.builder()
                .days(-1)
                .build();
        List<Member> results = memberRepository.searchMembers(request);
        assertThat(results).hasSize(2); // Should ignore negative days
    }
}
