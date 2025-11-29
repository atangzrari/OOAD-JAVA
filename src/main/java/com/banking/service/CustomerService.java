package com.banking.service;

import com.banking.dao.CustomerDAO;
import com.banking.dao.UserDAO;
import com.banking.model.Customer;
import com.banking.model.User;
import com.banking.model.UserRole;
import com.banking.util.PasswordHasher;

import java.util.List;

public class CustomerService {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final UserDAO userDAO = new UserDAO();

    public Customer registerCustomer(CustomerRegistrationRequest request) {
        if (request.username().isBlank() || request.password().isBlank()) {
            throw new IllegalArgumentException("Username and password are required");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPasswordHash(PasswordHasher.hash(request.password()));
        user.setRole(UserRole.CUSTOMER);
        userDAO.save(user);

        Customer customer = new Customer();
        customer.setUserId(user.getId());
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setAddress(request.address());
        customer.setEmployed(request.employed());
        customer.setEmployerName(request.employerName());
        customer.setEmployerAddress(request.employerAddress());

        return customerDAO.save(customer);
    }

    public List<Customer> listCustomers() {
        return customerDAO.findAll();
    }

    public Customer getCustomer(long id) {
        return customerDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
    }

    public void updateCustomer(Customer customer) {
        customerDAO.update(customer);
    }

    public record CustomerRegistrationRequest(
            String username,
            String password,
            String firstName,
            String lastName,
            String address,
            boolean employed,
            String employerName,
            String employerAddress) {
    }
}
