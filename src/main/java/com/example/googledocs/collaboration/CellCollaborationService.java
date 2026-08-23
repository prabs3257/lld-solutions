package com.example.googledocs.collaboration;

import com.example.googledocs.lock.CellLockManager;
import com.example.googledocs.model.Cell;
import com.example.googledocs.operation.Operation;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Core concurrency component.
 *
 * The important flow is:
 *
 * 1. Acquire the lock for exactly one cell.
 * 2. Read the latest version.
 * 3. If the incoming operation was created on an older version,
 *    transform it against all operations the user missed.
 * 4. Apply the transformed operation.
 * 5. Increment cell version.
 * 6. Append the operation to history.
 * 7. Release the lock.
 *
 * Steps 2-6 must happen atomically for a single cell.
 */
public class CellCollaborationService {

    private final CellLockManager lockManager;
    private final OperationTransformer operationTransformer;
    private final OperationApplier operationApplier;

    public CellCollaborationService(
            CellLockManager lockManager,
            OperationTransformer operationTransformer,
            OperationApplier operationApplier) {

        this.lockManager = lockManager;
        this.operationTransformer = operationTransformer;
        this.operationApplier = operationApplier;
    }

    public CellUpdateResult applyOperation(
            Cell cell,
            Operation incomingOperation) {

        ReentrantLock lock = lockManager.getLock(cell.getCellId());
        lock.lock();

        try {
            Operation transformedOperation = incomingOperation;

            // A stale operation was created against an older snapshot.
            // Bring it forward by transforming it against every operation
            // committed after its baseVersion.
            if (incomingOperation.getBaseVersion() < cell.getVersion()) {

                List<VersionedOperation> missedOperations =
                        cell.getOperationHistory()
                                .getOperationsAfter(incomingOperation.getBaseVersion());

                for (VersionedOperation missedOperation : missedOperations) {
                    transformedOperation =
                            operationTransformer.transform(
                                    transformedOperation,
                                    missedOperation.getOperation()
                            );
                }
            }

            String updatedContent =
                    operationApplier.apply(
                            cell.getContent(),
                            transformedOperation
                    );

            // This increments the version.
            cell.updateContent(updatedContent);

            long newVersion = cell.getVersion();

            cell.getOperationHistory().add(
                    newVersion,
                    transformedOperation
            );

            return new CellUpdateResult(
                    cell.getCellId(),
                    updatedContent,
                    newVersion,
                    transformedOperation
            );

        } finally {
            // Never leave a cell locked if transformation/application throws.
            lock.unlock();
        }
    }
}
