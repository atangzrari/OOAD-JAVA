package model.interfaces;

public interface AccountOperations {
    boolean withdraw(double amount);
    double deposit(double amount);
    double getBalance();
}