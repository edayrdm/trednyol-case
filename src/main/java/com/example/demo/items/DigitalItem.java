package com.example.demo.items;

import com.example.demo.common.Money;

public class DigitalItem extends Item{

    private static final int MAX_ITEMS = 5;

    public DigitalItem(int ID, int sellerID, int categoryID, Money price) {
        super(ID, sellerID, categoryID, price);
    }

    @Override
    public Money getPrice() {
        return getPriceValue();
    }

    @Override
    public int getMaxQuantity() {
        return MAX_ITEMS;
    }

}
