package model.interfaces;

import model.accounts.Account;
import java.util.List;

public interface CustomerOperations {
    Account openAccount(String accountType, double initialDeposit);
    List<Account> getAccounts();
}