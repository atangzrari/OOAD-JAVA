package model.customers;

import model.accounts.Account;
import model.banking.Bank;

public class Company extends Customer {
    private String companyName;
    private String companyAddress;

    public Company(String name, int customerId, String address,
                   String companyName, String companyAddress) {
        super(name, customerId, address);
        this.companyName = companyName;
        this.companyAddress = companyAddress;
    }

    public String getCompanyName() { return companyName; }
    public String getCompanyAddress() { return companyAddress; }

    @Override
    public Account openAccount(String accountType, double initialDeposit) {
        // Only checking accounts for companies (for salary payments)
        if (!"checking".equalsIgnoreCase(accountType)) {
            throw new IllegalArgumentException("Companies can only open checking accounts for salary payments");
        }

        Account newAccount = Bank.getInstance().createAccount(this, accountType, initialDeposit);
        if (newAccount != null) {
            addAccount(newAccount);
        }
        return newAccount;
    }
}