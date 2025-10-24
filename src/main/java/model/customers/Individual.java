package model.customers;

import model.accounts.Account;
import model.banking.Bank;

public class Individual extends Customer {
    private String dateOfBirth;
    private String gender;

    public Individual(String name, int customerId, String address,
                      String dateOfBirth, String gender) {
        super(name, customerId, address);
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public String getDateOfBirth() { return dateOfBirth; }
    public String getGender() { return gender; }

    @Override
    public Account openAccount(String accountType, double initialDeposit) {
        // Business logic handled by Bank class
        Account newAccount = Bank.getInstance().createAccount(this, accountType, initialDeposit);
        if (newAccount != null) {
            addAccount(newAccount);
        }
        return newAccount;
    }
}