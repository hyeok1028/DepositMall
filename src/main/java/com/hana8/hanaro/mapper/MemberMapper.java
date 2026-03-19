package com.hana8.hanaro.mapper;

import com.hana8.hanaro.dto.member.MemberCreateRequest;
import com.hana8.hanaro.dto.member.MemberResponse;
import com.hana8.hanaro.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberResponse toResponse(Member member);

    @Mapping(target = "accounts", ignore = true)
    Member toEntity(MemberCreateRequest dto);

    List<MemberResponse> toResponseList(List<Member> members);
}
