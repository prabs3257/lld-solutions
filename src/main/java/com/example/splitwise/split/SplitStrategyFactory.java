package com.example.splitwise.split;

public final class SplitStrategyFactory {
    private SplitStrategyFactory() {}

    public static SplitStrategy getStrategy(SplitType type) {
        if (type == null) throw new IllegalArgumentException("Split type is required");

        return switch (type) {
            case EQUAL -> new EqualSplitStrategy();
            case EXACT -> new ExactSplitStrategy();
            case PERCENTAGE -> new PercentageSplitStrategy();
        };
    }
}
