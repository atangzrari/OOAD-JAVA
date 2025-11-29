package com.banking.model;

import java.math.BigDecimal;

public class Transaction {

    private Long id;
    private long accountId;
    private String type;
    private BigDecimal amount;
    private String description;
    private String timestamp;

    public Transaction() {
    }

    public Transaction(long accountId, String type, BigDecimal amount, String description) {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
        }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        }
    }
