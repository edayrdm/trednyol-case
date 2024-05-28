package com.example.demo.promotion;

import com.example.demo.cart.Cart;
import com.example.demo.cart.CartItem;
import com.example.demo.common.Money;
import com.example.demo.common.PromotionDetails;
import com.example.demo.items.VasItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class PromotionService {

    private final List<Promotion> promotions = Arrays.asList(
            new SameSellerPromotion(9909, 0.10),
            new CategoryPromotion(5676,3003, 0.05),
            new TotalPricePromotion(1232)
    );

    public PromotionDetails applyBestPromotion(Cart cart){

        int promotionId = 0;
        Money discount = new Money(0);

        //TODO comment
        for (Promotion promotion : promotions) {
            Money currentDiscount = promotion.applyPromotion(cart);
            if (currentDiscount.isGreaterThan(discount)){
                discount = currentDiscount;
                promotionId = promotion.getID();
            }
        }

        return new PromotionDetails(discount, promotionId);
    }
}
