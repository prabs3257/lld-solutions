package com.example.atm.state;

import com.example.atm.workflow.ATMContext;

public class WithdrawalState implements ATMState {
    @Override public ATMState next(ATMContext context) { context.getSession().clear(); return new IdleState(); }
    @Override public String getStateName() { return "WITHDRAWAL"; }
}
