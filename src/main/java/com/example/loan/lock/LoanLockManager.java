package com.example.loan.lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LoanLockManager {

    private final ConcurrentHashMap<String, ReentrantLock> locks =
            new ConcurrentHashMap<>();

    public ReentrantLock getLock(String loanId) {
        return locks.computeIfAbsent(loanId, id -> new ReentrantLock());
    }
}
