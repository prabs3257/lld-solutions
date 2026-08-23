package com.example.atm.handler;

import com.example.atm.service.BalanceService;
import com.example.atm.workflow.ATMContext;

public class CheckBalanceStateHandler implements ATMStateHandler {
    private final BalanceService balanceService;
    public CheckBalanceStateHandler(BalanceService balanceService) { this.balanceService = balanceService; }
    @Override public void handle(ATMContext context) { System.out.println("Current balance: ₹" + balanceService.getBalance(context.getSession().getAccount())); }
}
