package com.example.loan.model;

import com.example.loan.enums.ChargeType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Charge {

    private final String chargeId;
    private final ChargeType type;
    private final BigDecimal amount;
    private final LocalDate appliedDate;

    public Charge(String chargeId, ChargeType type,
                  BigDecimal amount, LocalDate appliedDate) {
        this.chargeId = chargeId;
        this.type = type;
        this.amount = amount;
        this.appliedDate = appliedDate;
    }

    public String getChargeId() { return chargeId; }
    public ChargeType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getAppliedDate() { return appliedDate; }
}
