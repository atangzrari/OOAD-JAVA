package model.customers;

import model.accounts.Account;
import model.banking.Bank;

public class Organization extends Customer {
    private String organizationType;

    public Organization(String name, int customerId, String address, String organizationType) {
        super(name, customerId, address);
        this.organizationType = organizationType;
    }

    public String getOrganizationType() { return organizationType; }

    @Override
    public Account openAccount(String accountType, double initialDeposit) {
        Account newAccount = Bank.getInstance().createAccount(this, accountType, initialDeposit);
        if (newAccount != null) {
            addAccount(newAccount);
        }
        return newAccount;
    }
}