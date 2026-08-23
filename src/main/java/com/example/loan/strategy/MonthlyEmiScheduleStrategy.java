package com.example.loan.strategy;

import com.example.loan.model.Installment;
import com.example.loan.model.LoanAccount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MonthlyEmiScheduleStrategy implements RepaymentScheduleStrategy {

    private final EmiCalculator emiCalculator;

    public MonthlyEmiScheduleStrategy(EmiCalculator emiCalculator) {
        this.emiCalculator = emiCalculator;
    }

    @Override
    public List<Installment> generate(LoanAccount loan) {

        List<Installment> installments = new ArrayList<>();


        //this is the monthly emi
        BigDecimal emi = emiCalculator.calculate(
                loan.getOriginalPrincipal(),
                loan.getAnnualInterestRate(),
                loan.getTenureInMonths()
        );

        BigDecimal outstanding = loan.getOriginalPrincipal();

        BigDecimal monthlyRate = loan.getAnnualInterestRate()
                .divide(BigDecimal.valueOf(12), 15, RoundingMode.HALF_UP);

        for (int i = 1; i <= loan.getTenureInMonths(); i++) {

            // interest for that installment  = principal left * monthly rate
            BigDecimal interest = outstanding.multiply(monthlyRate)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal principal;

            if (i == loan.getTenureInMonths()) {
                principal = outstanding;
                interest = emi.subtract(principal).max(BigDecimal.ZERO);
            } else {
                principal = emi.subtract(interest)
                        .setScale(2, RoundingMode.HALF_UP);
            }

            LocalDate dueDate = loan.getDisbursementDate().plusMonths(i);

            installments.add(new Installment(
                    i, dueDate, principal, interest
            ));

            outstanding = outstanding.subtract(principal);
        }

        return installments;
    }
}
