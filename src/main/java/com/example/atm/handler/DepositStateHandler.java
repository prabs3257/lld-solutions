package com.example.atm.handler;

import com.example.atm.input.ATMInput;
import com.example.atm.service.DepositService;
import com.example.atm.workflow.ATMContext;

public class DepositStateHandler implements ATMStateHandler {
    private final ATMInput input;
    private final DepositService depositService;
    public DepositStateHandler(ATMInput input, DepositService depositService) { this.input = input; this.depositService = depositService; }
    @Override public void handle(ATMContext context) { depositService.deposit(context.getSession().getAccount(), input.readAmount()); }
}
