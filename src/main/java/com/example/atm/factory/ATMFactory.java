package com.example.atm.factory;

import com.example.atm.domain.ATMInventory;
import com.example.atm.domain.Account;
import com.example.atm.handler.*;
import com.example.atm.input.ATMInput;
import com.example.atm.input.ConsoleATMInput;
import com.example.atm.service.*;
import com.example.atm.state.*;
import com.example.atm.workflow.ATMContext;
import com.example.atm.workflow.ATMSession;
import com.example.atm.workflow.ATMWorkflowEngine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ATMFactory {
    public static ATMWorkflowEngine createATM() {
        Account account = new Account("ACC-001", BigDecimal.valueOf(10000));
        Map<String, Account> accounts = new HashMap<>();
        accounts.put(account.getAccountNumber(), account);

        ATMInput input = new ConsoleATMInput();
        ATMInventory inventory = new ATMInventory();

        AuthenticationService authenticationService = new AuthenticationService();
        AccountService accountService = new AccountService(accounts);
        WithdrawalService withdrawalService = new WithdrawalService(inventory);
        DepositService depositService = new DepositService();
        BalanceService balanceService = new BalanceService();

        ATMStateHandlerFactory handlerFactory = new ATMStateHandlerFactory();
        handlerFactory.register(IdleState.class, new IdleStateHandler(input));
        handlerFactory.register(HasCardState.class, new HasCardStateHandler(input, authenticationService, accountService));
        handlerFactory.register(WithdrawalState.class, new WithdrawalStateHandler(input, withdrawalService));
        handlerFactory.register(DepositState.class, new DepositStateHandler(input, depositService));
        handlerFactory.register(CheckBalanceState.class, new CheckBalanceStateHandler(balanceService));

        return new ATMWorkflowEngine(new ATMContext(new IdleState(), new ATMSession()), handlerFactory);
    }
}
