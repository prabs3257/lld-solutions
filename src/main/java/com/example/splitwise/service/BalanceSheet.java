package com.example.splitwise.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores normalized pairwise debts:
 *
 * balances[creditor][debtor] = amount
 *
 * Example:
 * balances["alice"]["bob"] = 5000
 * means Bob owes Alice ₹50.00.
 */
public final class BalanceSheet {
    private final Map<String, Map<String, Long>> balances = new HashMap<>();

    public synchronized void addDebt(String creditor, String debtor, long amountInCents) {
        if (creditor.equals(debtor) || amountInCents == 0) {
            return;
        }
        if (amountInCents < 0) {
            throw new IllegalArgumentException("Debt amount cannot be negative");
        }

        long reverseDebt = balances
                .getOrDefault(debtor, Map.of())
                .getOrDefault(creditor, 0L);

        if (reverseDebt > 0) {
            if (reverseDebt > amountInCents) {
                setDebt(debtor, creditor, reverseDebt - amountInCents);
            } else if (reverseDebt < amountInCents) {
                removeDebt(debtor, creditor);
                setDebt(creditor, debtor, amountInCents - reverseDebt);
            } else {
                removeDebt(debtor, creditor);
            }
            return;
        }

        long existingDebt = balances
                .getOrDefault(creditor, Map.of())
                .getOrDefault(debtor, 0L);

        setDebt(creditor, debtor, Math.addExact(existingDebt, amountInCents));
    }

    public synchronized void settle(
            String debtor,
            String creditor,
            long amountInCents
    ) {
        if (amountInCents <= 0) {
            throw new IllegalArgumentException("Settlement must be positive");
        }

        long outstanding = balances
                .getOrDefault(creditor, Map.of())
                .getOrDefault(debtor, 0L);

        if (outstanding < amountInCents) {
            throw new IllegalArgumentException("Settlement exceeds outstanding debt");
        }

        if (outstanding == amountInCents) {
            removeDebt(creditor, debtor);
        } else {
            setDebt(creditor, debtor, outstanding - amountInCents);
        }
    }

    /**
     * Positive value means the user should receive money.
     * Negative value means the user owes money.
     */
    public synchronized Map<String, Long> getUserBalances(String userId) {
        Map<String, Long> result = new HashMap<>();

        for (var entry : balances.entrySet()) {
            long amount = entry.getValue().getOrDefault(userId, 0L);
            if (amount > 0) {
                result.put(entry.getKey(), -amount);
            }
        }

        for (var entry : balances.getOrDefault(userId, Map.of()).entrySet()) {
            if (entry.getValue() > 0) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }

    public synchronized Map<String, Map<String, Long>> snapshot() {
        Map<String, Map<String, Long>> copy = new HashMap<>();

        for (var entry : balances.entrySet()) {
            copy.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }

        return copy;
    }

    private void setDebt(String creditor, String debtor, long amountInCents) {
        balances
                .computeIfAbsent(creditor, ignored -> new HashMap<>())
                .put(debtor, amountInCents);
    }

    private void removeDebt(String creditor, String debtor) {
        Map<String, Long> creditorDebts = balances.get(creditor);
        if (creditorDebts != null) {
            creditorDebts.remove(debtor);
            if (creditorDebts.isEmpty()) {
                balances.remove(creditor);
            }
        }
    }
}
