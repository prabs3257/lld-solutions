package com.example.loan.model;

import com.example.loan.enums.PaymentMode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Repayment {

    private final String repaymentId;
    private final String loanId;
    private final BigDecimal amount;
    private final LocalDateTime paymentTime;
    private final PaymentMode paymentMode;
    private final String idempotencyKey;

    public Repayment(String repaymentId, String loanId,
                     BigDecimal amount, LocalDateTime paymentTime,
                     PaymentMode paymentMode, String idempotencyKey) {
        this.repaymentId = repaymentId;
        this.loanId = loanId;
        this.amount = amount;
        this.paymentTime = paymentTime;
        this.paymentMode = paymentMode;
        this.idempotencyKey = idempotencyKey;
    }

    public String getRepaymentId() { return repaymentId; }
    public String getLoanId() { return loanId; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getPaymentTime() { return paymentTime; }
    public PaymentMode getPaymentMode() { return paymentMode; }
    public String getIdempotencyKey() { return idempotencyKey; }
}
