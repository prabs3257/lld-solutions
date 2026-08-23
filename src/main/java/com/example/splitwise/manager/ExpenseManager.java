package com.example.splitwise.manager;

import com.example.splitwise.model.Expense;
import com.example.splitwise.model.Split;
import com.example.splitwise.observer.ExpenseObserver;
import com.example.splitwise.repository.ExpenseRepository;
import com.example.splitwise.split.SplitStrategy;
import com.example.splitwise.split.SplitStrategyFactory;
import com.example.splitwise.split.SplitType;
import com.example.splitwise.util.IdGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class ExpenseManager {
    private final ExpenseRepository expenseRepository;
    private final GroupManager groupManager;
    private final BalanceManager balanceManager;
    private final IdGenerator idGenerator;
    private final List<ExpenseObserver> observers = new ArrayList<>();

    public ExpenseManager(
            ExpenseRepository expenseRepository,
            GroupManager groupManager,
            BalanceManager balanceManager,
            IdGenerator idGenerator
    ) {
        this.expenseRepository = expenseRepository;
        this.groupManager = groupManager;
        this.balanceManager = balanceManager;
        this.idGenerator = idGenerator;
    }

    public Expense addExpense(
            String groupId,
            String description,
            long amountInCents,
            String paidByUserId,
            List<String> participantIds,
            SplitType splitType,
            List<Long> values
    ) {
        groupManager.validateMember(groupId, paidByUserId);
        validateParticipants(groupId, participantIds);

        SplitStrategy strategy = SplitStrategyFactory.getStrategy(splitType);
        List<Split> splits = strategy.calculate(
                amountInCents,
                participantIds,
                values == null ? List.of() : values
        );

        Expense expense = new Expense(
                idGenerator.nextId(),
                description,
                amountInCents,
                paidByUserId,
                splits,
                groupId
        );

        /*
         * BalanceManager updates the ledger under the group's lock.
         * The repository write is done immediately after the balance update.
         * In a production DB-backed implementation, these would be one
         * database transaction.
         */
        balanceManager.updateBalances(expense);
        expenseRepository.save(expense);

        observers.forEach(observer -> observer.onExpenseAdded(expense));

        return expense;
    }

    public Expense getExpense(String expenseId) {
        return expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found: " + expenseId));
    }

    public List<Expense> getGroupExpenses(String groupId) {
        groupManager.getGroup(groupId);
        return expenseRepository.findByGroupId(groupId);
    }

    public void addObserver(ExpenseObserver observer) {
        if (observer == null) throw new IllegalArgumentException("Observer is required");
        observers.add(observer);
    }

    private void validateParticipants(String groupId, List<String> participantIds) {
        if (participantIds == null || participantIds.isEmpty()) {
            throw new IllegalArgumentException("Participants are required");
        }

        if (new HashSet<>(participantIds).size() != participantIds.size()) {
            throw new IllegalArgumentException("Duplicate participant");
        }

        participantIds.forEach(userId -> groupManager.validateMember(groupId, userId));
    }
}
