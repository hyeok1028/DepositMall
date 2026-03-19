package com.hana8.hanaro.service;

import com.hana8.hanaro.common.enums.MemberRole;
import com.hana8.hanaro.dto.member.MemberCreateRequest;
import com.hana8.hanaro.dto.member.MemberResponse;
import com.hana8.hanaro.dto.member.MemberSearchRequest;
import com.hana8.hanaro.dto.member.MemberUpdateRequest;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.mapper.MemberMapper;
import com.hana8.hanaro.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;
    private final MemberMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public List<MemberResponse> getMembers() {
        return mapper.toResponseList(repository.findAll());
    }

    public List<MemberResponse> searchMembers(MemberSearchRequest searchDTO) {
        List<Member> members;

        if (searchDTO.getNickname() != null && !searchDTO.getNickname().isBlank()
                && searchDTO.getIsActive() != null) {
            members = repository.findByNicknameContainingAndIsActive(
                    searchDTO.getNickname(),
                    searchDTO.getIsActive()
            );
        } else if (searchDTO.getEmail() != null && !searchDTO.getEmail().isBlank()
                && searchDTO.getIsActive() != null) {
            members = repository.findByEmailContainingAndIsActive(
                    searchDTO.getEmail(),
                    searchDTO.getIsActive()
            );
        } else if (searchDTO.getNickname() != null && !searchDTO.getNickname().isBlank()) {
            members = repository.findByNicknameContaining(searchDTO.getNickname());
        } else if (searchDTO.getEmail() != null && !searchDTO.getEmail().isBlank()) {
            members = repository.findByEmailContaining(searchDTO.getEmail());
        } else if (searchDTO.getIsActive() != null) {
            members = repository.findByIsActive(searchDTO.getIsActive());
        } else {
            members = repository.findAll();
        }

        return mapper.toResponseList(members);
    }

    public MemberResponse getMember(Long id) {
        Member member = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member #%d is not found!".formatted(id)));

        return mapper.toResponse(member);
    }

    public MemberResponse registMember(MemberCreateRequest request) {
        Member member = mapper.toEntity(request);
        member.update(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname(),
                request.getIsActive() != null ? request.getIsActive() : true,
                request.getRoles() != null && !request.getRoles().isEmpty()
                        ? request.getRoles()
                        : Set.of(MemberRole.ROLE_USER)
        );

        Member savedMember = repository.save(member);
        return mapper.toResponse(savedMember);
    }

    public MemberResponse editMember(MemberUpdateRequest request) {
        Member oldMember = repository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Member #%d is not found!".formatted(request.getId())));

        String encodedPassword = request.getPassword() != null && !request.getPassword().isBlank()
                ? passwordEncoder.encode(request.getPassword())
                : oldMember.getPassword();

        oldMember.update(
                request.getEmail() != null ? request.getEmail() : oldMember.getEmail(),
                encodedPassword,
                request.getNickname() != null ? request.getNickname() : oldMember.getNickname(),
                request.getIsActive() != null ? request.getIsActive() : oldMember.getIsActive(),
                request.getRoles() != null && !request.getRoles().isEmpty()
                        ? request.getRoles()
                        : oldMember.getRoles()
        );

        return mapper.toResponse(repository.save(oldMember));
    }

    public int withdrawMember(Long id) {
        Member member = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member #%d is not found!".formatted(id)));

        repository.delete(member);
        return 1;
    }
}
