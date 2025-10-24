package model.accounts;

import model.interfaces.AccountOperations;
import model.customers.Customer;

public abstract class Account implements AccountOperations {
    private final String accountNumber;
    private double balance;
    private Customer accountOwner;
    private String branch;
    private String accountType;

    public Account(String accountNumber, Customer accountOwner,
                   double initialBalance, String branch, String accountType) {
        this.accountNumber = accountNumber;
        this.accountOwner = accountOwner;
        this.balance = initialBalance;
        this.branch = branch;
        this.accountType = accountType;
    }

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public Customer getAccountOwner() { return accountOwner; }
    public String getBranch() { return branch; }
    public String getAccountType() { return accountType; }

    @Override
    public double getBalance() {
        return balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public double deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            return balance;
        }
        throw new IllegalArgumentException("Deposit amount must be positive");
    }

    @Override
    public abstract boolean withdraw(double amount);

    @Override
    public String toString() {
        return String.format("%s Account [%s] - Balance: $%.2f",
                accountType, accountNumber, balance);
    }
}