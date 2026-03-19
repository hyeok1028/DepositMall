package com.hana8.hanaro.mapper;

import com.hana8.hanaro.dto.subscription.SubscriptionResponse;
import com.hana8.hanaro.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(target = "memberId", source = "member.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "withdrawAccountId", source = "withdrawAccount.id")
    @Mapping(target = "subscriptionAccountId", source = "subscriptionAccount.id")
    SubscriptionResponse toResponse(Subscription subscription);

    @Mapping(target = "member", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "withdrawAccount", ignore = true)
    @Mapping(target = "subscriptionAccount", ignore = true)
    Subscription toEntity(SubscriptionResponse dto);

    List<SubscriptionResponse> toResponseList(List<Subscription> subscriptions);
}
