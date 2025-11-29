package com.banking.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public abstract class Account {
    private Long id;
    private final AccountType type;
    private String accountNumber;
    private long customerId;
    private BigDecimal balance;
    private String branch;

    protected Account(AccountType type) {
        this.type = Objects.requireNonNull(type, "type");
        this.balance = BigDecimal.ZERO;
        this.accountNumber = UUID.randomUUID().toString();
    }

    protected Account(AccountType type, BigDecimal openingBalance) {
        this(type);
        if (openingBalance != null) {
            this.balance = openingBalance;
        }
    }

    public AccountType getType() {
        return type;
    }

    public void deposit(BigDecimal amount) {
        validateAmount(amount);
        this.balance = this.balance.add(amount);
    }

    protected void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}