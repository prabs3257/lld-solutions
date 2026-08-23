package com.example.zepto.strategy;

import com.example.zepto.inventory.InventoryManager;

import java.util.Map;

/**
 * Replenishes an SKU when its current quantity is below a configured
 * threshold.
 */
public class ThresholdReplenishStrategy implements ReplenishStrategy {
    private int threshold;

    public ThresholdReplenishStrategy(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void replenish(
            InventoryManager manager,
            Map<Integer, Integer> itemsToReplenish) {

        System.out.println("[ThresholdReplenish] Checking threshold...");

        for (Map.Entry<Integer, Integer> entry : itemsToReplenish.entrySet()) {
            int sku = entry.getKey();
            int qtyToAdd = entry.getValue();
            int current = manager.checkStock(sku);

            if (current < threshold) {
                manager.addStock(sku, qtyToAdd);
                System.out.println(
                        "  -> SKU " + sku
                                + " was " + current
                                + ", replenished by " + qtyToAdd);
            }
        }
    }
}
