package com.example.atm.input;

import com.example.atm.domain.ATMOperation;
import com.example.atm.domain.Card;

import java.util.Scanner;

public class ConsoleATMInput implements ATMInput {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public Card readCard() {
        System.out.print("Enter card number: ");
        String cardNumber = scanner.nextLine();
        return new Card(cardNumber, 1234, "ACC-001");
    }

    @Override
    public int readPin() {
        System.out.print("Enter PIN: ");
        return scanner.nextInt();
    }

    @Override
    public ATMOperation readOperation() {
        System.out.println("1. Withdraw");
        System.out.println("2. Deposit");
        System.out.println("3. Check Balance");
        System.out.print("Choose operation: ");
        int choice = scanner.nextInt();
        return switch (choice) {
            case 1 -> ATMOperation.WITHDRAW;
            case 2 -> ATMOperation.DEPOSIT;
            case 3 -> ATMOperation.CHECK_BALANCE;
            default -> throw new IllegalArgumentException("Invalid operation");
        };
    }

    @Override
    public int readAmount() {
        System.out.print("Enter amount: ");
        return scanner.nextInt();
    }
}
