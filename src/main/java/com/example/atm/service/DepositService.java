package com.example.atm.service;

import com.example.atm.domain.Account;
import com.example.atm.exception.InvalidAmountException;

import java.math.BigDecimal;

public class DepositService {
    public void deposit(Account account, int amount) {
        if (amount <= 0) throw new InvalidAmountException("Deposit amount must be positive");
        account.deposit(BigDecimal.valueOf(amount));
        System.out.println("₹" + amount + " deposited successfully");
    }
}
