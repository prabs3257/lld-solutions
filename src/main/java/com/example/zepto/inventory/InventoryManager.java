package com.example.zepto.inventory;

import com.example.zepto.factory.ProductFactory;
import com.example.zepto.model.Product;

import java.util.List;

/**
 * Coordinates inventory operations and hides the InventoryStore
 * implementation from the DarkStore.
 */
public class InventoryManager {
    private InventoryStore store;

    public InventoryManager(InventoryStore store) {
        this.store = store;
    }

    public void addStock(int sku, int qty) {
        Product prod = ProductFactory.createProduct(sku);
        store.addProduct(prod, qty);
        System.out.println("[InventoryManager] Added SKU " + sku + " Qty " + qty);
    }

    public void removeStock(int sku, int qty) {
        store.removeProduct(sku, qty);
    }

    public int checkStock(int sku) {
        return store.checkStock(sku);
    }

    public List<Product> getAvailableProducts() {
        return store.listAvailableProducts();
    }
}
