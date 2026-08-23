package com.example.atm.workflow;


import com.example.atm.domain.ATMOperation;
import com.example.atm.domain.Account;
import com.example.atm.domain.Card;

public class ATMSession {
    private Card card;
    private Account account;
    private boolean authenticated;
    private ATMOperation selectedOperation;

    public Card getCard() { return card; }
    public void setCard(Card card) { this.card = card; }
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    public boolean isAuthenticated() { return authenticated; }
    public void setAuthenticated(boolean authenticated) { this.authenticated = authenticated; }
    public ATMOperation getSelectedOperation() { return selectedOperation; }
    public void setSelectedOperation(ATMOperation selectedOperation) { this.selectedOperation = selectedOperation; }

    public void clear() {
        card = null;
        account = null;
        authenticated = false;
        selectedOperation = null;
    }
}
