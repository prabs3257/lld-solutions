package com.example.splitwise.split;

import com.example.splitwise.model.Split;

import java.util.ArrayList;
import java.util.List;

public final class ExactSplitStrategy implements SplitStrategy {
    @Override
    public List<Split> calculate(long total, List<String> userIds, List<Long> values) {
        if (userIds == null || values == null || userIds.size() != values.size()) {
            throw new IllegalArgumentException("Participants and exact amounts must have the same size");
        }

        long sum = 0;
        List<Split> splits = new ArrayList<>();

        for (int i = 0; i < userIds.size(); i++) {
            Long amount = values.get(i);
            if (amount == null || amount < 0) {
                throw new IllegalArgumentException("Exact split amount cannot be negative");
            }

            sum = Math.addExact(sum, amount);
            splits.add(new Split(userIds.get(i), amount));
        }

        if (sum != total) {
            throw new IllegalArgumentException("Exact splits must equal total amount");
        }

        return splits;
    }
}
