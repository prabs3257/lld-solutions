package com.example.splitwise.split;

import com.example.splitwise.model.Split;

import java.util.ArrayList;
import java.util.List;

public final class EqualSplitStrategy implements SplitStrategy {
    @Override
    public List<Split> calculate(long total, List<String> userIds, List<Long> ignored) {
        if (userIds == null || userIds.isEmpty()) {
            throw new IllegalArgumentException("Participants are required");
        }

        long base = total / userIds.size();
        long remainder = total % userIds.size();

        List<Split> splits = new ArrayList<>();
        for (int i = 0; i < userIds.size(); i++) {
            long amount = base + (i < remainder ? 1 : 0);
            splits.add(new Split(userIds.get(i), amount));
        }
        return splits;
    }
}
