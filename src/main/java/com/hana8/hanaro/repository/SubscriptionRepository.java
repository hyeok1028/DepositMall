package com.hana8.hanaro.repository;

import com.hana8.hanaro.common.enums.SubscriptionStatus;
import com.hana8.hanaro.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByMemberId(Long memberId);

    List<Subscription> findByProductId(Long productId);

    List<Subscription> findByStatus(SubscriptionStatus status);

    List<Subscription> findByMemberIdAndStatus(Long memberId, SubscriptionStatus status);

    List<Subscription> findByMemberIdAndProductId(Long memberId, Long productId);
}