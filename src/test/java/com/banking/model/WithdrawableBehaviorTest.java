package com.banking.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class WithdrawableBehaviorTest {

    @Test
    void savingsIsNotWithdrawableButOthersAre() {
        SavingsAccount savings = new SavingsAccount(new BigDecimal("500.00"));
        InvestmentAccount investment = new InvestmentAccount(new BigDecimal("600.00"));
        ChequeAccount cheque = new ChequeAccount(new BigDecimal("700.00"));

        assertFalse(savings instanceof Withdrawable);
        assertTrue(investment instanceof Withdrawable);
        assertTrue(cheque instanceof Withdrawable);
    }
}

