package model.accounts;

import model.interfaces.InterestBearing;
import model.customers.Customer;

public class InvestmentAccount extends Account implements InterestBearing {
    private static final double INTEREST_RATE = 0.05; // 5% monthly
    private static final double MIN_OPENING_BALANCE = 500.00;

    public InvestmentAccount(String accountNumber, Customer accountOwner,
                             double initialBalance, String branch) {
        super(accountNumber, accountOwner, initialBalance, branch, "Investment");

        // Validate minimum opening balance
        if (initialBalance < MIN_OPENING_BALANCE) {
            throw new IllegalArgumentException(
                    String.format("Investment account requires minimum opening balance of $%.2f",
                            MIN_OPENING_BALANCE));
        }
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= getBalance()) {
            setBalance(getBalance() - amount);
            return true;
        }
        return false;
    }

    @Override
    public double calculateMonthlyInterest() {
        return getBalance() * INTEREST_RATE;
    }

    public void applyMonthlyInterest() {
        double interest = calculateMonthlyInterest();
        deposit(interest);
    }

    public static double getMinOpeningBalance() {
        return MIN_OPENING_BALANCE;
    }
}