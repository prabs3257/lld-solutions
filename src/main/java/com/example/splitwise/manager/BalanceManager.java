package com.example.splitwise.manager;

import com.example.splitwise.model.Expense;
import com.example.splitwise.model.Settlement;
import com.example.splitwise.model.Split;
import com.example.splitwise.service.BalanceSheet;
import com.example.splitwise.service.DebtSimplifier;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

public final class BalanceManager {
    private final ConcurrentMap<String, BalanceSheet> ledgers = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();
    private final DebtSimplifier debtSimplifier;

    public BalanceManager(DebtSimplifier debtSimplifier) {
        this.debtSimplifier = debtSimplifier;
    }

    public void initializeGroup(String groupId) {
        ledgers.putIfAbsent(groupId, new BalanceSheet());
        locks.putIfAbsent(groupId, new ReentrantLock());
    }

    public void updateBalances(Expense expense) {
        ReentrantLock lock = lockFor(expense.getGroupId());
        lock.lock();

        try {
            BalanceSheet ledger = ledgerFor(expense.getGroupId());

            for (Split split : expense.getSplits()) {
                if (!split.userId().equals(expense.getPaidByUserId())
                        && split.amountInCents() > 0) {
                    ledger.addDebt(
                            expense.getPaidByUserId(),
                            split.userId(),
                            split.amountInCents()
                    );
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void settle(
            String groupId,
            String debtorUserId,
            String creditorUserId,
            long amountInCents
    ) {
        ReentrantLock lock = lockFor(groupId);
        lock.lock();

        try {
            ledgerFor(groupId).settle(
                    debtorUserId,
                    creditorUserId,
                    amountInCents
            );
        } finally {
            lock.unlock();
        }
    }

    public Map<String, Long> getUserBalances(String groupId, String userId) {
        return ledgerFor(groupId).getUserBalances(userId);
    }

    public List<Settlement> getSimplifiedSettlements(String groupId) {
        return debtSimplifier.simplify(ledgerFor(groupId).snapshot());
    }

    private BalanceSheet ledgerFor(String groupId) {
        BalanceSheet ledger = ledgers.get(groupId);
        if (ledger == null) {
            throw new IllegalArgumentException("No balance ledger for group: " + groupId);
        }
        return ledger;
    }

    private ReentrantLock lockFor(String groupId) {
        return locks.computeIfAbsent(groupId, ignored -> new ReentrantLock());
    }
}
