package com.example.zepto.inventory;

import com.example.zepto.model.Product;

import java.util.List;

/**
 * Abstraction over inventory storage.
 *
 * It exposes only the operations required by InventoryManager.
 */
public interface InventoryStore {

    void addProduct(Product prod, int qty);

    void removeProduct(int sku, int qty);

    int checkStock(int sku);

    List<Product> listAvailableProducts();
}
