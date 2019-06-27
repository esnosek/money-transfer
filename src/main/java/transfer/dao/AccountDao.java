package transfer.dao;

import transfer.dto.AccountDto;
import transfer.model.Account;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class AccountDao {

  private static final String INSERT_ACCOUNT_SQL =
      "INSERT INTO accounts (accountId, balance) VALUES (?,?)";

  private static final String UPDATE_BALANCE_SQL =
      "UPDATE accounts SET balance = ? WHERE accountId = ?";

  private static final String SELECT_ACCOUNT_SQL = "SELECT * FROM accounts WHERE accountId = ?";

  private static final String SELECT_ALL_ACCOUNTS_SQL = "SELECT * FROM accounts";

  private static final String SELECT_BALANCE_SQL =
      "SELECT balance FROM accounts WHERE accountId = ?";

  public Account create(Connection conn, AccountDto accountDto) throws SQLException {
    String id = UUID.randomUUID().toString();
    try (PreparedStatement stmt = conn.prepareStatement(INSERT_ACCOUNT_SQL)) {
      stmt.setString(1, id);
      stmt.setBigDecimal(2, accountDto.getBalance());
      stmt.execute();
    }
    return find(conn, id);
  }

  public void updateBalance(Connection conn, String id, BigDecimal newBalance) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(UPDATE_BALANCE_SQL)) {
      stmt.setBigDecimal(1, newBalance);
      stmt.setString(2, id);
      stmt.execute();
    }
  }

  public List<Account> findAll(Connection conn) throws SQLException {
    List<Account> accounts = new LinkedList<>();
    try (PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_ACCOUNTS_SQL)) {
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String accountId = rs.getString("accountId");
        BigDecimal decimal = rs.getBigDecimal("balance");
        accounts.add(new Account(accountId, decimal));
      }
    }
    return accounts;
  }

  public Account find(Connection conn, String id) throws SQLException {
    Account account = null;
    try (PreparedStatement stmt = conn.prepareStatement(SELECT_ACCOUNT_SQL)) {
      stmt.setString(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        String accountId = rs.getString("accountId");
        BigDecimal decimal = rs.getBigDecimal("balance");
        account = new Account(accountId, decimal);
      }
    }
    return account;
  }

  public BigDecimal getBalance(Connection conn, String id) throws SQLException {
    BigDecimal balance = null;
    try (PreparedStatement stmt = conn.prepareStatement(SELECT_BALANCE_SQL)) {
      stmt.setString(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) balance = rs.getBigDecimal("balance");
    }
    return balance;
  }
}
