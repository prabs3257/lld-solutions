package com.example.atm.handler;

import com.example.atm.domain.ATMOperation;
import com.example.atm.domain.Account;
import com.example.atm.domain.Card;
import com.example.atm.input.ATMInput;
import com.example.atm.service.AccountService;
import com.example.atm.service.AuthenticationService;
import com.example.atm.workflow.ATMContext;

public class HasCardStateHandler implements ATMStateHandler {
    private final ATMInput input;
    private final AuthenticationService authenticationService;
    private final AccountService accountService;

    public HasCardStateHandler(ATMInput input, AuthenticationService authenticationService, AccountService accountService) {
        this.input = input;
        this.authenticationService = authenticationService;
        this.accountService = accountService;
    }

    @Override
    public void handle(ATMContext context) {
        var session = context.getSession();
        Card card = session.getCard();
        boolean authenticated = authenticationService.authenticate(card, input.readPin());
        if (!authenticated) {
            System.out.println("Invalid PIN");
            session.setAuthenticated(false);
            return;
        }
        Account account = accountService.getAccount(card);
        session.setAccount(account);
        session.setAuthenticated(true);
        ATMOperation operation = input.readOperation();
        session.setSelectedOperation(operation);
    }
}
