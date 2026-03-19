package com.hana8.hanaro.repository;

import com.hana8.hanaro.common.enums.AccountType;
import com.hana8.hanaro.common.enums.MemberRole;
import com.hana8.hanaro.entity.Account;
import com.hana8.hanaro.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 저장 및 이메일 조회 테스트")
    void saveAndFindByEmail() {
        Member member = Member.builder()
                .email("test@test.com")
                .password("password")
                .nickname("tester")
                .roles(Set.of(MemberRole.ROLE_USER))
                .build();

        memberRepository.save(member);

        Optional<Member> found = memberRepository.findByEmail("test@test.com");
        assertThat(found).isPresent();
        assertThat(found.get().getNickname()).isEqualTo("tester");
    }
}
