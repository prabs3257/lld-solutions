package com.example.googledocs.operation;

import java.util.Objects;

public final class InsertOperation extends Operation {

    private final int position;
    private final String text;

    public InsertOperation(String userId, long baseVersion, int position, String text) {
        super(userId, baseVersion);

        if (position < 0) {
            throw new IllegalArgumentException("Position cannot be negative");
        }

        this.position = position;
        this.text = Objects.requireNonNull(text);
    }

    public int getPosition() {
        return position;
    }

    public String getText() {
        return text;
    }

    @Override
    public OperationType getType() {
        return OperationType.INSERT;
    }

    @Override
    public String toString() {
        return "INSERT('" + text + "' at " + position + ")";
    }
}
