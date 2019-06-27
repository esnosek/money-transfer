package transfer.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static transfer.config.ConnectionPool.getConnection;

class DatabaseSetup {

  static void start() {
    try (Connection conn = getConnection()) {
      dropAll(conn);
      createAccountTable(conn);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static void dropAll(Connection conn) throws SQLException {
    try (Statement stmt = conn.createStatement()) {
      stmt.execute("DROP ALL OBJECTS");
    }
  }

  private static void createAccountTable(Connection conn) throws SQLException {
    try (Statement stmt = conn.createStatement()) {
      stmt.execute("SET DEFAULT_LOCK_TIMEOUT 5000");
      stmt.executeUpdate("CREATE TABLE accounts (accountId VARCHAR(255), balance DECIMAL)");
    }
  }
}
