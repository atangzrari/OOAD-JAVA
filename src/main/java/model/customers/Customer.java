package model.customers;

import model.interfaces.CustomerOperations;
import model.accounts.Account;
import java.util.ArrayList;
import java.util.List;

public abstract class Customer implements CustomerOperations {
    private String name;
    private int customerId;
    private String address;
    private List<Account> accounts;

    public Customer(String name, int customerId, String address) {
        this.name = name;
        this.customerId = customerId;
        this.address = address;
        this.accounts = new ArrayList<>();
    }

    // Getters
    public String getName() { return name; }
    public int getCustomerId() { return customerId; }
    public String getAddress() { return address; }

    @Override
    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    protected void addAccount(Account account) {
        if (account != null) {
            accounts.add(account);
        }
    }

    @Override
    public abstract Account openAccount(String accountType, double initialDeposit);
}