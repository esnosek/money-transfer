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

    private static final String SELECT_ACCOUNT_SQL =
            "SELECT * FROM accounts WHERE accountId = ?";

    private static final String SELECT_ALL_ACCOUNTS_SQL =
            "SELECT * FROM accounts";

    private static final String SELECT_BALANCE_SQL =
            "SELECT balance FROM accounts WHERE accountId = ?";

    public Account create(Connection conn, AccountDto accountDto) {
        String id = UUID.randomUUID().toString();
        try(PreparedStatement stmt = conn.prepareStatement(INSERT_ACCOUNT_SQL)){
            stmt.setString(1, id);
            stmt.setBigDecimal(2, accountDto.getBalance());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return find(conn, id);
    }

    public void updateBalance(Connection conn, String id, BigDecimal newBalance) throws SQLException {
        try(PreparedStatement stmt = conn.prepareStatement(UPDATE_BALANCE_SQL)){
            stmt.setBigDecimal(1, newBalance);
            stmt.setString(2, id);
            stmt.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<Account> findAll(Connection conn) {
        List<Account> accounts = new LinkedList<>();
        try(PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_ACCOUNTS_SQL)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Account account = Account.builder()
                        .accountId(rs.getString("accountId"))
                        .balance(rs.getBigDecimal("balance"))
                        .build();
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public Account find(Connection conn, String id) {
        Account account = null;
        try(PreparedStatement stmt = conn.prepareStatement(SELECT_ACCOUNT_SQL)){
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                account = Account.builder()
                        .accountId(rs.getString("accountId"))
                        .balance(rs.getBigDecimal("balance"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public BigDecimal getBalance(Connection conn, String id) {
        BigDecimal balance = null;
        try(PreparedStatement stmt = conn.prepareStatement(SELECT_BALANCE_SQL)){
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                balance = rs.getBigDecimal("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }
}
