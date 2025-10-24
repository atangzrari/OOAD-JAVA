package model.accounts;

import model.interfaces.InterestBearing;
import model.customers.Customer;

public class SavingsAccount extends Account implements InterestBearing {
    private static final double INTEREST_RATE = 0.0005; // 0.05% monthly

    public SavingsAccount(String accountNumber, Customer accountOwner,
                          double initialBalance, String branch) {
        super(accountNumber, accountOwner, initialBalance, branch, "Savings");
    }

    @Override
    public boolean withdraw(double amount) {
        // Savings account does not allow withdrawals according to requirements
        throw new UnsupportedOperationException("Savings account does not allow withdrawals");
    }

    @Override
    public double calculateMonthlyInterest() {
        return getBalance() * INTEREST_RATE;
    }

    public void applyMonthlyInterest() {
        double interest = calculateMonthlyInterest();
        deposit(interest);
    }
}