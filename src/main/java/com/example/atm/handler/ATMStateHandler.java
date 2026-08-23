package com.example.atm.handler;

import com.example.atm.workflow.ATMContext;

public interface ATMStateHandler {
    void handle(ATMContext context);
}
