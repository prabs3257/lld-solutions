package com.example.loan.strategy;

import com.example.loan.model.Installment;
import com.example.loan.model.LoanAccount;

import java.util.List;

public interface RepaymentScheduleStrategy {
    List<Installment> generate(LoanAccount loan);
}
