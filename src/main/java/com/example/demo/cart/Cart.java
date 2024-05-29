package com.example.demo.cart;

import com.example.demo.common.Money;
import com.example.demo.common.ResponseEntity;
import com.example.demo.items.Item;
import com.example.demo.items.VasItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Cart {

    private final List<CartItem> cartItemList = new ArrayList<>();

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public int totalUniqueCartItems() {
        return cartItemList.size();
    }

    public int totalItemCount() {
        return cartItemList.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public Optional<CartItem> checkItemIndex (Item item) {

        Optional<CartItem> cartItem =  cartItemList.stream()
                            .filter( cItem -> cItem.getItem().equals(item) )
                            .findFirst();

        return cartItem;
    }

    public Money getTotalPrice(){
        return cartItemList.stream().map(CartItem::getTotalPrice).reduce(new Money(0),Money::add);
    }

    public void addItemToList(Item item, int quantity) {
        cartItemList.add(new CartItem(item, quantity));
    }

    public ResponseEntity addVasItemToItem (int itemId, VasItem vasItem){

        Optional<CartItem> matchingObject = cartItemList.stream().
                filter(cartItem -> cartItem.getItem().getID() == itemId).
                findFirst();

        if(matchingObject.isEmpty()){
            return new ResponseEntity(false, "Given item id is not found");
        }

        CartItem cartItem = matchingObject.get();

        if (cartItem.getItem().getCategoryID() != 1001 && cartItem.getItem().getCategoryID() != 3004){
            return new ResponseEntity(false, "You cannot add vas item to this category");
        }

        return cartItem.addVasItemToList(vasItem);
    }

    public ResponseEntity removeItem(int itemId) {

        Optional<CartItem> matchingObject = cartItemList.stream().
                filter(cartItem -> cartItem.getItem().getID() == itemId).
                findFirst();

        if(matchingObject.isEmpty()){
            return new ResponseEntity(false, "Given item id is not found");
        }

        //todo remove
        cartItemList.removeIf(cartItem -> cartItem.equals(matchingObject.get()));
        return new ResponseEntity(true, "Item removed successfully.");
    }

    public void resetItems() {
        cartItemList.clear();
    }
}
