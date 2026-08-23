package com.example.atm.handler;

import com.example.atm.state.ATMState;

import java.util.HashMap;
import java.util.Map;

public class ATMStateHandlerFactory {
    private final Map<Class<? extends ATMState>, ATMStateHandler> handlers = new HashMap<>();

    public void register(Class<? extends ATMState> stateClass, ATMStateHandler handler) { handlers.put(stateClass, handler); }

    public ATMStateHandler getHandler(ATMState state) {
        ATMStateHandler handler = handlers.get(state.getClass());
        if (handler == null) throw new IllegalStateException("No handler registered for " + state.getClass().getSimpleName());
        return handler;
    }
}
