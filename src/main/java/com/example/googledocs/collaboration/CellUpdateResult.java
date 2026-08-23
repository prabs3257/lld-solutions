package com.example.googledocs.collaboration;

import com.example.googledocs.model.CellId;
import com.example.googledocs.operation.Operation;

/**
 * Result returned after an operation has been successfully committed.
 */
public final class CellUpdateResult {

    private final CellId cellId;
    private final String content;
    private final long version;
    private final Operation appliedOperation;

    public CellUpdateResult(
            CellId cellId,
            String content,
            long version,
            Operation appliedOperation) {

        this.cellId = cellId;
        this.content = content;
        this.version = version;
        this.appliedOperation = appliedOperation;
    }

    public CellId getCellId() {
        return cellId;
    }

    public String getContent() {
        return content;
    }

    public long getVersion() {
        return version;
    }

    public Operation getAppliedOperation() {
        return appliedOperation;
    }

    @Override
    public String toString() {
        return "CellUpdateResult{"
                + "cellId=" + cellId
                + ", content='" + content + '\''
                + ", version=" + version
                + ", operation=" + appliedOperation
                + '}';
    }
}
