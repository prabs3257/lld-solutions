package com.example.atm.state;

import com.example.atm.workflow.ATMContext;

public class IdleState implements ATMState {
    @Override public ATMState next(ATMContext context) { return new HasCardState(); }
    @Override public String getStateName() { return "IDLE"; }
}
