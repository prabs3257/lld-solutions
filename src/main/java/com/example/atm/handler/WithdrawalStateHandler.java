package com.example.atm.handler;

import com.example.atm.input.ATMInput;
import com.example.atm.service.WithdrawalService;
import com.example.atm.workflow.ATMContext;

public class WithdrawalStateHandler implements ATMStateHandler {
    private final ATMInput input;
    private final WithdrawalService withdrawalService;
    public WithdrawalStateHandler(ATMInput input, WithdrawalService withdrawalService) { this.input = input; this.withdrawalService = withdrawalService; }
    @Override public void handle(ATMContext context) { withdrawalService.withdraw(context.getSession().getAccount(), input.readAmount()); }
}
