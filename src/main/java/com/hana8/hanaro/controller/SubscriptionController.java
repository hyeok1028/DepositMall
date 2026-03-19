package com.hana8.hanaro.controller;

import com.hana8.hanaro.dto.subscription.CancelSubscriptionRequest;
import com.hana8.hanaro.dto.subscription.InterestResponse;
import com.hana8.hanaro.dto.subscription.SubscriptionCreateRequest;
import com.hana8.hanaro.dto.subscription.SubscriptionResponse;
import com.hana8.hanaro.dto.subscription.SubscriptionSearchRequest;
import com.hana8.hanaro.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService service;

    @GetMapping({"", "/"})
    public List<SubscriptionResponse> getSubscriptions(SubscriptionSearchRequest dto) {
        return service.getSubscriptions(dto);
    }

    @GetMapping("/{id}")
    public SubscriptionResponse getSubscription(@PathVariable Long id) {
        return service.getSubscription(id);
    }

    @PostMapping({"", "/"})
    public SubscriptionResponse registSubscription(@Valid @RequestBody SubscriptionCreateRequest dto) {
        return service.registSubscription(dto);
    }

    @PostMapping("/{id}/cancel")
    public SubscriptionResponse cancelSubscription(@PathVariable Long id,
                                                  @RequestBody CancelSubscriptionRequest dto) {
        return service.cancelSubscription(id, dto);
    }

    @GetMapping("/{id}/interest")
    public InterestResponse getInterest(@PathVariable Long id) {
        return service.getInterest(id);
    }
}
