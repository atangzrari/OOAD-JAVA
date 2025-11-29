package model.accounts;

import model.customers.Customer;

public class CheckingAccount extends Account {

    public CheckingAccount(String accountNumber, Customer accountOwner,
                           double initialBalance, String branch) {
        super(accountNumber, accountOwner, initialBalance, branch, "Checking");
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= getBalance()) {
            setBalance(getBalance() - amount);
            return true;
        }
        return false;
    }
}