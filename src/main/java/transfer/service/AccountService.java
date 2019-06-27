package transfer.service;

import com.google.inject.Inject;
import transfer.dao.AccountDao;
import transfer.dto.AccountDto;
import transfer.model.Account;
import transfer.service.exceptions.AccountException;

import java.sql.Connection;
import java.util.List;

import static transfer.config.ConnectionPool.getConnection;

public class AccountService {

  private final AccountDao accountDao;

  @Inject
  public AccountService(AccountDao accountDao) {
    this.accountDao = accountDao;
  }

  public Account create(AccountDto accountDto) throws AccountException {
    try (Connection conn = getConnection()) {
      return accountDao.create(conn, accountDto);
    } catch (Exception e) {
      throw new AccountException("Unexpected exception when creating the account", e);
    }
  }

  public List<Account> findAll() throws AccountException {
    try (Connection conn = getConnection()) {
      return accountDao.findAll(conn);
    } catch (Exception e) {
      throw new AccountException("Unexpected exception when retrieving accounts", e);
    }
  }

  public Account find(String id) throws AccountException {
    try (Connection conn = getConnection()) {
      return accountDao.find(conn, id);
    } catch (Exception e) {
      throw new AccountException("Unexpected exception when retrieving the account: " + id, e);
    }
  }
}
