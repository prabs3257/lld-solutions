package com.example.splitwise.repository;

import com.example.splitwise.model.Expense;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class InMemoryExpenseRepository implements ExpenseRepository {
    private final ConcurrentMap<String, Expense> expenses = new ConcurrentHashMap<>();

    @Override
    public Expense save(Expense expense) {
        if (expenses.putIfAbsent(expense.getId(), expense) != null) {
            throw new IllegalArgumentException("Expense already exists: " + expense.getId());
        }
        return expense;
    }

    @Override
    public Optional<Expense> findById(String expenseId) {
        return Optional.ofNullable(expenses.get(expenseId));
    }

    @Override
    public List<Expense> findByGroupId(String groupId) {
        return expenses.values().stream()
                .filter(expense -> groupId.equals(expense.getGroupId()))
                .toList();
    }
}
