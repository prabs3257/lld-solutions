package com.example.splitwise.service;

import com.example.splitwise.model.Settlement;

import java.util.*;

public final class DebtSimplifier {

    public List<Settlement> simplify(Map<String, Map<String, Long>> balances) {
        Map<String, Long> net = new HashMap<>();

        for (var creditorEntry : balances.entrySet()) {
            String creditor = creditorEntry.getKey();
            net.putIfAbsent(creditor, 0L);

            for (var debtEntry : creditorEntry.getValue().entrySet()) {
                String debtor = debtEntry.getKey();
                long amount = debtEntry.getValue();

                if (amount <= 0) continue;

                net.merge(creditor, amount, Long::sum);
                net.merge(debtor, -amount, Long::sum);
            }
        }

        List<Node> creditors = new ArrayList<>();
        List<Node> debtors = new ArrayList<>();

        for (var entry : net.entrySet()) {
            if (entry.getValue() > 0) {
                creditors.add(new Node(entry.getKey(), entry.getValue()));
            } else if (entry.getValue() < 0) {
                debtors.add(new Node(entry.getKey(), -entry.getValue()));
            }
        }

        creditors.sort(Comparator.comparingLong(Node::amount).reversed());
        debtors.sort(Comparator.comparingLong(Node::amount).reversed());

        List<Settlement> settlements = new ArrayList<>();

        int creditorIndex = 0;
        int debtorIndex = 0;

        while (creditorIndex < creditors.size()
                && debtorIndex < debtors.size()) {

            Node creditor = creditors.get(creditorIndex);
            Node debtor = debtors.get(debtorIndex);

            long amount = Math.min(
                    creditor.amount(),
                    debtor.amount()
            );

            settlements.add(new Settlement(
                    debtor.userId(),
                    creditor.userId(),
                    amount
            ));

            creditors.set(
                    creditorIndex,
                    new Node(
                            creditor.userId(),
                            creditor.amount() - amount
                    )
            );

            debtors.set(
                    debtorIndex,
                    new Node(
                            debtor.userId(),
                            debtor.amount() - amount
                    )
            );

            if (creditors.get(creditorIndex).amount() == 0) {
                creditorIndex++;
            }

            if (debtors.get(debtorIndex).amount() == 0) {
                debtorIndex++;
            }
        }

        return settlements;
    }

    private record Node(String userId, long amount) {}
}
