package transfer.service;

import com.google.inject.Inject;
import transfer.dao.AccountDao;
import transfer.dto.AccountDto;
import transfer.model.Account;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static transfer.config.ConnectionPool.getConnection;

public class AccountService {

    private final AccountDao accountDao;

    @Inject
    public AccountService(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    public Account create(AccountDto accountDto) {
        Account account = null;
        try(Connection conn = getConnection()){
            account = accountDao.create(conn, accountDto);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public List<Account> findAll() {
        List<Account> accounts = new LinkedList<>();
        try(Connection conn = getConnection()){
            accounts = accountDao.findAll(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public Account find(String id) {
        Account account = null;
        try(Connection conn = getConnection()){
            account = accountDao.find(conn, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public void updateBalance(String id, BigDecimal newBalance){
        try(Connection conn = getConnection()){
            accountDao.updateBalance(conn, id, newBalance);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BigDecimal getBalance(String id) {
        BigDecimal balance = null;
        try(Connection conn = getConnection()){
            balance = accountDao.getBalance(conn, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }
}
