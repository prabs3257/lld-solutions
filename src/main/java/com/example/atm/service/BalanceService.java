package com.example.atm.service;

import com.example.atm.domain.Account;

import java.math.BigDecimal;

public class BalanceService {
    public BigDecimal getBalance(Account account) { return account.getBalance(); }
}
