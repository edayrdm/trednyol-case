package com.example.demo.promotion;

import com.example.demo.cart.Cart;
import com.example.demo.common.Money;

public class TotalPricePromotion extends Promotion{

    public TotalPricePromotion(int ID) {
        super(ID);
    }

    @Override
    public Money applyPromotion(Cart cart) {

        Money totalPrice = cart.getTotalPrice();

        Money discount = new Money(0);

        if (totalPrice.isEqualOrGreaterThan(new Money(50000))) {
            discount = new Money(2000);
        } else if (totalPrice.isEqualOrGreaterThan(new Money(10000))) {
            discount = new Money(1000);
        } else if (totalPrice.isEqualOrGreaterThan(new Money(5000))) {
            discount = new Money(500);
        } else if (totalPrice.isEqualOrGreaterThan(new Money(500))) {
            discount = new Money(250);
        }
        return discount;
    }
}
