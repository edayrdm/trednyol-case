package com.example.demo.cart;

import com.example.demo.common.Money;
import lombok.Getter;

import java.util.List;

@Getter
public class CartDisplay {

    private final List<CartItem> items;
    private final Money totalAmount;
    private final int appliedPromotionId;
    private final Money totalDiscount;

    public CartDisplay(List<CartItem> items, Money totalAmount, int appliedPromotionId, Money totalDiscount) {
        this.items = items;
        this.totalAmount = totalAmount;
        this.appliedPromotionId = appliedPromotionId;
        this.totalDiscount = totalDiscount;
    }

}
