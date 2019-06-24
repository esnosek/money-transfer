package transfer.service;

import com.google.inject.Inject;
import lombok.extern.java.Log;
import transfer.dao.AccountDao;
import transfer.dto.TransferDto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import static transfer.config.ConnectionPool.getConnection;

@Log
public class TransferService {

    private final AccountDao accountDao;

    @Inject
    public TransferService(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    public void transfer(TransferDto transferDto) throws TransferException{
        try(Connection conn = getConnection()){
            conn.setAutoCommit(false);
            new TransferValidator().validate(conn, accountDao, transferDto);
            updateAccounts(conn, transferDto);
        } catch (SQLException e) {
            throw new TransferException("Unexpected error when transferring money", e);
        }
    }

    private void updateAccounts(Connection conn, TransferDto transferDto) throws SQLException{
        String fromId = transferDto.getFromAccountId();
        String toId = transferDto.getToAccountId();
        BigDecimal amount = transferDto.getAmount();
        try{
            if(fromId.compareTo(toId) >= 0){
                reduceBalance(conn, fromId, amount);
                log.info("Reduce from : " + fromId + ", amount : " + amount);
                increaseBalance(conn, toId, amount);
                log.info("Increase to : " + toId + ", amount : " + amount);
            }
            else {
                increaseBalance(conn, toId, amount);
                log.info("Increase to : " + toId + ", amount : " + amount);
                reduceBalance(conn, fromId, amount);
                log.info("Reduce from : " + fromId + ", amount : " + amount);
            }
        } catch(SQLException e){
            conn.rollback();
            throw e;
        } finally {
            conn.commit();
            conn.setAutoCommit(true);
        }
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
