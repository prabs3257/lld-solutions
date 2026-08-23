package com.example.splitwise.split;

import com.example.splitwise.model.Split;

import java.util.List;

public interface SplitStrategy {
    List<Split> calculate(long totalAmountInCents, List<String> userIds, List<Long> values);
}
