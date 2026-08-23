package com.example.zepto.model;

/**
 * Represents a product that can be stored in a dark store and added to a cart.
 *
 * The original repository identifies a product using an SKU and stores its
 * display name and price in the object.
 */
public class Product {
    private int sku;
    private String name;
    private double price;

    public Product(int id, String nm, double pr) {
        sku = id;
        name = nm;
        price = pr;
    }

    public int getSku() {
        return this.sku;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }
}
