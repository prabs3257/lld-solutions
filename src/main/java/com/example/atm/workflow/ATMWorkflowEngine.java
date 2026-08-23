package com.example.atm.workflow;


import com.example.atm.handler.ATMStateHandler;
import com.example.atm.handler.ATMStateHandlerFactory;
import com.example.atm.state.ATMState;

public class ATMWorkflowEngine {
    private final ATMContext context;
    private final ATMStateHandlerFactory handlerFactory;

    public ATMWorkflowEngine(ATMContext context, ATMStateHandlerFactory handlerFactory) {
        this.context = context;
        this.handlerFactory = handlerFactory;
    }

    public void executeStep() {
        ATMState currentState = context.getCurrentState();
        ATMStateHandler handler = handlerFactory.getHandler(currentState);
        handler.handle(context);
        ATMState nextState = currentState.next(context);
        context.setCurrentState(nextState);
    }

    public ATMState getCurrentState() { return context.getCurrentState(); }
}
