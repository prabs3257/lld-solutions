package com.example.atm.handler;

import com.example.atm.input.ATMInput;
import com.example.atm.workflow.ATMContext;

public class IdleStateHandler implements ATMStateHandler {
    private final ATMInput input;
    public IdleStateHandler(ATMInput input) { this.input = input; }
    @Override public void handle(ATMContext context) { context.getSession().setCard(input.readCard()); }
}
