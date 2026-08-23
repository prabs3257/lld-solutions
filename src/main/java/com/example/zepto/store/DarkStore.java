package com.example.zepto.store;

import com.example.zepto.inventory.DbInventoryStore;
import com.example.zepto.inventory.InventoryManager;
import com.example.zepto.model.Product;
import com.example.zepto.strategy.ReplenishStrategy;

import java.util.List;
import java.util.Map;

/**
 * Represents a Zepto dark store.
 *
 * Each store has:
 * - a location,
 * - its own InventoryManager,
 * - a configurable replenishment strategy.
 */
public class DarkStore {
    private String name;
    private double x;
    private double y;
    private InventoryManager inventoryManager;
    private ReplenishStrategy replenishStrategy;

    public DarkStore(String n, double x_coord, double y_coord) {
        name = n;
        x = x_coord;
        y = y_coord;

        // The original repository directly creates DbInventoryStore here.
        // It notes that an InventoryStoreFactory could be used for looser coupling.
        inventoryManager = new InventoryManager(new DbInventoryStore());
    }

    /**
     * Computes Euclidean distance between the store and the user's location.
     */
    public double distanceTo(double ux, double uy) {
        return Math.sqrt((x - ux) * (x - ux) + (y - uy) * (y - uy));
    }

    public void runReplenishment(Map<Integer, Integer> itemsToReplenish) {
        if (replenishStrategy != null) {
            replenishStrategy.replenish(inventoryManager, itemsToReplenish);
        }
    }

    // Delegation methods: DarkStore forwards inventory operations to
    // InventoryManager instead of exposing the store directly.

    public List<Product> getAllProducts() {
        return inventoryManager.getAvailableProducts();
    }

    public int checkStock(int sku) {
        return inventoryManager.checkStock(sku);
    }

    public void removeStock(int sku, int qty) {
        inventoryManager.removeStock(sku, qty);
    }

    public void addStock(int sku, int qty) {
        inventoryManager.addStock(sku, qty);
    }

    public void setReplenishStrategy(ReplenishStrategy strategy) {
        this.replenishStrategy = strategy;
    }

    public String getName() {
        return this.name;
    }

    public double getXCoordinate() {
        return this.x;
    }

    public double getYCoordinate() {
        return this.y;
    }

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }
}
