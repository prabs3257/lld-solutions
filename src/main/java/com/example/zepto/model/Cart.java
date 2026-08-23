package com.example.zepto.model;

import com.example.zepto.factory.ProductFactory;
import com.example.zepto.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * User cart.
 *
 * Every entry stores a Product together with the quantity requested.
 */
public class Cart {
    public List<Pair<Product, Integer>> items = new ArrayList<>();

    public void addItem(int sku, int qty) {
        Product prod = ProductFactory.createProduct(sku);
        items.add(new Pair<>(prod, qty));

        System.out.println(
                "[Cart] Added SKU "
                        + sku
                        + " (" + prod.getName() + ") x"
                        + qty);
    }

    public double getTotal() {
        double sum = 0.0;

        for (Pair<Product, Integer> item : items) {
            sum += item.getKey().getPrice() * item.getValue();
        }

        return sum;
    }

    public List<Pair<Product, Integer>> getItems() {
        return items;
    }
}
