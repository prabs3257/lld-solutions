package com.example.splitwise.model;

public record Settlement(String fromUserId, String toUserId, long amountInCents) {
    public Settlement {
        if (fromUserId == null || toUserId == null) {
            throw new IllegalArgumentException("Settlement users are required");
        }
        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("Settlement users must be different");
        }
        if (amountInCents <= 0) {
            throw new IllegalArgumentException("Settlement must be positive");
        }
    }
}
