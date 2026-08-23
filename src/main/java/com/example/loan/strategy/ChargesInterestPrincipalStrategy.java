package com.example.loan.strategy;

import com.example.loan.model.Installment;
import com.example.loan.model.PaymentAllocation;

import java.math.BigDecimal;

public class ChargesInterestPrincipalStrategy
        implements PaymentAllocationStrategy {

    @Override
    public PaymentAllocation allocate(BigDecimal amount,
                                      Installment installment) {

        BigDecimal remaining = amount;

        BigDecimal charges = remaining.min(installment.getRemainingCharges());
        remaining = remaining.subtract(charges);

        BigDecimal interest = remaining.min(installment.getRemainingInterest());
        remaining = remaining.subtract(interest);

        BigDecimal principal = remaining.min(installment.getRemainingPrincipal());

        return new PaymentAllocation(charges, interest, principal);
    }
}
