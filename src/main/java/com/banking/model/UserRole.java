package com.banking.model;

public enum UserRole {
    ADMIN,
    CUSTOMER;

    public static UserRole fromDb(String value) {
        return value == null ? null : UserRole.valueOf(value.toUpperCase());
    }
}

