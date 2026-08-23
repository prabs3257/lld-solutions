package com.example.splitwise;

import com.example.splitwise.facade.SplitwiseFacade;
import com.example.splitwise.manager.BalanceManager;
import com.example.splitwise.manager.ExpenseManager;
import com.example.splitwise.manager.GroupManager;
import com.example.splitwise.manager.UserManager;
import com.example.splitwise.model.Group;
import com.example.splitwise.model.Settlement;
import com.example.splitwise.model.User;
import com.example.splitwise.observer.ConsoleNotificationObserver;
import com.example.splitwise.repository.*;
import com.example.splitwise.service.DebtSimplifier;
import com.example.splitwise.split.SplitType;
import com.example.splitwise.util.IdGenerator;

import java.util.List;

public final class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new InMemoryUserRepository();
        GroupRepository groupRepository = new InMemoryGroupRepository();
        ExpenseRepository expenseRepository = new InMemoryExpenseRepository();

        UserManager userManager =
                new UserManager(userRepository, new IdGenerator("u"));

        GroupManager groupManager =
                new GroupManager(
                        groupRepository,
                        userManager,
                        new IdGenerator("g")
                );

        BalanceManager balanceManager =
                new BalanceManager(new DebtSimplifier());

        ExpenseManager expenseManager =
                new ExpenseManager(
                        expenseRepository,
                        groupManager,
                        balanceManager,
                        new IdGenerator("e")
                );

        SplitwiseFacade splitwise =
                new SplitwiseFacade(
                        userManager,
                        groupManager,
                        expenseManager,
                        balanceManager
                );

        splitwise.addExpenseObserver(new ConsoleNotificationObserver());

        User alice = splitwise.createUser("Alice", "alice@example.com");
        User bob = splitwise.createUser("Bob", "bob@example.com");
        User charlie = splitwise.createUser("Charlie", "charlie@example.com");

        Group trip = splitwise.createGroup(
                "Goa Trip",
                List.of(
                        alice.getId(),
                        bob.getId(),
                        charlie.getId()
                )
        );

        // ₹900 equal split => ₹300 each.
        splitwise.addExpense(
                trip.getId(),
                "Dinner",
                90_000,
                alice.getId(),
                List.of(alice.getId(), bob.getId(), charlie.getId()),
                SplitType.EQUAL,
                List.of()
        );

        // ₹500 exact split:
        // Alice ₹200, Bob ₹100, Charlie ₹200.
        splitwise.addExpense(
                trip.getId(),
                "Taxi",
                50_000,
                bob.getId(),
                List.of(alice.getId(), bob.getId(), charlie.getId()),
                SplitType.EXACT,
                List.of(20_000L, 10_000L, 20_000L)
        );

        System.out.println("\n--- Current balances ---");
        printBalances(splitwise, trip, alice, bob, charlie);

        System.out.println("\n--- Simplified settlements ---");
        for (Settlement settlement :
                splitwise.getSimplifiedSettlements(trip.getId())) {

            System.out.printf(
                    "%s pays %s ₹%.2f%n",
                    settlement.fromUserId(),
                    settlement.toUserId(),
                    settlement.amountInCents() / 100.0
            );
        }
    }

    private static void printBalances(
            SplitwiseFacade splitwise,
            Group group,
            User alice,
            User bob,
            User charlie
    ) {
        System.out.println("Alice   : " + splitwise.getBalances(group.getId(), alice.getId()));
        System.out.println("Bob     : " + splitwise.getBalances(group.getId(), bob.getId()));
        System.out.println("Charlie : " + splitwise.getBalances(group.getId(), charlie.getId()));
    }
}
