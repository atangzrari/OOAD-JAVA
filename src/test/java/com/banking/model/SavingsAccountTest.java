package com.banking.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountTest {

    @Test
    void savingsAccountAccruesInterestWithoutWithdrawals() {
        SavingsAccount account = new SavingsAccount(new BigDecimal("1000.00"));
        assertFalse(account instanceof Withdrawable);
        account.applyMonthlyInterest();
        assertEquals(0, account.getBalance().compareTo(new BigDecimal("1000.50")));
    }
}

