package com.example.loan.service;

import com.example.loan.model.Installment;
import com.example.loan.model.LoanAccount;
import com.example.loan.repository.LoanRepository;
import com.example.loan.strategy.RepaymentScheduleStrategy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class LoanService {

    private final LoanRepository loanRepository;
    private final RepaymentScheduleStrategy scheduleStrategy;

    public LoanService(LoanRepository loanRepository,
                       RepaymentScheduleStrategy scheduleStrategy) {
        this.loanRepository = loanRepository;
        this.scheduleStrategy = scheduleStrategy;
    }

    public LoanAccount createAndActivateLoan(
            String loanId,
            String customerId,
            BigDecimal principal,
            BigDecimal annualInterestRate,
            LocalDate disbursementDate,
            int tenureMonths) {

        LoanAccount loan = new LoanAccount(
                loanId, customerId, principal,
                annualInterestRate, disbursementDate, tenureMonths
        );

        List<Installment> schedule = scheduleStrategy.generate(loan);
        loan.activate(schedule);
        loanRepository.save(loan);

        return loan;
    }
}
