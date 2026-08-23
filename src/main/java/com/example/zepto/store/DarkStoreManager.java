package com.example.zepto.store;

import com.example.zepto.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Singleton responsible for registering dark stores and finding stores
 * close to a user.
 */
public class DarkStoreManager {
    private static DarkStoreManager instance;
    private List<DarkStore> darkStores;

    private DarkStoreManager() {
        darkStores = new ArrayList<>();
    }

    public static DarkStoreManager getInstance() {
        if (instance == null) {
            instance = new DarkStoreManager();
        }
        return instance;
    }

    public void registerDarkStore(DarkStore ds) {
        darkStores.add(ds);
    }

    /**
     * Finds stores within maxDistance and returns them nearest-first.
     */
    public List<DarkStore> getNearbyDarkStores(
            double ux,
            double uy,
            double maxDistance) {

        List<Pair<Double, DarkStore>> distList = new ArrayList<>();

        for (DarkStore ds : darkStores) {
            double distance = ds.distanceTo(ux, uy);

            if (distance <= maxDistance) {
                distList.add(new Pair<>(distance, ds));
            }
        }

        distList.sort(Comparator.comparing(Pair::getKey));

        List<DarkStore> result = new ArrayList<>();
        for (Pair<Double, DarkStore> pair : distList) {
            result.add(pair.getValue());
        }

        return result;
    }
}
