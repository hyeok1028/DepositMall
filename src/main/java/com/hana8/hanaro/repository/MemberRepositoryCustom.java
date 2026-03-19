package com.hana8.hanaro.repository;

import com.hana8.hanaro.dto.member.MemberSearchRequest;
import com.hana8.hanaro.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> searchMembers(MemberSearchRequest request);
}
