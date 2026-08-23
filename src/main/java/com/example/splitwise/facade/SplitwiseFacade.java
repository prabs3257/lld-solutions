package com.example.splitwise.facade;

import com.example.splitwise.manager.BalanceManager;
import com.example.splitwise.manager.ExpenseManager;
import com.example.splitwise.manager.GroupManager;
import com.example.splitwise.manager.UserManager;
import com.example.splitwise.model.Expense;
import com.example.splitwise.model.Group;
import com.example.splitwise.model.Settlement;
import com.example.splitwise.model.User;
import com.example.splitwise.split.SplitType;

import java.util.List;
import java.util.Map;

/**
 * Optional facade representing the public API of the application.
 * It keeps Main/client code independent from the internal managers.
 */
public final class SplitwiseFacade {
    private final UserManager userManager;
    private final GroupManager groupManager;
    private final ExpenseManager expenseManager;
    private final BalanceManager balanceManager;

    public SplitwiseFacade(
            UserManager userManager,
            GroupManager groupManager,
            ExpenseManager expenseManager,
            BalanceManager balanceManager
    ) {
        this.userManager = userManager;
        this.groupManager = groupManager;
        this.expenseManager = expenseManager;
        this.balanceManager = balanceManager;
    }

    public User createUser(String name, String email) {
        return userManager.createUser(name, email);
    }

    public Group createGroup(String name, List<String> memberIds) {
        Group group = groupManager.createGroup(name, memberIds);
        balanceManager.initializeGroup(group.getId());
        return group;
    }

    public void addMember(String groupId, String userId) {
        groupManager.addMember(groupId, userId);
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
        return expenseManager.addExpense(
                groupId,
                description,
                amountInCents,
                paidByUserId,
                participantIds,
                splitType,
                values
        );
    }

    public void settle(
            String groupId,
            String debtorUserId,
            String creditorUserId,
            long amountInCents
    ) {
        groupManager.validateMember(groupId, debtorUserId);
        groupManager.validateMember(groupId, creditorUserId);

        balanceManager.settle(
                groupId,
                debtorUserId,
                creditorUserId,
                amountInCents
        );
    }

    public Map<String, Long> getBalances(String groupId, String userId) {
        groupManager.validateMember(groupId, userId);
        return balanceManager.getUserBalances(groupId, userId);
    }

    public List<Settlement> getSimplifiedSettlements(String groupId) {
        groupManager.getGroup(groupId);
        return balanceManager.getSimplifiedSettlements(groupId);
    }

    public List<Expense> getExpenses(String groupId) {
        return expenseManager.getGroupExpenses(groupId);
    }

    public void addExpenseObserver(com.example.splitwise.observer.ExpenseObserver observer) {
        expenseManager.addObserver(observer);
    }
}
