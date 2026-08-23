package com.example.loan.service;

import com.example.loan.enums.ChargeType;
import com.example.loan.enums.LoanStatus;
import com.example.loan.lock.LoanLockManager;
import com.example.loan.model.Charge;
import com.example.loan.model.Installment;
import com.example.loan.model.LoanAccount;
import com.example.loan.repository.LoanRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class OverdueProcessingService {

    private final LoanRepository loanRepository;
    private final LoanLockManager lockManager;
    private final BigDecimal lateFee;

    public OverdueProcessingService(
            LoanRepository loanRepository,
            LoanLockManager lockManager,
            BigDecimal lateFee) {
        this.loanRepository = loanRepository;
        this.lockManager = lockManager;
        this.lateFee = lateFee;
    }

    public void processLoan(String loanId, LocalDate processingDate) {

        ReentrantLock lock = lockManager.getLock(loanId);
        lock.lock();

        try {
            LoanAccount loan = loanRepository.findById(loanId)
                    .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

            if (loan.getStatus() != LoanStatus.ACTIVE) {
                return;
            }

            for (Installment installment : loan.getInstallments()) {
                if (installment.isOverdue(processingDate)) {
                    installment.markOverdue();

                    if (!installment.hasCharge(ChargeType.LATE_PAYMENT)) {
                        installment.addCharge(new Charge(
                                UUID.randomUUID().toString(),
                                ChargeType.LATE_PAYMENT,
                                lateFee,
                                processingDate
                        ));

                        System.out.printf(
                                "Late fee %s applied to installment %d%n",
                                lateFee,
                                installment.getInstallmentNumber()
                        );
                    }
                }
            }

            loanRepository.save(loan);

        } finally {
            lock.unlock();
        }
    }
}
