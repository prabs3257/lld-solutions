package com.example.atm.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException() { super("Insufficient account balance"); }
}
