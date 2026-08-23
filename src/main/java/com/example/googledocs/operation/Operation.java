package com.example.googledocs.operation;

import java.util.Objects;
import java.util.UUID;

/**
 * Base class for every change made to a cell.
 *
 * baseVersion is the version of the cell that the user saw when creating
 * this operation. If the cell has advanced since then, the operation must
 * be transformed before applying it.
 */
public abstract class Operation {

    private final String operationId;
    private final String userId;
    private final long baseVersion;

    protected Operation(String userId, long baseVersion) {
        this(UUID.randomUUID().toString(), userId, baseVersion);
    }

    protected Operation(String operationId, String userId, long baseVersion) {
        this.operationId = Objects.requireNonNull(operationId);
        this.userId = Objects.requireNonNull(userId);

        if (baseVersion < 0) {
            throw new IllegalArgumentException("baseVersion cannot be negative");
        }

        this.baseVersion = baseVersion;
    }

    public String getOperationId() {
        return operationId;
    }

    public String getUserId() {
        return userId;
    }

    public long getBaseVersion() {
        return baseVersion;
    }

    public abstract OperationType getType();
}
