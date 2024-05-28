package com.example.demo.promotion;
import com.example.demo.cart.Cart;
import com.example.demo.common.Money;
import lombok.Getter;

public class SameSellerPromotion extends Promotion {

    private final double discountRate;

    public SameSellerPromotion(int ID, double discountRate) {
        super(ID);
        this.discountRate = discountRate;
    }

    @Override
    public Money applyPromotion(Cart cart) {

        int distinctSellers = (int) cart.getCartItemList().stream().map(
                item -> item.getItem().getSellerID()
        ).distinct().count();

        if (distinctSellers == 1) {
            return cart.getTotalPrice().multi(discountRate);
        }
        return new Money(0);
    }
}
