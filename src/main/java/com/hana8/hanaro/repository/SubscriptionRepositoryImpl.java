package com.hana8.hanaro.repository;

import com.hana8.hanaro.common.enums.SubscriptionStatus;
import com.hana8.hanaro.dto.subscription.SubscriptionSearchRequest;
import com.hana8.hanaro.entity.QSubscription;
import com.hana8.hanaro.entity.Subscription;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SubscriptionRepositoryImpl implements SubscriptionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Subscription> searchSubscriptions(SubscriptionSearchRequest request) {
        QSubscription subscription = QSubscription.subscription;

        return queryFactory
                .selectFrom(subscription)
                .where(
                        memberIdEq(request.getMemberId()),
                        productIdEq(request.getProductId()),
                        statusEq(request.getStatus())
                )
                .fetch();
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? QSubscription.subscription.member.id.eq(memberId) : null;
    }

    private BooleanExpression productIdEq(Long productId) {
        return productId != null ? QSubscription.subscription.product.id.eq(productId) : null;
    }

    private BooleanExpression statusEq(SubscriptionStatus status) {
        return status != null ? QSubscription.subscription.status.eq(status) : null;
    }
}
