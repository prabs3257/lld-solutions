package com.example.googledocs.operation;

public final class DeleteOperation extends Operation {

    private final int position;
    private final int length;

    public DeleteOperation(String userId, long baseVersion, int position, int length) {
        super(userId, baseVersion);

        if (position < 0 || length < 0) {
            throw new IllegalArgumentException("Position and length must be non-negative");
        }

        this.position = position;
        this.length = length;
    }

    public int getPosition() {
        return position;
    }

    public int getLength() {
        return length;
    }

    @Override
    public OperationType getType() {
        return OperationType.DELETE;
    }

    @Override
    public String toString() {
        return "DELETE(length=" + length + " at " + position + ")";
    }
}
