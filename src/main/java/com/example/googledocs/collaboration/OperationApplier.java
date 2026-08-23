package com.example.googledocs.collaboration;

import com.example.googledocs.operation.DeleteOperation;
import com.example.googledocs.operation.InsertOperation;
import com.example.googledocs.operation.Operation;
import com.example.googledocs.operation.ReplaceOperation;

/**
 * Contains the mechanics of applying a transformed operation to cell content.
 *
 * Keeping this separate from Cell makes the domain object simple and makes
 * operation application independently testable.
 */
public class OperationApplier {

    public String apply(String currentContent, Operation operation) {

        if (operation instanceof InsertOperation) {
            return applyInsert(currentContent, (InsertOperation) operation);
        }

        if (operation instanceof DeleteOperation) {
            return applyDelete(currentContent, (DeleteOperation) operation);
        }

        if (operation instanceof ReplaceOperation) {
            return ((ReplaceOperation) operation).getContent();
        }

        throw new IllegalArgumentException("Unsupported operation");
    }

    private String applyInsert(String content, InsertOperation operation) {
        int position = operation.getPosition();

        if (position < 0 || position > content.length()) {
            throw new IllegalArgumentException(
                    "Invalid insert position " + position
                            + " for content length " + content.length()
            );
        }

        return content.substring(0, position)
                + operation.getText()
                + content.substring(position);
    }

    private String applyDelete(String content, DeleteOperation operation) {
        int start = operation.getPosition();
        int end = start + operation.getLength();

        if (start < 0 || end > content.length()) {
            throw new IllegalArgumentException(
                    "Invalid delete range [" + start + ", " + end + ")"
            );
        }

        return content.substring(0, start)
                + content.substring(end);
    }
}
