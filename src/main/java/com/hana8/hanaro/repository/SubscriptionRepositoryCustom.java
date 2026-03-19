package com.hana8.hanaro.repository;

import com.hana8.hanaro.dto.subscription.SubscriptionSearchRequest;
import com.hana8.hanaro.entity.Subscription;

import java.util.List;

public interface SubscriptionRepositoryCustom {
    List<Subscription> searchSubscriptions(SubscriptionSearchRequest request);
}
