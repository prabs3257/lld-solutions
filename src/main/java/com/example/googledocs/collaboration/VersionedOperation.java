package com.example.googledocs.collaboration;

import com.example.googledocs.operation.Operation;

/**
 * Stores an operation together with the cell version produced after
 * applying that operation.
 */
public final class VersionedOperation {

    private final long version;
    private final Operation operation;

    public VersionedOperation(long version, Operation operation) {
        this.version = version;
        this.operation = operation;
    }

    public long getVersion() {
        return version;
    }

    public Operation getOperation() {
        return operation;
    }
}
