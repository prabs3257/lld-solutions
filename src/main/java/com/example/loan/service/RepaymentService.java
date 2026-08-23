package com.example.loan.service;

import com.example.loan.enums.LoanStatus;
import com.example.loan.enums.PaymentMode;
import com.example.loan.lock.LoanLockManager;
import com.example.loan.model.Installment;
import com.example.loan.model.LoanAccount;
import com.example.loan.model.PaymentAllocation;
import com.example.loan.model.Repayment;
import com.example.loan.repository.LoanRepository;
import com.example.loan.strategy.PaymentAllocationStrategy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class RepaymentService {

    private final LoanRepository loanRepository;
    private final PaymentAllocationStrategy allocationStrategy;
    private final LoanLockManager lockManager;

    public RepaymentService(LoanRepository loanRepository,
                            PaymentAllocationStrategy allocationStrategy,
                            LoanLockManager lockManager) {
        this.loanRepository = loanRepository;
        this.allocationStrategy = allocationStrategy;
        this.lockManager = lockManager;
    }

    public void makeRepayment(
            String loanId,
            BigDecimal amount,
            PaymentMode paymentMode,
            String idempotencyKey) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }

        ReentrantLock lock = lockManager.getLock(loanId);
        lock.lock();

        try {
            LoanAccount loan = loanRepository.findById(loanId)
                    .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

            if (loan.getStatus() != LoanStatus.ACTIVE) {
                throw new IllegalStateException("Loan is not active");
            }

            if (loan.hasProcessedPayment(idempotencyKey)) {
                System.out.println("Duplicate request ignored: " + idempotencyKey);
                return;
            }

            BigDecimal remaining = amount;

            for (Installment installment : loan.getInstallments()) {

                if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }

                if (installment.isPaid()) {
                    continue;
                }

                PaymentAllocation allocation =
                        allocationStrategy.allocate(remaining, installment);

                if (allocation.total().compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }

                installment.applyPayment(allocation);
                loan.reducePrincipal(allocation.principal());
                remaining = remaining.subtract(allocation.total());

                System.out.printf(
                        "Loan=%s | Installment=%d | Charges=%s | Interest=%s | Principal=%s | Remaining Payment=%s%n",
                        loanId,
                        installment.getInstallmentNumber(),
                        allocation.charges(),
                        allocation.interest(),
                        allocation.principal(),
                        remaining
                );
            }

            if (remaining.compareTo(BigDecimal.ZERO) > 0) {
                throw new IllegalArgumentException(
                        "Payment exceeds total outstanding by " + remaining
                );
            }

            Repayment repayment = new Repayment(
                    UUID.randomUUID().toString(),
                    loanId,
                    amount,
                    LocalDateTime.now(),
                    paymentMode,
                    idempotencyKey
            );

            loan.addRepayment(repayment);

            if (loan.isFullySettled()) {
                loan.close();
                System.out.println("Loan closed successfully: " + loanId);
            }

            loanRepository.save(loan);

        } finally {
            lock.unlock();
        }
    }
}
