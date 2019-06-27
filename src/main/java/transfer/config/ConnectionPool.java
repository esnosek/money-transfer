package transfer.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static transfer.config.ApplicationConfig.getConfig;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionPool {

  private static String HOST = getConfig().getString("database.host");
  private static String USER = getConfig().getString("database.user");
  private static int MIN_IDLE = getConfig().getInt("database.min_idle");
  private static int MAX_IDLE = getConfig().getInt("database.max_idle");
  private static int MAX_OPEN_PREPARED_STMT = getConfig().getInt("database.max_open+prepared_stmt");

  private static BasicDataSource ds = new BasicDataSource();

  static {
    ds.setUrl(HOST);
    ds.setUsername(USER);
    ds.setMinIdle(MIN_IDLE);
    ds.setMaxIdle(MAX_IDLE);
    ds.setMaxOpenPreparedStatements(MAX_OPEN_PREPARED_STMT);
  }

  public static Connection getConnection() throws SQLException {
    return ds.getConnection();
  }
}
