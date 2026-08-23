package com.example.loan.repository;

import com.example.loan.model.LoanAccount;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryLoanRepository implements LoanRepository {

    private final Map<String, LoanAccount> loans = new ConcurrentHashMap<>();

    @Override
    public void save(LoanAccount loan) {
        loans.put(loan.getLoanId(), loan);
    }

    @Override
    public Optional<LoanAccount> findById(String loanId) {
        return Optional.ofNullable(loans.get(loanId));
    }
}
