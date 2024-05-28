package com.example.demo.promotion;

import com.example.demo.cart.Cart;
import com.example.demo.common.Money;

import java.util.Objects;

public class CategoryPromotion extends Promotion {

    private final double discountRate;
    private final int categoryId;

    public CategoryPromotion(int ID, int categoryId, double discountRate) {
        super(ID);
        this.categoryId = categoryId;
        this.discountRate = discountRate;
    }

    @Override
    public Money applyPromotion(Cart cart) {

         Money discount = cart.getCartItemList().stream()
                 .filter(item -> Objects.equals(item.getItem().getCategoryID(), categoryId))
                 .map(item -> item.getItem().getPrice())
                 .map(price -> price.multi(discountRate))
                 .reduce(new Money(0), Money::add);

        return discount;
    }
}
