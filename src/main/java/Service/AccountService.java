package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    AccountDAO accountDAO;

       public AccountService(){
        accountDAO = new AccountDAO();
    }
    
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public List<Account> getAllAccounts() {
        List<Account> data = accountDAO.getAllAccounts();
        return data;
    }

    public Account addAccount(Account account) {
        if (account.getPassword().length() < 4 || account.getUsername() == "")
            return null;
        else
            return accountDAO.insertAccount(account);
    }

    public Account login(Account account) {
        return accountDAO.login(account);
    }
}
