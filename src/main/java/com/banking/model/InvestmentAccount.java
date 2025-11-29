package com.banking.model;

import java.math.BigDecimal;

public class InvestmentAccount extends Account implements Withdrawable, InterestBearing {

    private static final BigDecimal MONTHLY_INTEREST_RATE = new BigDecimal("0.05");
    private static final BigDecimal MIN_OPENING_BALANCE = new BigDecimal("500.00");

    public InvestmentAccount() {
        super(AccountType.INVESTMENT);
    }

    public InvestmentAccount(BigDecimal openingBalance) {
        super(AccountType.INVESTMENT, openingBalance);
        ensureMinimum(openingBalance);
    }

    private void ensureMinimum(BigDecimal amount) {
        if (amount == null || amount.compareTo(MIN_OPENING_BALANCE) < 0) {
            throw new IllegalArgumentException("Investment account requires minimum opening deposit of 500.00 BWP");
        }
    }

    @Override
    public void withdraw(BigDecimal amount) {
        validateAmount(amount);
        if (getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        setBalance(getBalance().subtract(amount));
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

    public static BigDecimal getMinimumOpeningBalance() {
        return MIN_OPENING_BALANCE;
    }
}