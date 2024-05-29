package com.example.demo.cart;

import com.example.demo.common.Money;
import com.example.demo.common.PromotionDetails;
import com.example.demo.common.ResponseEntity;
import com.example.demo.items.Item;
import com.example.demo.items.ItemService;
import com.example.demo.items.VasItem;
import com.example.demo.promotion.PromotionService;

import java.util.List;
import java.util.Optional;


public class CartService {

    private final Cart cart;
    private final ItemService itemService;

    private static final int VAS_ITEM_SELLER_ID = 5003;
    private static final int VAS_ITEM_CATEGORY_ID = 3242;

    private static final int MAX_UNIQUE_ITEMS = 10;
    private static final int MAX_TOTAL_ITEMS = 30;
    private static final Money MAX_TOTAL_AMOUNT = new Money(500000.0);

    public CartService(ItemService itemService) {
        this.cart = new Cart();
        this.itemService = itemService;
    }

    public ResponseEntity addItem(int itemId, int categoryId, int sellerId, double price, int quantity) {
        Item item = itemService.createItem(itemId, categoryId, sellerId, price, quantity);

        // check max allowed quantity
        if (quantity > item.getMaxQuantity()){
            return new ResponseEntity(false, "Exceeds maximum quantity for item");
        }
        //The maximum quantity of an item that can be added is 10
        if ( cart.checkItemIndex(item).isEmpty() && cart.totalUniqueCartItems() >= MAX_UNIQUE_ITEMS ) {
            return new ResponseEntity(false, "Exceeds maximum unique items in cart");
        }
        //The total number of products cannot exceed 30.
        if ( cart.totalItemCount() + quantity > MAX_TOTAL_ITEMS) {
            return new ResponseEntity(false, "Exceeds maximum total items in cart");
        }

        //first add item to list
        Optional<CartItem> optionalCartItem = cart.checkItemIndex(item);
        if ( optionalCartItem.isEmpty())
            cart.addItemToList(item, quantity );
        else {
            optionalCartItem.get().addQuantity(quantity);
        }


        //then check the price
        //The total amount (including vas items) of the Cart cannot exceed 500,000 TL.
        var totalPrice = cart.getTotalPrice();
        var discountAmount = getTotalDiscount().getDiscountAmount();
        if (totalPrice.sub(discountAmount).isGreaterThan(MAX_TOTAL_AMOUNT)) {

            //todo remove
            //if exceeds then remove
            if( optionalCartItem.isEmpty())
                cart.removeItem(item.getID());
            else
                optionalCartItem.get().subQuantity(quantity);

            return new ResponseEntity(false, "Exceeds maximum total amount in cart");
        }
        return new ResponseEntity(true, "Item added successfully.");

    }

    public ResponseEntity addVasItemToItem(int itemId, int vasItemId, int vasCategoryId, int vasSellerId, double price, int quantity) {

        if (vasSellerId != VAS_ITEM_SELLER_ID ) {
            return new ResponseEntity(false, "Seller ID is not defined for vas items");
        }
        if (vasCategoryId != VAS_ITEM_CATEGORY_ID) {
            return new ResponseEntity(false, "This ID is not defined for category");
        }

        VasItem vasItem = itemService.createVasItem(vasItemId, vasCategoryId, vasSellerId, price, quantity);
        return cart.addVasItemToItem(itemId, vasItem);
    }

    public ResponseEntity removeItem(int itemId) {
        return cart.removeItem(itemId);
    }

    public void resetCart() {
        cart.resetItems();
    }

    public CartDisplay displayCart() {

        List<CartItem> items = cart.getCartItemList();
        PromotionDetails promotionDetails = getTotalDiscount();

        Money totalPrice = cart.getTotalPrice();
        Money totalAmount = totalPrice.sub(promotionDetails.getDiscountAmount());
        Money totalDiscount = promotionDetails.getDiscountAmount();

        int appliedPromotionId = promotionDetails.getPromotionId();

        return new CartDisplay(items, totalAmount, appliedPromotionId, totalDiscount);
    }

    public PromotionDetails getTotalDiscount() {
        PromotionService promotionService = new PromotionService();
        return promotionService.applyBestPromotion(cart);
    }


}
