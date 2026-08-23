package com.example.zepto.helper;

import com.example.zepto.model.Product;
import com.example.zepto.model.User;
import com.example.zepto.store.DarkStore;
import com.example.zepto.store.DarkStoreManager;
import com.example.zepto.strategy.ThresholdReplenishStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper used by the demo to initialize dark stores and display products
 * available near a user.
 */
public class ZeptoHelper {

    /**
     * Displays unique products available from any dark store within 5 KM.
     */
    public static void showAllItems(User user) {
        System.out.println(
                "\n[Zepto] All Available products within 5 KM for "
                        + user.name
                        + ":");

        DarkStoreManager dsManager = DarkStoreManager.getInstance();
        List<DarkStore> nearbyStores =
                dsManager.getNearbyDarkStores(user.x, user.y, 5.0);

        Map<Integer, Double> skuToPrice = new HashMap<>();
        Map<Integer, String> skuToName = new HashMap<>();

        for (DarkStore ds : nearbyStores) {
            for (Product product : ds.getAllProducts()) {
                int sku = product.getSku();

                // A product is displayed once even if multiple nearby stores
                // stock the same SKU.
                if (!skuToPrice.containsKey(sku)) {
                    skuToPrice.put(sku, product.getPrice());
                    skuToName.put(sku, product.getName());
                }
            }
        }

        for (Map.Entry<Integer, Double> entry : skuToPrice.entrySet()) {
            System.out.println(
                    "  SKU "
                            + entry.getKey()
                            + " - "
                            + skuToName.get(entry.getKey())
                            + " @ ₹"
                            + entry.getValue());
        }
    }

    /**
     * Creates the same three dark stores and stock quantities used by the
     * original repository's demo.
     */
    public static void initialize() {
        DarkStoreManager dsManager = DarkStoreManager.getInstance();

        // DarkStore A
        DarkStore darkStoreA = new DarkStore("DarkStoreA", 0.0, 0.0);
        darkStoreA.setReplenishStrategy(
                new ThresholdReplenishStrategy(3));

        System.out.println("\nAdding stocks in DarkStoreA....");
        darkStoreA.addStock(101, 5);
        darkStoreA.addStock(102, 2);

        // DarkStore B
        DarkStore darkStoreB = new DarkStore("DarkStoreB", 4.0, 1.0);
        darkStoreB.setReplenishStrategy(
                new ThresholdReplenishStrategy(3));

        System.out.println("\nAdding stocks in DarkStoreB....");
        darkStoreB.addStock(101, 3);
        darkStoreB.addStock(103, 10);

        // DarkStore C
        DarkStore darkStoreC = new DarkStore("DarkStoreC", 2.0, 3.0);
        darkStoreC.setReplenishStrategy(
                new ThresholdReplenishStrategy(3));

        System.out.println("\nAdding stocks in DarkStoreC....");
        darkStoreC.addStock(102, 5);
        darkStoreC.addStock(201, 7);

        dsManager.registerDarkStore(darkStoreA);
        dsManager.registerDarkStore(darkStoreB);
        dsManager.registerDarkStore(darkStoreC);
    }
}
