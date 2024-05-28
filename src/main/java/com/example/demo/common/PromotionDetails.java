package com.example.demo.common;

import lombok.Getter;

public class PromotionDetails {

    @Getter
    private final Money discountAmount;
    @Getter
    private final int promotionId;

    public PromotionDetails(Money discountAmount, int promotionId) {
        this.discountAmount = discountAmount;
        this.promotionId = promotionId;
    }
}
