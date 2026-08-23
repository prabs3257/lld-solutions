package com.example.zepto.strategy;

import com.example.zepto.inventory.InventoryManager;

import java.util.Map;

/**
 * Alternate replenishment strategy present in the original repository.
 *
 * The repository only prints that weekly replenishment was triggered.
 */
public class WeeklyReplenishStrategy implements ReplenishStrategy {

    public WeeklyReplenishStrategy() {
    }

    @Override
    public void replenish(
            InventoryManager manager,
            Map<Integer, Integer> itemsToReplenish) {

        System.out.println(
                "[WeeklyReplenish] Weekly replenishment triggered for inventory.");
    }
}
