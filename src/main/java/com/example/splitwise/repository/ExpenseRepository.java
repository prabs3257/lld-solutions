package com.example.splitwise.repository;

import com.example.splitwise.model.Expense;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository {
    Expense save(Expense expense);
    Optional<Expense> findById(String expenseId);
    List<Expense> findByGroupId(String groupId);
}
