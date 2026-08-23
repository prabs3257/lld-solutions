package com.example.splitwise.observer;

import com.example.splitwise.model.Expense;

public final class ConsoleNotificationObserver implements ExpenseObserver {
    @Override
    public void onExpenseAdded(Expense expense) {
        System.out.printf(
                "[NOTIFICATION] Expense '%s' of ₹%.2f was added by %s%n",
                expense.getDescription(),
                expense.getAmountInCents() / 100.0,
                expense.getPaidByUserId()
        );
    }
}
