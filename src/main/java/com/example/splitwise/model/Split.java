package com.example.splitwise.model;

public record Split(String userId, long amountInCents) {
    public Split {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("Split user id is required");
        }
        if (amountInCents < 0) {
            throw new IllegalArgumentException("Split amount cannot be negative");
        }
    }
}
