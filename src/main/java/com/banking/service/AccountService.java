package com.banking.service;

import com.banking.dao.AccountDAO;
import com.banking.dao.CustomerDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.AccountFactory;
import com.banking.model.AccountType;
import com.banking.model.Customer;
import com.banking.model.InterestBearing;
import com.banking.model.Transaction;
import com.banking.model.Withdrawable;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {

    private final AccountDAO accountDAO = new AccountDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final AuditService auditService = new AuditService();

    public Account openAccount(long customerId,
                               AccountType type,
                               BigDecimal initialDeposit,
                               String branch,
                               long actingUserId) {
        Customer customer = customerDAO.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        if (type == null) {
            throw new IllegalArgumentException("Account type required");
        }

        if (initialDeposit == null || initialDeposit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Initial deposit must be positive");
        }

        if (type == AccountType.CHEQUE && !customer.isEmployed()) {
            throw new IllegalArgumentException("Cheque accounts require employment details");
        }
        if (type == AccountType.CHEQUE &&
                (isBlank(customer.getEmployerName()) || isBlank(customer.getEmployerAddress()))) {
            throw new IllegalArgumentException("Employer name and address required for cheque accounts");
        }

        Account account = AccountFactory.createNew(type, initialDeposit);
        account.setCustomerId(customerId);
        account.setBranch(branch == null ? null : branch.trim());
        accountDAO.save(account);

        if (initialDeposit != null && initialDeposit.compareTo(BigDecimal.ZERO) > 0) {
            recordTransaction(account, "DEPOSIT", initialDeposit, "Initial deposit");
        }

        auditService.record(actingUserId, "Opened %s account for customer %d"
                .formatted(type.name(), customerId));
        return account;
    }

    public Account deposit(String accountNumber, BigDecimal amount, long actingUserId) {
        Account account = accountDAO.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.deposit(amount);
        accountDAO.updateBalance(account.getId(), account.getBalance());
        recordTransaction(account, "DEPOSIT", amount, "Deposit");
        auditService.record(actingUserId, "Deposit into account %s".formatted(accountNumber));
        return account;
    }

    public Account withdraw(String accountNumber, BigDecimal amount, long actingUserId) {
        Account account = accountDAO.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        if (!(account instanceof Withdrawable withdrawable)) {
            throw new UnsupportedOperationException("Withdrawals not allowed for this account type");
        }
        withdrawable.withdraw(amount);
        accountDAO.updateBalance(account.getId(), account.getBalance());
        recordTransaction(account, "WITHDRAWAL", amount, "Withdrawal");
        auditService.record(actingUserId, "Withdrawal from account %s".formatted(accountNumber));
        return account;
    }

    public Account applyMonthlyInterest(String accountNumber, long actingUserId) {
        Account account = accountDAO.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        if (!(account instanceof InterestBearing interestBearing)) {
            throw new UnsupportedOperationException("Account does not earn interest");
        }
        BigDecimal before = account.getBalance();
        interestBearing.applyMonthlyInterest();
        BigDecimal interestAmount = account.getBalance().subtract(before);
        accountDAO.updateBalance(account.getId(), account.getBalance());
        if (interestAmount.compareTo(BigDecimal.ZERO) > 0) {
            recordTransaction(account, "INTEREST", interestAmount, "Monthly interest");
        }
        auditService.record(actingUserId, "Applied interest to account %s".formatted(accountNumber));
        return account;
    }

    public List<Account> getAccountsForCustomer(long customerId) {
        return accountDAO.findByCustomerId(customerId);
    }

    public List<Account> getAllAccounts() {
        return accountDAO.findAll();
    }

    public List<Transaction> getTransactionsForCustomer(long customerId) {
        return transactionDAO.findByCustomer(customerId);
    }

    public List<Transaction> getTransactionsForAccount(long accountId) {
        return transactionDAO.findByAccount(accountId);
    }

    private void recordTransaction(Account account, String type, BigDecimal amount, String description) {
        Transaction transaction = new Transaction(account.getId(), type, amount, description);
        transactionDAO.record(transaction);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}

