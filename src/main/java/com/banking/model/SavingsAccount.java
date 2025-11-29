package com.banking.model;

import java.math.BigDecimal;

public class SavingsAccount extends Account implements InterestBearing {

    private static final BigDecimal MONTHLY_INTEREST_RATE = new BigDecimal("0.0005");

    public SavingsAccount() {
        super(AccountType.SAVINGS);
    }

    public SavingsAccount(BigDecimal openingBalance) {
        super(AccountType.SAVINGS, openingBalance);
    }

    @Override
    public void applyMonthlyInterest() {
        BigDecimal interest = getBalance().multiply(MONTHLY_INTEREST_RATE);
        if (interest.compareTo(BigDecimal.ZERO) > 0) {
            deposit(interest);
        }
    }

    @Override
    public BigDecimal getMonthlyInterestRate() {
        return MONTHLY_INTEREST_RATE;
    }
}