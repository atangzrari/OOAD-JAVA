package com.banking.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class InvestmentAccountTest {

    @Test
    void rejectsOpeningBalanceBelowMinimum() {
        assertThrows(IllegalArgumentException.class,
                () -> new InvestmentAccount(new BigDecimal("100.00")));
    }

    @Test
    void appliesFivePercentInterestMonthly() {
        InvestmentAccount account = new InvestmentAccount(new BigDecimal("1000.00"));
        account.applyMonthlyInterest();
        assertEquals(0, account.getBalance().compareTo(new BigDecimal("1050.00")));
    }
}

