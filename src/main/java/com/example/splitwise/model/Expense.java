package com.example.splitwise.model;

import java.time.Instant;
import java.util.List;

public final class Expense {
    private final String id;
    private final String description;
    private final long amountInCents;
    private final String paidByUserId;
    private final List<Split> splits;
    private final String groupId;
    private final Instant createdAt;

    public Expense(
            String id,
            String description,
            long amountInCents,
            String paidByUserId,
            List<Split> splits,
            String groupId
    ) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Expense id is required");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("Description is required");
        if (amountInCents <= 0) throw new IllegalArgumentException("Expense amount must be positive");
        if (paidByUserId == null || paidByUserId.isBlank()) throw new IllegalArgumentException("Payer is required");
        if (groupId == null || groupId.isBlank()) throw new IllegalArgumentException("Group id is required");
        if (splits == null || splits.isEmpty()) throw new IllegalArgumentException("At least one split is required");

        long total = splits.stream().mapToLong(Split::amountInCents).sum();
        if (total != amountInCents) {
            throw new IllegalArgumentException("Splits must add up to the expense amount");
        }

        this.id = id;
        this.description = description;
        this.amountInCents = amountInCents;
        this.paidByUserId = paidByUserId;
        this.splits = List.copyOf(splits);
        this.groupId = groupId;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public long getAmountInCents() {
        return amountInCents;
    }

    public String getPaidByUserId() {
        return paidByUserId;
    }

    public List<Split> getSplits() {
        return splits;
    }

    public String getGroupId() {
        return groupId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
