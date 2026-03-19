package com.hana8.hanaro.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterestResponse {
    private Long principal;
    private Double rate;
    private Long expectedInterest;
    private Long maturityAmount;
}
