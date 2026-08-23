package com.example.atm.exception;

public class InsufficientCashException extends RuntimeException {
    public InsufficientCashException() { super("ATM does not have sufficient cash"); }
}
