package com.example.atm.service;

import com.example.atm.domain.ATMInventory;
import com.example.atm.domain.Account;
import com.example.atm.exception.InvalidAmountException;

import java.math.BigDecimal;

public class WithdrawalService {
    private final ATMInventory inventory;

    public WithdrawalService(ATMInventory inventory) { this.inventory = inventory; }

    public void withdraw(Account account, int amount) {
        if (amount <= 0) throw new InvalidAmountException("Withdrawal amount must be positive");
        BigDecimal withdrawalAmount = BigDecimal.valueOf(amount);
        if (account.getBalance().compareTo(withdrawalAmount) < 0) {
            throw new com.example.atm.exception.InsufficientBalanceException();
        }
        if (!inventory.canDispense(amount)) throw new com.example.atm.exception.InsufficientCashException();
        account.withdraw(withdrawalAmount);
        inventory.dispense(amount);
        System.out.println("Please collect ₹" + amount);
    }
}
