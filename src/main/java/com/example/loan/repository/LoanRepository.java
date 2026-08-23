package com.example.loan.repository;

import com.example.loan.model.LoanAccount;

import java.util.Optional;

public interface LoanRepository {
    void save(LoanAccount loan);
    Optional<LoanAccount> findById(String loanId);
}
