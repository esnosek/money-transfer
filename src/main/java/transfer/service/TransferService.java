package transfer.service;

import com.google.inject.Inject;
import transfer.dao.AccountDao;
import transfer.dto.TransferDto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import static transfer.config.ConnectionPool.getConnection;

public class TransferService {

    private final AccountDao accountDao;

    @Inject
    public TransferService(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    public boolean transfer(TransferDto transferDto){
        try(Connection conn = getConnection()){
            conn.setAutoCommit(false);
            try{
                TransferValidator validator = new TransferValidator();
                if(!validator.validate(conn, accountDao, transferDto))
                    return false;

                String fromId = transferDto.getFromAccountId();
                String toId = transferDto.getToAccountId();
                BigDecimal amount = transferDto.getAmount();

                if(fromId.compareTo(toId) >= 0){
                    reduceBalance(conn, fromId, amount);
                    System.out.println("Reduce from : " + fromId + ", amount : " + amount);
                    increaseBalance(conn, toId, amount);
                    System.out.println("Increase to : " + toId + ", amount : " + amount);
                }
                else {
                    increaseBalance(conn, toId, amount);
                    System.out.println("Increase to : " + toId + ", amount : " + amount);
                    reduceBalance(conn, fromId, amount);
                    System.out.println("Reduce from : " + fromId + ", amount : " + amount);
                }
            } catch(SQLException e){
                e.printStackTrace();
                conn.rollback();
            } finally {
                conn.commit();
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void reduceBalance(Connection conn, String id, BigDecimal amount) throws SQLException {
        BigDecimal newBalance = accountDao.getBalance(conn, id).subtract(amount);
        accountDao.updateBalance(conn, id, newBalance);
    }

    private void increaseBalance(Connection conn, String id, BigDecimal amount) throws SQLException{
        BigDecimal newBalance = accountDao.getBalance(conn, id).add(amount);
        accountDao.updateBalance(conn, id, newBalance);
    }
}
