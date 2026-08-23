package com.example.atm.exception;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(String message) { super(message); }
}
