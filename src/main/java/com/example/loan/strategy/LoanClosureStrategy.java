package com.example.loan.strategy;

import com.example.loan.model.LoanAccount;
import com.example.loan.model.SettlementQuote;

import java.time.LocalDate;

public interface LoanClosureStrategy {
    SettlementQuote calculate(LoanAccount loan, LocalDate closureDate);
}
