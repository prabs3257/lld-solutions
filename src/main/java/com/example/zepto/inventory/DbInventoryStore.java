package com.example.zepto.inventory;

import com.example.zepto.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory implementation of InventoryStore from the repository.
 *
 * stock:
 *   SKU -> available quantity
 *
 * products:
 *   SKU -> Product metadata
 */
public class DbInventoryStore implements InventoryStore {
    private Map<Integer, Integer> stock;
    private Map<Integer, Product> products;

    public DbInventoryStore() {
        stock = new HashMap<>();
        products = new HashMap<>();
    }

    @Override
    public void addProduct(Product prod, int qty) {
        int sku = prod.getSku();

        if (!products.containsKey(sku)) {
            products.put(sku, prod);
        }
        // Otherwise, the extra Product instance is ignored.

        stock.put(sku, stock.getOrDefault(sku, 0) + qty);
    }

    @Override
    public void removeProduct(int sku, int qty) {
        if (!stock.containsKey(sku)) {
            return;
        }

        int currentQuantity = stock.get(sku);
        int remainingQuantity = currentQuantity - qty;

        if (remainingQuantity > 0) {
            stock.put(sku, remainingQuantity);
        } else {
            stock.remove(sku);
            products.remove(sku);
        }
    }

    @Override
    public int checkStock(int sku) {
        return stock.getOrDefault(sku, 0);
    }

    @Override
    public List<Product> listAvailableProducts() {
        List<Product> available = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : stock.entrySet()) {
            int sku = entry.getKey();
            int qty = entry.getValue();

            if (qty > 0 && products.containsKey(sku)) {
                available.add(products.get(sku));
            }
        }

        return available;
    }
}
