package com.example.loan.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class EmiCalculator {

    public BigDecimal calculate(BigDecimal principal,
                                BigDecimal annualRate,
                                int months) {

        BigDecimal monthlyRate = annualRate.divide(
                BigDecimal.valueOf(12),
                15,
                RoundingMode.HALF_UP
        );

        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(
                    BigDecimal.valueOf(months),
                    2,
                    RoundingMode.HALF_UP
            );
        }

        double p = principal.doubleValue();
        double r = monthlyRate.doubleValue();
        double factor = Math.pow(1 + r, months);

        return BigDecimal.valueOf(
                p * r * factor / (factor - 1)
        ).setScale(2, RoundingMode.HALF_UP);
    }
}
