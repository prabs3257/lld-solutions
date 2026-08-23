package com.example.loan.model;

import java.math.BigDecimal;

public record PaymentAllocation(
        BigDecimal charges,
        BigDecimal interest,
        BigDecimal principal) {

    public BigDecimal total() {
        return charges.add(interest).add(principal);
    }

    public static PaymentAllocation empty() {
        return new PaymentAllocation(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );
    }
}
