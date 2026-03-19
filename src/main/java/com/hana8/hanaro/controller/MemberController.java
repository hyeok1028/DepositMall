package com.hana8.hanaro.controller;

import com.hana8.hanaro.common.response.ApiResponse;
import com.hana8.hanaro.dto.member.MemberCreateRequest;
import com.hana8.hanaro.dto.member.MemberResponse;
import com.hana8.hanaro.dto.member.MemberSearchRequest;
import com.hana8.hanaro.dto.member.MemberUpdateRequest;
import com.hana8.hanaro.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "User - Member", description = "사용자용 회원 관리 API")
public class MemberController {

    private final MemberService service;

    @GetMapping({"", "/"})
    @Operation(summary = "전체 회원 목록 조회", description = "시스템에 등록된 전체 회원의 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getMembers() {
        List<MemberResponse> response = service.getMembers();
        return ResponseEntity.ok(ApiResponse.success(response, "Member list retrieved"));
    }

    @GetMapping("/search")
    @Operation(summary = "회원 검색", description = "이메일, 닉네임, 활성 상태 등을 기준으로 회원을 검색합니다.")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> searchMembers(@ParameterObject MemberSearchRequest searchDTO) {
        List<MemberResponse> response = service.searchMembers(searchDTO);
        return ResponseEntity.ok(ApiResponse.success(response, "Member search results retrieved"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "회원 상세 조회", description = "특정 회원의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<MemberResponse>> getMember(@PathVariable Long id) {
        MemberResponse response = service.getMember(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Member details retrieved"));
    }

    @PostMapping({"", "/"})
    @Operation(summary = "회원 등록", description = "새로운 회원을 등록합니다. 성공 시 success: true를 반환합니다.")
    public ResponseEntity<ApiResponse<MemberResponse>> registMember(@Valid @RequestBody MemberCreateRequest member) {
        MemberResponse response = service.registMember(member);
        return ResponseEntity.ok(ApiResponse.success(response, "Member registered successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다. 성공 시 success: true를 반환합니다.")
    public ResponseEntity<ApiResponse<MemberResponse>> editMember(@PathVariable Long id,
                                     @Valid @RequestBody MemberUpdateRequest member) {
        member.setId(id);
        MemberResponse response = service.editMember(member);
        return ResponseEntity.ok(ApiResponse.success(response, "Member info updated"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "회원 탈퇴", description = "회원을 시스템에서 삭제합니다. 성공 시 success: true를 반환합니다.")
    public ResponseEntity<ApiResponse<Integer>> withdrawMember(@PathVariable Long id) {
        int result = service.withdrawMember(id);
        return ResponseEntity.ok(ApiResponse.success(result, "Member withdrawn successfully"));
    }
}
