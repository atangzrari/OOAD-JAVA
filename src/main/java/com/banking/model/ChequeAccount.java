package com.banking.model;

import java.math.BigDecimal;

public class ChequeAccount extends Account implements Withdrawable {

    public ChequeAccount() {
        super(AccountType.CHEQUE);
    }

    public ChequeAccount(BigDecimal openingBalance) {
        super(AccountType.CHEQUE, openingBalance);
    }

    @Override
    public void withdraw(BigDecimal amount) {
        validateAmount(amount);
        if (getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        setBalance(getBalance().subtract(amount));
    }
}