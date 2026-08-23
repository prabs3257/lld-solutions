package com.example.atm.domain;

import com.example.atm.exception.InsufficientCashException;

import java.util.HashMap;
import java.util.Map;

public class ATMInventory {
    private final Map<Integer, Integer> cashInventory = new HashMap<>();
    private static final int[] DENOMINATIONS = {2000, 500, 200, 100};

    public ATMInventory() {
        cashInventory.put(2000, 10);
        cashInventory.put(500, 20);
        cashInventory.put(200, 20);
        cashInventory.put(100, 20);
    }

    public boolean canDispense(int amount) {
        Map<Integer, Integer> temp = new HashMap<>(cashInventory);
        for (int denomination : DENOMINATIONS) {
            int available = temp.getOrDefault(denomination, 0);
            int required = Math.min(amount / denomination, available);
            amount -= required * denomination;
        }
        return amount == 0;
    }

    public void dispense(int amount) {
        if (!canDispense(amount)) {
            throw new InsufficientCashException();
        }
        for (int denomination : DENOMINATIONS) {
            int available = cashInventory.getOrDefault(denomination, 0);
            int required = Math.min(amount / denomination, available);
            if (required > 0) {
                cashInventory.put(denomination, available - required);
                amount -= required * denomination;
            }
        }
        if (amount != 0) {
            throw new InsufficientCashException();
        }
    }

    public void addCash(int denomination, int count) {
        cashInventory.merge(denomination, count, Integer::sum);
    }
}
