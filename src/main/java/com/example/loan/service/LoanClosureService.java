package com.example.loan.service;

import com.example.loan.enums.LoanStatus;
import com.example.loan.lock.LoanLockManager;
import com.example.loan.model.Installment;
import com.example.loan.model.LoanAccount;
import com.example.loan.model.PaymentAllocation;
import com.example.loan.model.SettlementQuote;
import com.example.loan.repository.LoanRepository;
import com.example.loan.strategy.LoanClosureStrategy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.locks.ReentrantLock;

public class LoanClosureService {

    private final LoanRepository loanRepository;
    private final LoanClosureStrategy closureStrategy;
    private final LoanLockManager lockManager;

    public LoanClosureService(
            LoanRepository loanRepository,
            LoanClosureStrategy closureStrategy,
            LoanLockManager lockManager) {
        this.loanRepository = loanRepository;
        this.closureStrategy = closureStrategy;
        this.lockManager = lockManager;
    }

    public SettlementQuote getSettlementQuote(
            String loanId, LocalDate closureDate) {

        LoanAccount loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        return closureStrategy.calculate(loan, closureDate);
    }

    public void closeLoan(
            String loanId,
            BigDecimal paymentAmount,
            LocalDate closureDate) {

        ReentrantLock lock = lockManager.getLock(loanId);
        lock.lock();

        try {
            LoanAccount loan = loanRepository.findById(loanId)
                    .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

            if (loan.getStatus() != LoanStatus.ACTIVE) {
                throw new IllegalStateException("Loan is not active");
            }

            SettlementQuote quote =
                    closureStrategy.calculate(loan, closureDate);

            if (paymentAmount.compareTo(quote.totalSettlementAmount()) < 0) {
                throw new IllegalArgumentException(
                        "Insufficient amount for closure. Required="
                                + quote.totalSettlementAmount()
                );
            }

            for (Installment installment : loan.getInstallments()) {
                installment.applyPayment(new PaymentAllocation(
                        installment.getRemainingCharges(),
                        installment.getRemainingInterest(),
                        installment.getRemainingPrincipal()
                ));
            }

            loan.reducePrincipal(loan.getOutstandingPrincipal());
            loan.close();
            loanRepository.save(loan);

            System.out.println("Loan foreclosed successfully: " + loanId);

        } finally {
            lock.unlock();
        }
    }
}
