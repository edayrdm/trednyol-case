package com.example.demo.items;

import com.example.demo.common.Money;
import lombok.Getter;

public abstract class Item {

    @Getter
    private final int ID;
    @Getter
    private final int sellerID;
    @Getter
    private final int categoryID;
    private final Money price;

    public Item(int ID, int sellerID, int categoryID, Money price) {
        this.ID = ID;
        this.sellerID = sellerID;
        this.categoryID = categoryID;
        this.price = price;
    }

    public Money getPriceValue() {
        return price;
    }

    public abstract Money getPrice();

    public abstract int getMaxQuantity();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return ID == item.ID;
    }

}
