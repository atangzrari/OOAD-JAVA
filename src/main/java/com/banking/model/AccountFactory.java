package com.banking.model;

import java.math.BigDecimal;

public final class AccountFactory {

    private AccountFactory() {
    }

    public static Account createNew(AccountType type, BigDecimal openingBalance) {
        return switch (type) {
            case SAVINGS -> new SavingsAccount(openingBalance);
            case INVESTMENT -> new InvestmentAccount(openingBalance);
            case CHEQUE -> new ChequeAccount(openingBalance);
        };
    }

    public static Account rehydrate(AccountType type) {
        return switch (type) {
            case SAVINGS -> new SavingsAccount();
            case INVESTMENT -> new InvestmentAccount();
            case CHEQUE -> new ChequeAccount();
        };
    }
}

