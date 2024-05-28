package com.example.demo.items;

import com.example.demo.common.Money;

import java.util.ArrayList;
import java.util.List;

public class DefaultItem extends Item {

    private static final int MAX_ITEMS = 10;

    public DefaultItem(int ID, int sellerID, int categoryID, Money price) {
        super(ID, sellerID, categoryID, price);
    }

    @Override
    public Money getPrice() {

        //need to add vas item's price to total price
        return getPriceValue();
    }

    @Override
    public int getMaxQuantity() {
        return MAX_ITEMS;
    }
}
