package com.example.googledocs.collaboration;

import com.example.googledocs.operation.DeleteOperation;
import com.example.googledocs.operation.InsertOperation;
import com.example.googledocs.operation.Operation;
import com.example.googledocs.operation.ReplaceOperation;

/**
 * A small but functional Operational Transformation style implementation.
 *
 * It supports:
 *   Insert vs Insert
 *   Insert vs Delete
 *   Delete vs Insert
 *   Delete vs Delete
 *
 * Tie breaking for simultaneous inserts is deterministic using userId and,
 * if needed, operationId. Every node must use the same rule to converge.
 *
 * Replace is intentionally treated as a coarse-grained "last applied wins"
 * operation. In a real Google Docs implementation, rich text would normally
 * be represented by fine-grained operations instead of full replacements.
 */
public class SimpleOperationTransformer implements OperationTransformer {

    @Override
    public Operation transform(Operation incoming, Operation alreadyApplied) {

        if (incoming instanceof ReplaceOperation) {
            return incoming;
        }

        if (alreadyApplied instanceof ReplaceOperation) {
            // A full replacement destroys positional context.
            // This simplified policy applies the incoming operation against
            // the new content using its original coordinates.
            return incoming;
        }

        if (incoming instanceof InsertOperation && alreadyApplied instanceof InsertOperation) {
            return transformInsertAgainstInsert(
                    (InsertOperation) incoming,
                    (InsertOperation) alreadyApplied
            );
        }

        if (incoming instanceof InsertOperation && alreadyApplied instanceof DeleteOperation) {
            return transformInsertAgainstDelete(
                    (InsertOperation) incoming,
                    (DeleteOperation) alreadyApplied
            );
        }

        if (incoming instanceof DeleteOperation && alreadyApplied instanceof InsertOperation) {
            return transformDeleteAgainstInsert(
                    (DeleteOperation) incoming,
                    (InsertOperation) alreadyApplied
            );
        }

        if (incoming instanceof DeleteOperation && alreadyApplied instanceof DeleteOperation) {
            return transformDeleteAgainstDelete(
                    (DeleteOperation) incoming,
                    (DeleteOperation) alreadyApplied
            );
        }

        throw new IllegalArgumentException(
                "Unsupported transformation: "
                        + incoming.getType()
                        + " against "
                        + alreadyApplied.getType()
        );
    }

    private Operation transformInsertAgainstInsert(
            InsertOperation incoming,
            InsertOperation applied) {

        int position = incoming.getPosition();

        boolean appliedComesBeforeIncoming =
                applied.getPosition() < position
                        || (applied.getPosition() == position
                        && compareTieBreaker(applied, incoming) < 0);

        if (appliedComesBeforeIncoming) {
            position += applied.getText().length();
        }

        return new InsertOperation(
                incoming.getUserId(),
                incoming.getBaseVersion(),
                position,
                incoming.getText()
        );
    }

    private Operation transformInsertAgainstDelete(
            InsertOperation incoming,
            DeleteOperation applied) {

        int deleteStart = applied.getPosition();
        int deleteEnd = deleteStart + applied.getLength();
        int position = incoming.getPosition();

        if (position <= deleteStart) {
            // Delete happened after our insertion point.
            return copyInsert(incoming, position);
        }

        if (position >= deleteEnd) {
            // Characters before our insertion point disappeared.
            return copyInsert(incoming, position - applied.getLength());
        }

        // The original insertion position was inside deleted text.
        // Clamp it to the beginning of the deleted range.
        return copyInsert(incoming, deleteStart);
    }

    private Operation transformDeleteAgainstInsert(
            DeleteOperation incoming,
            InsertOperation applied) {

        int deleteStart = incoming.getPosition();
        int deleteEnd = deleteStart + incoming.getLength();
        int insertPosition = applied.getPosition();
        int insertLength = applied.getText().length();

        if (insertPosition < deleteStart
                || (insertPosition == deleteStart
                && compareTieBreaker(applied, incoming) < 0)) {

            deleteStart += insertLength;
            deleteEnd += insertLength;

        } else if (insertPosition > deleteStart && insertPosition < deleteEnd) {
            // Preserve the text inserted by the other user by extending the
            // end of our deletion range.
            deleteEnd += insertLength;
        }

        return new DeleteOperation(
                incoming.getUserId(),
                incoming.getBaseVersion(),
                deleteStart,
                Math.max(0, deleteEnd - deleteStart)
        );
    }

    private Operation transformDeleteAgainstDelete(
            DeleteOperation incoming,
            DeleteOperation applied) {

        int incomingStart = incoming.getPosition();
        int incomingEnd = incomingStart + incoming.getLength();

        int appliedStart = applied.getPosition();
        int appliedEnd = appliedStart + applied.getLength();

        // No overlap: applied delete was completely before incoming.
        if (appliedEnd <= incomingStart) {
            return new DeleteOperation(
                    incoming.getUserId(),
                    incoming.getBaseVersion(),
                    incomingStart - applied.getLength(),
                    incoming.getLength()
            );
        }

        // No overlap: applied delete was completely after incoming.
        if (appliedStart >= incomingEnd) {
            return copyDelete(incoming, incomingStart, incoming.getLength());
        }

        // Overlap: remove the portion already deleted by the applied operation.
        int overlapStart = Math.max(incomingStart, appliedStart);
        int overlapEnd = Math.min(incomingEnd, appliedEnd);
        int overlap = Math.max(0, overlapEnd - overlapStart);

        int newStart = incomingStart;
        if (appliedStart < incomingStart) {
            newStart = appliedStart;
        }

        int newLength = Math.max(0, incoming.getLength() - overlap);

        return new DeleteOperation(
                incoming.getUserId(),
                incoming.getBaseVersion(),
                newStart,
                newLength
        );
    }

    private int compareTieBreaker(Operation first, Operation second) {
        int userComparison = first.getUserId().compareTo(second.getUserId());

        return userComparison != 0
                ? userComparison
                : first.getOperationId().compareTo(second.getOperationId());
    }

    private InsertOperation copyInsert(InsertOperation operation, int position) {
        return new InsertOperation(
                operation.getUserId(),
                operation.getBaseVersion(),
                position,
                operation.getText()
        );
    }

    private DeleteOperation copyDelete(
            DeleteOperation operation,
            int position,
            int length) {

        return new DeleteOperation(
                operation.getUserId(),
                operation.getBaseVersion(),
                position,
                length
        );
    }
}
