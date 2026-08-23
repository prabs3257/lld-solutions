package com.example.atm.service;

import com.example.atm.domain.Account;
import com.example.atm.domain.Card;

import java.util.Map;

public class AccountService {
    private final Map<String, Account> accounts;

    public AccountService(Map<String, Account> accounts) { this.accounts = accounts; }

    public Account getAccount(Card card) {
        Account account = accounts.get(card.getAccountNumber());
        if (account == null) throw new IllegalArgumentException("Account not found");
        return account;
    }
}
