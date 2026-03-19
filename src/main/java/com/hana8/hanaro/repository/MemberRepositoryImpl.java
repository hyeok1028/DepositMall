package com.hana8.hanaro.repository;

import com.hana8.hanaro.dto.member.MemberSearchRequest;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.entity.QMember;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> searchMembers(MemberSearchRequest request) {
        QMember member = QMember.member;

        return queryFactory
                .selectFrom(member)
                .where(
                        nicknameContains(request.getNickname()),
                        emailContains(request.getEmail()),
                        isActiveEq(request.getIsActive()),
                        joinedWithinDays(request.getDays())
                )
                .fetch();
    }

    private BooleanExpression nicknameContains(String nickname) {
        return StringUtils.hasText(nickname) ? QMember.member.nickname.contains(nickname) : null;
    }

    private BooleanExpression emailContains(String email) {
        return StringUtils.hasText(email) ? QMember.member.email.contains(email) : null;
    }

    private BooleanExpression isActiveEq(Boolean isActive) {
        return isActive != null ? QMember.member.isActive.eq(isActive) : null;
    }

    private BooleanExpression joinedWithinDays(Integer days) {
        if (days == null || days < 0) {
            return null;
        }
        LocalDateTime limitDate = LocalDateTime.now().minusDays(days);
        return QMember.member.createdAt.goe(limitDate);
    }
}
