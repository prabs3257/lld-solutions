package com.example.zepto.order;

import com.example.zepto.factory.ProductFactory;
import com.example.zepto.model.*;
import com.example.zepto.store.DarkStore;
import com.example.zepto.store.DarkStoreManager;
import com.example.zepto.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton responsible for placing orders.
 *
 * The flow is kept exactly as in the repository:
 * 1. Find dark stores within 5 KM, sorted by distance.
 * 2. Try to fulfill the complete order from the nearest store.
 * 3. Otherwise split quantities across nearby stores.
 * 4. Assign delivery partners.
 * 5. Print and store the resulting Order.
 */
public class OrderManager {
    private static OrderManager instance;
    private List<Order> orders;

    private OrderManager() {
        orders = new ArrayList<>();
    }

    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public void placeOrder(User user, Cart cart) {
        System.out.println("\n[OrderManager] Placing Order for: " + user.name);

        List<Pair<Product, Integer>> requestedItems = cart.getItems();

        // 1) Find nearby dark stores within 5 KM.
        double maxDist = 5.0;
        List<DarkStore> nearbyDarkStores =
                DarkStoreManager
                        .getInstance()
                        .getNearbyDarkStores(user.x, user.y, maxDist);

        if (nearbyDarkStores.isEmpty()) {
            System.out.println(
                    "  No dark stores within 5 KM. Cannot fulfill order.");
            return;
        }

        // 2) Check whether the closest store can supply the complete cart.
        DarkStore firstStore = nearbyDarkStores.get(0);
        boolean allInFirst = true;

        for (Pair<Product, Integer> item : requestedItems) {
            int sku = item.getKey().getSku();
            int qty = item.getValue();

            if (firstStore.checkStock(sku) < qty) {
                allInFirst = false;
                break;
            }
        }

        Order order = new Order(user);

        // If everything is in the nearest store, use only that store.
        if (allInFirst) {
            System.out.println("  All items at: " + firstStore.getName());

            for (Pair<Product, Integer> item : requestedItems) {
                int sku = item.getKey().getSku();
                int qty = item.getValue();

                firstStore.removeStock(sku, qty);
                order.items.add(new Pair<>(item.getKey(), qty));
            }

            order.totalAmount = cart.getTotal();
            order.partners.add(new DeliveryPartner("Partner1"));
            System.out.println("  Assigned Delivery Partner: Partner1");
        }

        // Otherwise, split the order across nearby stores.
        else {
            System.out.println("  Splitting order across stores...");

            // Remaining quantity required for each SKU.
            Map<Integer, Integer> allItems = new HashMap<>();
            for (Pair<Product, Integer> item : requestedItems) {
                allItems.put(item.getKey().getSku(), item.getValue());
            }

            int partnerId = 1;

            for (DarkStore store : nearbyDarkStores) {
                if (allItems.isEmpty()) {
                    break;
                }

                System.out.println("   Checking: " + store.getName());

                List<Integer> toErase = new ArrayList<>();

                for (Map.Entry<Integer, Integer> entry : allItems.entrySet()) {
                    int sku = entry.getKey();
                    int qtyNeeded = entry.getValue();
                    int availableQty = store.checkStock(sku);

                    if (availableQty <= 0) {
                        continue;
                    }

                    int takenQty = Math.min(availableQty, qtyNeeded);
                    store.removeStock(sku, takenQty);

                    System.out.println(
                            "     "
                                    + store.getName()
                                    + " supplies SKU "
                                    + sku
                                    + " x"
                                    + takenQty);

                    order.items.add(
                            new Pair<>(
                                    ProductFactory.createProduct(sku),
                                    takenQty));

                    if (qtyNeeded > takenQty) {
                        allItems.put(sku, qtyNeeded - takenQty);
                    } else {
                        toErase.add(sku);
                    }
                }

                for (int sku : toErase) {
                    allItems.remove(sku);
                }

                if (!toErase.isEmpty()) {
                    String pname = "Partner" + partnerId++;
                    order.partners.add(new DeliveryPartner(pname));
                    System.out.println(
                            "     Assigned: "
                                    + pname
                                    + " for "
                                    + store.getName());
                }
            }

            // The repository prints any quantities that could not be fulfilled.
            if (!allItems.isEmpty()) {
                System.out.println("  Could not fulfill:");

                for (Map.Entry<Integer, Integer> entry : allItems.entrySet()) {
                    System.out.println(
                            "    SKU "
                                    + entry.getKey()
                                    + " x"
                                    + entry.getValue());
                }
            }

            // For split orders, total is calculated using only fulfilled items.
            double sum = 0;
            for (Pair<Product, Integer> item : order.items) {
                sum += item.getKey().getPrice() * item.getValue();
            }
            order.totalAmount = sum;
        }

        // Print the order summary.
        System.out.println(
                "\n[OrderManager] Order #" + order.orderId + " Summary:");
        System.out.println("  User: " + user.name + "\n  Items:");

        for (Pair<Product, Integer> item : order.items) {
            System.out.println(
                    "    SKU "
                            + item.getKey().getSku()
                            + " ("
                            + item.getKey().getName()
                            + ") x"
                            + item.getValue()
                            + " @ ₹"
                            + item.getKey().getPrice());
        }

        System.out.println(
                "  Total: ₹" + order.totalAmount + "\n  Partners:");

        for (DeliveryPartner dp : order.partners) {
            System.out.println("    " + dp.name);
        }

        System.out.println();
        orders.add(order);
    }

    public List<Order> getAllOrders() {
        return orders;
    }
}
