package com.example.atm.service;

import com.example.atm.domain.Card;

public class AuthenticationService {
    public boolean authenticate(Card card, int pin) {
        return card != null && card.getPin() == pin;
    }
}
