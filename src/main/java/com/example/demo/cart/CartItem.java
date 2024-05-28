package com.example.demo.cart;

import com.example.demo.common.Money;
import com.example.demo.common.ResponseEntity;
import com.example.demo.items.DefaultItem;
import com.example.demo.items.Item;
import com.example.demo.items.VasItem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class CartItem {

    @Getter
    private int quantity;
    @Getter
    private Item item;
    @Getter
    private final List<VasItem> vasItemList = new ArrayList<>();

    public CartItem(Item item, Integer quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public void addQuantitiy(Integer add){
        this.quantity += add;
    }

    public void subQuantitiy(Integer add){
        this.quantity -= add;
    }

    public int totalVasItemCount() {
        return vasItemList.stream().mapToInt(VasItem::getQuantity).sum();
    }

    public Money getTotalPrice(){

        Money totalPrice = item.getPrice().multi(this.quantity);

        Money totalVasItemList = vasItemList.stream().map(VasItem::getTotalPrice).reduce(new Money(0),Money::add);

        return totalPrice.add(totalVasItemList);
    }

    public ResponseEntity addVasItemToList(VasItem vasItem){

        if ( totalVasItemCount() + vasItem.getQuantity() > 3) {
            return new ResponseEntity(false, "Cannot add more than 3 VasItems to the list");
        }

        if (vasItem.getPrice().isGreaterThan(item.getPrice())) {
            return new ResponseEntity(false, "VasItem price cannot be higher than DefaultItem price");
        }

        vasItemList.add(vasItem);
        return new ResponseEntity(true, "VasItem added successfully.");
    }
}
