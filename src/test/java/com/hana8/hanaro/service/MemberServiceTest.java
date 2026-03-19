package com.hana8.hanaro.service;

import com.hana8.hanaro.common.enums.MemberRole;
import com.hana8.hanaro.dto.member.MemberCreateRequest;
import com.hana8.hanaro.dto.member.MemberResponse;
import com.hana8.hanaro.dto.member.MemberSearchRequest;
import com.hana8.hanaro.dto.member.MemberUpdateRequest;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.mapper.MemberMapper;
import com.hana8.hanaro.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository repository;
    @Mock
    private MemberMapper mapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService service;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .id(1L)
                .email("test@test.com")
                .nickname("tester")
                .isActive(true)
                .roles(Set.of(MemberRole.ROLE_USER))
                .build();
    }

    @Test
    @DisplayName("전체 회원 조회 테스트")
    void getMembers() {
        given(repository.findAll()).willReturn(List.of(testMember));
        given(mapper.toResponseList(anyList())).willReturn(List.of(mock(MemberResponse.class)));

        List<MemberResponse> result = service.getMembers();

        assertThat(result).hasSize(1);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("회원 검색 테스트")
    void searchMembers() {
        MemberSearchRequest request = new MemberSearchRequest();
        given(repository.searchMembers(request)).willReturn(List.of(testMember));
        given(mapper.toResponseList(anyList())).willReturn(List.of(mock(MemberResponse.class)));

        List<MemberResponse> result = service.searchMembers(request);

        assertThat(result).hasSize(1);
        verify(repository, times(1)).searchMembers(request);
    }

    @Test
    @DisplayName("회원 상세 조회 - 성공")
    void getMember_Success() {
        given(repository.findById(1L)).willReturn(Optional.of(testMember));
        given(mapper.toResponse(testMember)).willReturn(mock(MemberResponse.class));

        MemberResponse result = service.getMember(1L);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("회원 상세 조회 - 실패 (존재하지 않음)")
    void getMember_NotFound() {
        given(repository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.getMember(99L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("회원 등록 테스트")
    void registMember() {
        MemberCreateRequest request = new MemberCreateRequest();
        request.setEmail("new@test.com");
        request.setPassword("1234");
        request.setNickname("newbie");

        given(mapper.toEntity(request)).willReturn(Member.builder().build());
        given(passwordEncoder.encode("1234")).willReturn("encoded");
        given(repository.save(any(Member.class))).willReturn(testMember);
        given(mapper.toResponse(any(Member.class))).willReturn(mock(MemberResponse.class));

        MemberResponse result = service.registMember(request);

        assertThat(result).isNotNull();
        verify(repository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원 수정 테스트")
    void editMember() {
        MemberUpdateRequest request = new MemberUpdateRequest();
        request.setId(1L);
        request.setNickname("updated");

        given(repository.findById(1L)).willReturn(Optional.of(testMember));
        given(repository.save(any(Member.class))).willReturn(testMember);
        given(mapper.toResponse(any(Member.class))).willReturn(mock(MemberResponse.class));

        MemberResponse result = service.editMember(request);

        assertThat(result).isNotNull();
        verify(repository, times(1)).save(testMember);
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void withdrawMember() {
        given(repository.findById(1L)).willReturn(Optional.of(testMember));

        int result = service.withdrawMember(1L);

        assertThat(result).isEqualTo(1);
        verify(repository, times(1)).delete(testMember);
    }
}
