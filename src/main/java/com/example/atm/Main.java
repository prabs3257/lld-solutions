package com.example.atm;


import com.example.atm.factory.ATMFactory;
import com.example.atm.workflow.ATMWorkflowEngine;

public class Main {
    public static void main(String[] args) {
        ATMWorkflowEngine atm = ATMFactory.createATM();
        while (true) {
            try {
                atm.executeStep();
            } catch (Exception e) {
                System.out.println("Transaction failed: " + e.getMessage());
                break;
            }
        }
    }
}
