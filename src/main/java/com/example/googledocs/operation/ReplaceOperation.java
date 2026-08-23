package com.example.googledocs.operation;

import java.util.Objects;

/**
 * Replaces the entire cell content.
 *
 * This operation is useful for spreadsheet-style updates, but simultaneous
 * full replacements need a deterministic conflict policy. The current
 * implementation uses a simple last-applied-wins policy.
 */
public final class ReplaceOperation extends Operation {

    private final String content;

    public ReplaceOperation(String userId, long baseVersion, String content) {
        super(userId, baseVersion);
        this.content = Objects.requireNonNull(content);
    }

    public String getContent() {
        return content;
    }

    @Override
    public OperationType getType() {
        return OperationType.REPLACE;
    }

    @Override
    public String toString() {
        return "REPLACE('" + content + "')";
    }
}
