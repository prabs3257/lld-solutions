package com.example.loan.model;

import com.example.loan.enums.ChargeType;
import com.example.loan.enums.InstallmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Installment {

    private final int installmentNumber;
    private final LocalDate dueDate;
    private final BigDecimal principalDue;
    private final BigDecimal interestDue;

    private BigDecimal principalPaid = BigDecimal.ZERO;
    private BigDecimal interestPaid = BigDecimal.ZERO;
    private BigDecimal chargesPaid = BigDecimal.ZERO;

    private InstallmentStatus status = InstallmentStatus.PENDING;
    private final List<Charge> charges = new ArrayList<>();

    public Installment(int installmentNumber, LocalDate dueDate,
                       BigDecimal principalDue, BigDecimal interestDue) {
        this.installmentNumber = installmentNumber;
        this.dueDate = dueDate;
        this.principalDue = principalDue;
        this.interestDue = interestDue;
    }

    public BigDecimal getRemainingPrincipal() {
        return principalDue.subtract(principalPaid);
    }

    public BigDecimal getRemainingInterest() {
        return interestDue.subtract(interestPaid);
    }

    public BigDecimal getTotalCharges() {
        return charges.stream()
                .map(Charge::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getRemainingCharges() {
        return getTotalCharges().subtract(chargesPaid);
    }

    public BigDecimal getTotalRemaining() {
        return getRemainingCharges()
                .add(getRemainingInterest())
                .add(getRemainingPrincipal());
    }

    public boolean isPaid() {
        return getTotalRemaining().compareTo(BigDecimal.ZERO) <= 0;
    }

    public boolean isOverdue(LocalDate date) {
        return date.isAfter(dueDate) && !isPaid();
    }

    public void addCharge(Charge charge) {
        if (!hasCharge(charge.getType())) {
            charges.add(charge);
        }
    }

    public boolean hasCharge(ChargeType type) {
        return charges.stream()
                .anyMatch(charge -> charge.getType() == type);
    }

    public void applyPayment(PaymentAllocation allocation) {
        validatePayment(allocation);

        chargesPaid = chargesPaid.add(allocation.charges());
        interestPaid = interestPaid.add(allocation.interest());
        principalPaid = principalPaid.add(allocation.principal());

        updateStatus();
    }

    private void validatePayment(PaymentAllocation allocation) {
        if (allocation.charges().compareTo(getRemainingCharges()) > 0
                || allocation.interest().compareTo(getRemainingInterest()) > 0
                || allocation.principal().compareTo(getRemainingPrincipal()) > 0) {
            throw new IllegalArgumentException("Payment exceeds installment due");
        }
    }

    public void markOverdue() {
        if (!isPaid()) {
            status = InstallmentStatus.OVERDUE;
        }
    }

    private void updateStatus() {
        if (isPaid()) {
            status = InstallmentStatus.PAID;
        } else if (principalPaid.add(interestPaid).add(chargesPaid)
                .compareTo(BigDecimal.ZERO) > 0) {
            status = InstallmentStatus.PARTIALLY_PAID;
        }
    }

    public int getInstallmentNumber() { return installmentNumber; }
    public LocalDate getDueDate() { return dueDate; }
    public BigDecimal getPrincipalDue() { return principalDue; }
    public BigDecimal getInterestDue() { return interestDue; }
    public BigDecimal getPrincipalPaid() { return principalPaid; }
    public BigDecimal getInterestPaid() { return interestPaid; }
    public BigDecimal getChargesPaid() { return chargesPaid; }
    public InstallmentStatus getStatus() { return status; }
    public List<Charge> getCharges() { return List.copyOf(charges); }
}
