package com.example.zepto.strategy;

import com.example.zepto.inventory.InventoryManager;

import java.util.Map;

/**
 * Strategy interface used by DarkStore for inventory replenishment.
 */
public interface ReplenishStrategy {

    void replenish(
            InventoryManager manager,
            Map<Integer, Integer> itemsToReplenish);
}
