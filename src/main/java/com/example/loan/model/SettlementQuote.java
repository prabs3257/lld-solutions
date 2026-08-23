package com.example.loan.model;

import java.math.BigDecimal;

public record SettlementQuote(
        BigDecimal principalOutstanding,
        BigDecimal interestOutstanding,
        BigDecimal chargesOutstanding,
        BigDecimal totalSettlementAmount) {
}
