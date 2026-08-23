package com.example.googledocs.lock;

import com.example.googledocs.model.CellId;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides one lock per cell.
 *
 * This is the chosen lock granularity:
 *
 * User A editing A1 does not block User B editing B1.
 * User A and User B editing A1 are serialized.
 *
 * The lock manager separates concurrency concerns from the Cell entity.
 */
public class CellLockManager {

    private final ConcurrentHashMap<CellId, ReentrantLock> locks =
            new ConcurrentHashMap<>();

    public ReentrantLock getLock(CellId cellId) {
        return locks.computeIfAbsent(
                cellId,
                ignored -> new ReentrantLock()
        );
    }
}
