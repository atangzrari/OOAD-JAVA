package com.banking.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class InterestBearingTest {

    @Test
    void savingsAndInvestmentImplementInterestBearing() {
        SavingsAccount savings = new SavingsAccount(new BigDecimal("1000.00"));
        InvestmentAccount investment = new InvestmentAccount(new BigDecimal("1500.00"));

        assertTrue(savings instanceof InterestBearing);
        assertTrue(investment instanceof InterestBearing);

        savings.applyMonthlyInterest();
        investment.applyMonthlyInterest();

        assertEquals(0, savings.getBalance().compareTo(new BigDecimal("1000.50")));
        assertEquals(0, investment.getBalance().compareTo(new BigDecimal("1575.00")));
    }
}

