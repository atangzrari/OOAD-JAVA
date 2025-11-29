package com.banking.service;

import com.banking.dao.DatabaseConnection;
import com.banking.model.Account;
import com.banking.model.AccountType;
import com.banking.model.Customer;
import com.banking.model.Transaction;
import com.banking.service.AuthService.AuthenticatedUser;
import com.banking.service.CustomerService.CustomerRegistrationRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceIntegrationTest {

    private static final CustomerService customerService = new CustomerService();
    private static final AccountService accountService = new AccountService();
    private static final AuthService authService = new AuthService();

    @BeforeAll
    static void setupDatabase() {
        System.setProperty("banking.db.url", "jdbc:sqlite::memory:");
        DatabaseConnection.initializeDatabase();
    }

    @AfterAll
    static void tearDown() {
        DatabaseConnection.closeConnection();
    }

    @Test
    void customerLifecyclePersistsAccountsTransactionsAndAudit() {
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "jane.doe",
                "secret",
                "Jane",
                "Doe",
                "123 Main St",
                true,
                "TechCorp",
                "456 Business Rd"
        );
        Customer customer = customerService.registerCustomer(request);

        AuthenticatedUser auth = authService.authenticate("jane.doe", "secret");
        Account investment = accountService.openAccount(
                customer.getId(),
                AccountType.INVESTMENT,
                new BigDecimal("1000.00"),
                "Headquarters",
                auth.user().getId()
        );

        accountService.deposit(investment.getAccountNumber(), new BigDecimal("200.00"), auth.user().getId());
        accountService.withdraw(investment.getAccountNumber(), new BigDecimal("150.00"), auth.user().getId());
        accountService.applyMonthlyInterest(investment.getAccountNumber(), auth.user().getId());

        List<Account> accounts = accountService.getAccountsForCustomer(customer.getId());
        assertEquals(1, accounts.size());

        List<Transaction> transactions = accountService.getTransactionsForCustomer(customer.getId());
        // initial deposit + manual deposit + withdrawal + interest
        assertEquals(4, transactions.size());
        assertEquals("INTEREST", transactions.get(0).getType());
    }

    @Test
    void chequeAccountRequiresEmployment() {
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "john.unemployed",
                "secret",
                "John",
                "Idle",
                "Unknown",
                false,
                "",
                ""
        );
        Customer customer = customerService.registerCustomer(request);
        AuthenticatedUser auth = authService.authenticate("john.unemployed", "secret");

        assertThrows(IllegalArgumentException.class, () ->
                accountService.openAccount(
                        customer.getId(),
                        AccountType.CHEQUE,
                        new BigDecimal("1000.00"),
                        "Branch",
                        auth.user().getId()
                ));
    }
}

