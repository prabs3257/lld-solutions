package com.example.googledocs.collaboration;

import com.example.googledocs.operation.Operation;

import java.util.ArrayList;
import java.util.List;

/**
 * Operation history for a single cell.
 *
 * Access to this class is guarded by the corresponding cell lock.
 * Therefore ArrayList is sufficient and easier to reason about than
 * using a concurrent collection.
 */
public class OperationHistory {

    private final List<VersionedOperation> operations = new ArrayList<>();

    public void add(long version, Operation operation) {
        operations.add(new VersionedOperation(version, operation));
    }

    /**
     * Returns all operations applied after the supplied version.
     *
     * Example:
     * User created an operation at version 5.
     * Cell is now at version 8.
     * We return operations that produced versions 6, 7 and 8.
     */
    public List<VersionedOperation> getOperationsAfter(long version) {
        List<VersionedOperation> result = new ArrayList<>();

        for (VersionedOperation operation : operations) {
            if (operation.getVersion() > version) {
                result.add(operation);
            }
        }

        return result;
    }
}
