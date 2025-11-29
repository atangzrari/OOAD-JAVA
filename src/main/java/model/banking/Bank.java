package model.banking;

import model.accounts.*;
import model.customers.Customer;
import java.util.HashMap;
import java.util.Map;

public class Bank {
    private static Bank instance;
    private String name;
    private String address;
    private Map<String, Account> accounts;
    private Map<Integer, Customer> customers;
    private int accountCounter;
    private int customerCounter;

    private Bank() {
        this.name = "MyBank";
        this.address = "123 Main Street";
        this.accounts = new HashMap<>();
        this.customers = new HashMap<>();
        this.accountCounter = 1000;
        this.customerCounter = 100;
    }

    public static Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }

    // Getters
    public String getName() { return name; }
    public String getAddress() { return address; }

    public Account createAccount(Customer customer, String accountType, double initialDeposit) {
        String accountNumber = generateAccountNumber();
        Account newAccount = null;

        try {
            switch (accountType.toLowerCase()) {
                case "savings":
                    newAccount = new SavingsAccount(accountNumber, customer, initialDeposit, "Main Branch");
                    break;
                case "investment":
                    newAccount = new InvestmentAccount(accountNumber, customer, initialDeposit, "Main Branch");
                    break;
                case "checking":
                    newAccount = new CheckingAccount(accountNumber, customer, initialDeposit, "Main Branch");
                    break;
                default:
                    throw new IllegalArgumentException("Unknown account type: " + accountType);
            }

            if (newAccount != null) {
                accounts.put(accountNumber, newAccount);
            }
        } catch (IllegalArgumentException e) {
            throw e; // Re-throw validation exceptions
        }

        return newAccount;
    }

    public Account findAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public void registerCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
    }

    public Customer findCustomer(int customerId) {
        return customers.get(customerId);
    }

    public void processMonthlyInterest() {
        for (Account account : accounts.values()) {
            if (account instanceof SavingsAccount) {
                ((SavingsAccount) account).applyMonthlyInterest();
            } else if (account instanceof InvestmentAccount) {
                ((InvestmentAccount) account).applyMonthlyInterest();
            }
        }
    }

    private String generateAccountNumber() {
        return "ACC" + (accountCounter++);
    }

    public int generateCustomerId() {
        return customerCounter++;
    }
}