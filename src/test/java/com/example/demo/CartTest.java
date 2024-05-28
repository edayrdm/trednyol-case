package com.example.demo;

import com.example.demo.cart.Cart;
import com.example.demo.common.Money;
import com.example.demo.common.PromotionDetails;
import com.example.demo.items.DefaultItem;
import com.example.demo.items.DigitalItem;
import com.example.demo.items.VasItem;
import com.example.demo.promotion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartTest {

    private Cart cart;

    @BeforeEach
    public void setUp() {
        cart = new Cart();
    }

    @Test
    public void testAddDefaultItem() {
        DefaultItem item = new DefaultItem(1, 1000, 1001, new Money(100));
        cart.addItemToList(item, 2);
        assertEquals(2, cart.totalItemCount());
    }

    @Test
    public void testAddSameDefaultItem() {
        DefaultItem item = new DefaultItem(1, 1000, 1001, new Money(100));
        DefaultItem item2 = new DefaultItem(1, 1000, 1001, new Money(100));

        cart.addItemToList(item, 3);
        cart.addItemToList(item2, 1);
        assertEquals(400, cart.getTotalPrice().value());
    }

    @Test
    public void testAddDigitalItem() {
        DigitalItem item = new DigitalItem(2, 1000,7889, new Money(100));
        cart.addItemToList(item, 5);
        assertEquals(5, cart.totalItemCount());
    }

    @Test
    public void testAddDigitalItemAmount() {
        DigitalItem item = new DigitalItem(2, 1000,7889, new Money(100));
        cart.addItemToList(item, 5);
        assertEquals(500, cart.getTotalPrice().value());
    }

    @Test
    public void testAddVasItem() {
        DefaultItem item = new DefaultItem(3, 1000, 1001, new Money(100));
        cart.addItemToList(item, 3);

        VasItem vasItem = new VasItem(3, 5003, 3242, new Money(100), 3 );
        cart.addVasItemToItem(3, vasItem);

        assertEquals(3, cart.totalItemCount());
        assertEquals(600, cart.getTotalPrice().value());
    }

    @Test
    public void testApplySameSellerPromotion() {
        DefaultItem item1 = new DefaultItem(5, 3004, 1004, new Money(50) );
        DefaultItem item2 = new DefaultItem(6, 3004, 1004, new Money(200));

        cart.addItemToList(item1, 5);
        cart.addItemToList(item2, 1);

        Promotion promotion = new SameSellerPromotion(9909, 0.10);
        Money discountAmount = promotion.applyPromotion(cart);

        assertEquals(new Money(45).value(), discountAmount.value());
    }

    @Test
    public void testApplyCategoryPromotion() {
        DefaultItem item1 = new DefaultItem(5, 3004, 3003, new Money(200) );
        DefaultItem item2 = new DefaultItem(3, 3000, 3003, new Money(200) );
        cart.addItemToList(item1, 1);
        cart.addItemToList(item2, 1);

        Promotion promotion = new CategoryPromotion(5676,3003, 0.05);
        Money money= promotion.applyPromotion(cart);
        assertEquals(new Money(20).value(), money.value());
    }

    @Test
    public void testApplyTotalPricePromotion() {

        DefaultItem item = new DefaultItem(5, 3004, 3003, new Money(100) );
        cart.addItemToList(item, 5);

        Promotion promotion = new TotalPricePromotion(1232);
        Money money= promotion.applyPromotion(cart);

        assertEquals(new Money(250).value(), money.value());
    }

    @Test
    public void testApplyBestPromotion() {
        DefaultItem item1 = new DefaultItem(5, 3004, 3003, new Money(1000) );
        DefaultItem item2 = new DefaultItem(4, 3004, 3003, new Money(200) );
        cart.addItemToList(item1, 3);
        cart.addItemToList(item2, 1);

        new PromotionService().applyBestPromotion(cart);
        assertEquals(new Money(320).value(), cart.getTotalDiscount().getDiscountAmount().value());
    }

}
