package com.example.loan.strategy;

import com.example.loan.model.Installment;
import com.example.loan.model.LoanAccount;
import com.example.loan.model.SettlementQuote;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SimpleLoanClosureStrategy implements LoanClosureStrategy {

    @Override
    public SettlementQuote calculate(LoanAccount loan,
                                     LocalDate closureDate) {

        BigDecimal principal = BigDecimal.ZERO;
        BigDecimal interest = BigDecimal.ZERO;
        BigDecimal charges = BigDecimal.ZERO;

        for (Installment installment : loan.getInstallments()) {
            principal = principal.add(installment.getRemainingPrincipal());
            interest = interest.add(installment.getRemainingInterest());
            charges = charges.add(installment.getRemainingCharges());
        }

        BigDecimal total = principal.add(interest).add(charges);

        return new SettlementQuote(principal, interest, charges, total);
    }
}
