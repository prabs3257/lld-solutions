package com.example.splitwise.observer;

import com.example.splitwise.model.Expense;

public interface ExpenseObserver {
    void onExpenseAdded(Expense expense);
}
