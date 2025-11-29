package com.banking.service;

import com.banking.dao.CustomerDAO;
import com.banking.dao.UserDAO;
import com.banking.model.Customer;
import com.banking.model.User;
import com.banking.model.UserRole;
import com.banking.util.PasswordHasher;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    public AuthenticatedUser authenticate(String username, String password) {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!PasswordHasher.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        Customer customer = null;
        if (user.getRole() == UserRole.CUSTOMER) {
            customer = customerDAO.findByUserId(user.getId())
                    .orElseThrow(() -> new IllegalStateException("Customer profile missing for user"));
        }
        return new AuthenticatedUser(user, customer);
    }

    public record AuthenticatedUser(User user, Customer customer) { }
}

