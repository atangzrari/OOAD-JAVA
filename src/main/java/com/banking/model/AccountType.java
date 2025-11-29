package com.banking.model;

public enum AccountType {
    SAVINGS,
    INVESTMENT,
    CHEQUE;

    public static AccountType fromDb(String value) {
        for (AccountType type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown account type: " + value);
    }
}

