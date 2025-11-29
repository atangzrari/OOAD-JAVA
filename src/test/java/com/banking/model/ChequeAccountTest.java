package com.banking.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ChequeAccountTest {

    @Test
    void supportsWithdrawals() {
        ChequeAccount account = new ChequeAccount(new BigDecimal("1000.00"));
        assertTrue(account instanceof Withdrawable);
        ((Withdrawable) account).withdraw(new BigDecimal("250.00"));
        assertEquals(0, account.getBalance().compareTo(new BigDecimal("750.00")));
    }

    @Test
    void preventsOverdraft() {
        ChequeAccount account = new ChequeAccount(new BigDecimal("100.00"));
        assertThrows(IllegalStateException.class,
                () -> ((Withdrawable) account).withdraw(new BigDecimal("200.00")));
    }
}

