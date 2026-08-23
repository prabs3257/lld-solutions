package com.example.atm.workflow;


import com.example.atm.state.ATMState;

public class ATMContext {
    private ATMState currentState;
    private final ATMSession session;

    public ATMContext(ATMState initialState, ATMSession session) {
        this.currentState = initialState;
        this.session = session;
    }

    public ATMState getCurrentState() { return currentState; }
    public void setCurrentState(ATMState currentState) { this.currentState = currentState; }
    public ATMSession getSession() { return session; }
}
