package com.example.zepto.model;

/**
 * Represents a user of the application.
 *
 * The x/y coordinates are used to locate nearby dark stores and every
 * user owns a Cart.
 */
public class User {
    public String name;
    public double x;
    public double y;
    private Cart cart;

    public User(String n, double x_coord, double y_coord) {
        name = n;
        x = x_coord;
        y = y_coord;
        cart = new Cart();
    }

    public Cart getCart() {
        return cart;
    }
}
