package com.example.demo.items;

import com.example.demo.common.Money;
import lombok.Getter;

public class VasItem extends Item{

    @Getter
    private Integer quantity;

    private static final int MAX_ITEMS = 3;

    public VasItem(int ID, int sellerID, int categoryID, Money price, Integer quantity) {
        super(ID, sellerID, categoryID, price);
        this.quantity = quantity;
    }

    @Override
    public Money getPrice() {
        return getPriceValue();
    }

    @Override
    public int getMaxQuantity() {
        return MAX_ITEMS;
    }

    public Money getTotalPrice() {
        return getPriceValue().multi(this.quantity);
    }

}
