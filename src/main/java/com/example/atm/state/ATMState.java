package com.example.atm.state;

import com.example.atm.workflow.ATMContext;

public interface ATMState {
    ATMState next(ATMContext context);
    String getStateName();
}
