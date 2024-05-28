package com.example.demo.promotion;

import com.example.demo.cart.Cart;
import com.example.demo.common.Money;
import lombok.Getter;

public abstract class Promotion {

    @Getter
    private final int ID;

    public Promotion(int id) {
        ID = id;
    }

    public abstract Money applyPromotion(Cart cart);
}
