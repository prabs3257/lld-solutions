package com.example.atm.input;

import com.example.atm.domain.ATMOperation;
import com.example.atm.domain.Card;

public interface ATMInput {
    Card readCard();
    int readPin();
    ATMOperation readOperation();
    int readAmount();
}
