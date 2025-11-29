package model.banking;

import model.customers.Customer;
import java.util.HashMap;
import java.util.Map;

public class Authentication {
    private Map<String, String> userCredentials; // username -> password
    private Map<String, Customer> userCustomers; // username -> customer

    public Authentication() {
        this.userCredentials = new HashMap<>();
        this.userCustomers = new HashMap<>();
        initializeDefaultUsers();
    }

    private void initializeDefaultUsers() {
        // Add some default users for testing
        userCredentials.put("admin", "admin123");
        userCredentials.put("customer1", "pass123");
    }

    public boolean authenticate(String username, String password) {
        String storedPassword = userCredentials.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    public void registerUser(String username, String password, Customer customer) {
        if (userCredentials.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        userCredentials.put(username, password);
        userCustomers.put(username, customer);
    }

    public Customer getCustomer(String username) {
        return userCustomers.get(username);
    }
}