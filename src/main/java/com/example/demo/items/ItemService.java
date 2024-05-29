package com.example.demo.items;

import com.example.demo.common.Money;

public class ItemService {

    public Item createItem(int itemId, int categoryId, int sellerId, double price, int quantity) {
        // Business logic to create different types of items based on categoryId
        if (categoryId == 7889) {
            return new DigitalItem(itemId, sellerId, categoryId, new Money(price));
        } else {
            return new DefaultItem(itemId, sellerId,categoryId, new Money(price));
        }
    }

    public VasItem createVasItem(int itemId, int categoryId, int sellerId, double price, int quantity) {
            return new VasItem(itemId, sellerId, categoryId, new Money(price),quantity );
    }
}
