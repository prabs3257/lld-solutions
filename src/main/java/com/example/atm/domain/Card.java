package com.example.atm.domain;

public class Card {
    private final String cardNumber;
    private final int pin;
    private final String accountNumber;

    public Card(String cardNumber, int pin, String accountNumber) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.accountNumber = accountNumber;
    }

    public String getCardNumber() { return cardNumber; }
    public int getPin() { return pin; }
    public String getAccountNumber() { return accountNumber; }
}
