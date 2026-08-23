package com.example.splitwise.split;

import com.example.splitwise.model.Split;

import java.util.ArrayList;
import java.util.List;

public final class PercentageSplitStrategy implements SplitStrategy {
    @Override
    public List<Split> calculate(long total, List<String> userIds, List<Long> percentages) {
        if (userIds == null || percentages == null || userIds.size() != percentages.size()) {
            throw new IllegalArgumentException("Participants and percentages must have the same size");
        }
        if (userIds.isEmpty()) {
            throw new IllegalArgumentException("Participants are required");
        }

        long percentageSum = 0;
        for (Long percentage : percentages) {
            if (percentage == null || percentage < 0) {
                throw new IllegalArgumentException("Percentage cannot be negative");
            }
            percentageSum = Math.addExact(percentageSum, percentage);
        }

        if (percentageSum != 100) {
            throw new IllegalArgumentException("Percentages must sum to 100");
        }

        List<Split> splits = new ArrayList<>();
        long allocated = 0;

        for (int i = 0; i < userIds.size(); i++) {
            long amount = (total * percentages.get(i)) / 100;
            splits.add(new Split(userIds.get(i), amount));
            allocated += amount;
        }

        long remainder = total - allocated;
        for (int i = 0; remainder > 0; i = (i + 1) % splits.size(), remainder--) {
            Split current = splits.get(i);
            splits.set(i, new Split(current.userId(), current.amountInCents() + 1));
        }

        return splits;
    }
}
