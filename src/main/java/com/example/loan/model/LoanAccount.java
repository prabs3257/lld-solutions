package com.example.loan.model;

import com.example.loan.enums.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanAccount {

    private final String loanId;
    private final String customerId;
    private final BigDecimal originalPrincipal;
    private BigDecimal outstandingPrincipal;
    private final BigDecimal annualInterestRate;
    private final LocalDate disbursementDate;
    private final int tenureInMonths;

    private LoanStatus status;

    private final List<Installment> installments = new ArrayList<>();
    private final List<Repayment> repayments = new ArrayList<>();

    public LoanAccount(String loanId, String customerId,
                       BigDecimal principal, BigDecimal annualInterestRate,
                       LocalDate disbursementDate, int tenureInMonths) {

        if (principal == null || principal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Principal must be positive");
        }

        this.loanId = loanId;
        this.customerId = customerId;
        this.originalPrincipal = principal;
        this.outstandingPrincipal = principal;
        this.annualInterestRate = annualInterestRate;
        this.disbursementDate = disbursementDate;
        this.tenureInMonths = tenureInMonths;
        this.status = LoanStatus.APPROVED;
    }

    public void activate(List<Installment> repaymentSchedule) {
        if (status != LoanStatus.APPROVED) {
            throw new IllegalStateException("Loan cannot be activated");
        }
        installments.addAll(repaymentSchedule);
        status = LoanStatus.ACTIVE;
    }

    public void addRepayment(Repayment repayment) {
        repayments.add(repayment);
    }

    public boolean hasProcessedPayment(String idempotencyKey) {
        return repayments.stream()
                .anyMatch(r -> r.getIdempotencyKey().equals(idempotencyKey));
    }

    public void reducePrincipal(BigDecimal principalAmount) {
        outstandingPrincipal = outstandingPrincipal.subtract(principalAmount);
        if (outstandingPrincipal.compareTo(BigDecimal.ZERO) < 0) {
            outstandingPrincipal = BigDecimal.ZERO;
        }
    }

    public boolean isFullySettled() {
        return installments.stream().allMatch(Installment::isPaid);
    }

    public void close() {
        if (!isFullySettled()) {
            throw new IllegalStateException("Loan still has outstanding dues");
        }
        status = LoanStatus.CLOSED;
    }

    public String getLoanId() { return loanId; }
    public String getCustomerId() { return customerId; }
    public BigDecimal getOriginalPrincipal() { return originalPrincipal; }
    public BigDecimal getOutstandingPrincipal() { return outstandingPrincipal; }
    public BigDecimal getAnnualInterestRate() { return annualInterestRate; }
    public LocalDate getDisbursementDate() { return disbursementDate; }
    public int getTenureInMonths() { return tenureInMonths; }
    public LoanStatus getStatus() { return status; }
    public List<Installment> getInstallments() { return installments; }
    public List<Repayment> getRepayments() { return List.copyOf(repayments); }
}
