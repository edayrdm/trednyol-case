package com.example.demo.cart;
import com.example.demo.common.Money;
import com.example.demo.common.PromotionDetails;
import com.example.demo.common.ResponseEntity;
import com.example.demo.items.Item;
import com.example.demo.items.VasItem;
import com.example.demo.promotion.PromotionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Cart {

    private final List<CartItem> cartItemList = new ArrayList<>();

    private static final int MAX_UNIQUE_ITEMS = 10;
    private static final int MAX_TOTAL_ITEMS = 30;
    private static final Money MAX_TOTAL_AMOUNT = new Money(500000.0);

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public int totalUniqueCartItems() {
        return cartItemList.size();
    }

    public int totalItemCount() {
        return cartItemList.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public int checkItemIndex (Item item) {

        Optional<CartItem> cartItem =  cartItemList.stream()
                            .filter( cItem -> cItem.getItem().equals(item) )
                            .findFirst();

        return cartItem.map(cartItemList::indexOf).orElse(-1);
    }

    public Money getTotalPrice(){
        return cartItemList.stream().map(CartItem::getTotalPrice).reduce(new Money(0),Money::add);
    }

    public PromotionDetails getTotalDiscount() {
        PromotionService promotionService = new PromotionService();
        return promotionService.applyBestPromotion(this);
    }

    public ResponseEntity addItemToList(Item item, int quantity) {

        // check max allowed quantity
        if (quantity > item.getMaxQuantity()){
            return new ResponseEntity(false, "Exceeds maximum quantity for item");
        }
        //The maximum quantity of an item that can be added is 10
        if ( checkItemIndex(item)==-1 && totalUniqueCartItems() >= MAX_UNIQUE_ITEMS ) {
            return new ResponseEntity(false, "Exceeds maximum unique items in cart");
        }
        //The total number of products cannot exceed 30.
        if (totalItemCount() + quantity > MAX_TOTAL_ITEMS) {
            return new ResponseEntity(false, "Exceeds maximum total items in cart");
        }

        //first add item to list
        int indexOfMatchingItem = checkItemIndex(item);
        if ( indexOfMatchingItem == -1)
            cartItemList.add(new CartItem(item, quantity));
        else {
            cartItemList.get(indexOfMatchingItem).addQuantitiy(quantity);
        }

        //then check the price
        //The total amount (including vas items) of the Cart cannot exceed 500,000 TL.
        var totalPrice = getTotalPrice();
        var discountAmount = getTotalDiscount().getDiscountAmount();
        if (totalPrice.sub(discountAmount).isGreaterThan(MAX_TOTAL_AMOUNT)) {
            //todo remove

            if( cartItemList.get(indexOfMatchingItem).getQuantity() == quantity)
                cartItemList.removeIf(cartItem -> cartItem.getItem().equals(item));
            else
                cartItemList.get(indexOfMatchingItem).subQuantitiy(quantity);

            return new ResponseEntity(false, "Exceeds maximum total amount in cart");
        }

        return new ResponseEntity(true, "Item added successfully.");
    }

    public ResponseEntity addVasItemToItem (int itemId, VasItem vasItem){

        if (vasItem.getSellerID() != 5003) {
            return new ResponseEntity(false, "Seller ID is not defined for vas items");
        }
        if (vasItem.getCategoryID() != 3242) {
            return new ResponseEntity(false, "This ID is not defined for category");
        }

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
