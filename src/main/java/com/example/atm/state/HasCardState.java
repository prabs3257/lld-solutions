package com.example.atm.state;

import com.example.atm.workflow.ATMContext;

public class HasCardState implements ATMState {
    @Override
    public ATMState next(ATMContext context) {
        if (!context.getSession().isAuthenticated()) return new IdleState();
        return switch (context.getSession().getSelectedOperation()) {
            case WITHDRAW -> new WithdrawalState();
            case DEPOSIT -> new DepositState();
            case CHECK_BALANCE -> new CheckBalanceState();
        };
    }

    @Override public String getStateName() { return "HAS_CARD"; }
}
