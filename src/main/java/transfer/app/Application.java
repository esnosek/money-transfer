package transfer.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import transfer.config.ApplicationConfig;
import transfer.config.TransferModule;
import transfer.controller.AccountController;
import transfer.controller.TransferController;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static transfer.config.ConnectionPool.getConnection;

public class Application {

    public static void main(String[] args) throws LifecycleException{
        Injector injector = initialize();
        setupDB();
        startTomcat(injector);
    }

    private static void setupDB(){
        try(Connection conn = getConnection();
            Statement stmt = conn.createStatement()
        ){
            stmt.execute("DROP ALL OBJECTS");
            stmt.execute("SET DEFAULT_LOCK_TIMEOUT 10000");

            stmt.executeUpdate(
                    "CREATE TABLE accounts (" +
                            "accountId VARCHAR(255)," +
                            "balance DECIMAL)"
            );
            stmt.executeUpdate(
                    "INSERT INTO accounts (accountId, balance) VALUES (" +
                            "'2f03d08b-a30c-4549-9863-b5cd23723721', 100)"
            );
            stmt.executeUpdate(
                    "INSERT INTO accounts (accountId, balance) VALUES " +
                            "('4244c80e-9d07-4dbe-90cc-44fc46e121cf', 200)"
            );

            ResultSet rs = stmt.executeQuery("SELECT * FROM accounts");
            while(rs.next()) {
                String accountId = rs.getString("accountId");
                BigDecimal balance = rs.getBigDecimal("balance");
                System.out.println("accountId : " + accountId + ", balance : " + balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Injector initialize(){
        return Guice.createInjector(new TransferModule());
    }

    private static void startTomcat(Injector injector) throws LifecycleException{
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();

        Context context = tomcat.addWebapp("", new File(".").getAbsolutePath());
        Tomcat.addServlet(context, "Transfer API", servlet(injector));
        context.addServletMappingDecoded("/api/*", "Transfer API");

        tomcat.start();
        tomcat.getServer().await();
    }

    private static ServletContainer servlet(Injector injector) {
        return new ServletContainer(ResourceConfig.forApplicationClass(ApplicationConfig.class)
                .register(injector.getInstance(AccountController.class))
                .register(injector.getInstance(TransferController.class)));
    }
}