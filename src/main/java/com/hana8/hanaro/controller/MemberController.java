package com.hana8.hanaro.controller;

import com.hana8.hanaro.dto.member.MemberCreateRequest;
import com.hana8.hanaro.dto.member.MemberResponse;
import com.hana8.hanaro.dto.member.MemberSearchRequest;
import com.hana8.hanaro.dto.member.MemberUpdateRequest;
import com.hana8.hanaro.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

    @GetMapping({"", "/"})
    public List<MemberResponse> getMembers() {
        return service.getMembers();
    }

    @GetMapping("/search")
    public List<MemberResponse> searchMembers(MemberSearchRequest searchDTO) {
        return service.searchMembers(searchDTO);
    }

    @GetMapping("/{id}")
    public MemberResponse getMember(@PathVariable Long id) {
        return service.getMember(id);
    }

    @PostMapping({"", "/"})
    public MemberResponse registMember(@Valid @RequestBody MemberCreateRequest member) {
        return service.registMember(member);
    }

    @PutMapping("/{id}")
    public MemberResponse editMember(@PathVariable Long id,
                                     @Valid @RequestBody MemberUpdateRequest member) {
        member.setId(id);
        return service.editMember(member);
    }

    @DeleteMapping("/{id}")
    public int withdrawMember(@PathVariable Long id) {
        return service.withdrawMember(id);
    }
}
