package com.example.zepto.app;

import com.example.zepto.helper.ZeptoHelper;
import com.example.zepto.model.Cart;
import com.example.zepto.model.User;
import com.example.zepto.order.OrderManager;

/**
 * Demo entry point from the original Zepto clone LLD.
 */
public class ZeptoClone {

    public static void main(String[] args) {
        // 1) Initialize stores and inventory.
        ZeptoHelper.initialize();

        // 2) A user comes onto the platform.
        User user = new User("Aditya", 1.0, 1.0);
        System.out.println(
                "\nUser with name " + user.name + " comes on platform");

        // 3) Show all products available within 5 KM.
        ZeptoHelper.showAllItems(user);

        // 4) User adds products to the cart.
        System.out.println("\nAdding items to cart");
        Cart cart = user.getCart();
        cart.addItem(101, 4);
        cart.addItem(102, 3);
        cart.addItem(103, 2);

        // 5) Place the order.
        OrderManager.getInstance().placeOrder(user, cart);

        System.out.println("\n=== Demo Complete ===");
    }
}
