package com.example.loan.strategy;

import com.example.loan.model.Installment;
import com.example.loan.model.PaymentAllocation;

import java.math.BigDecimal;

public interface PaymentAllocationStrategy {
    PaymentAllocation allocate(BigDecimal amount, Installment installment);
}
