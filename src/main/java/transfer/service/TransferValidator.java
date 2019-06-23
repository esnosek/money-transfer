package transfer.service;

import lombok.NoArgsConstructor;
import transfer.dao.AccountDao;
import transfer.dto.TransferDto;
import transfer.model.Account;

import java.math.BigDecimal;
import java.sql.Connection;

@NoArgsConstructor
class TransferValidator {

    boolean validate(Connection conn, AccountDao accountDao, TransferDto transferDto) {
        String fromId = transferDto.getFromAccountId();
        String toId = transferDto.getToAccountId();
        BigDecimal amount = transferDto.getAmount();

        Account accountFrom = accountDao.find(conn, fromId);
        Account accountTo = accountDao.find(conn, toId);

        if(null == accountFrom)
            return false;
        if(null == accountTo)
            return false;
        if(amount != null && amount.doubleValue() <= 0)
            return false;
        if(accountFrom.getBalance() != null && amount != null &&
                accountFrom.getBalance().subtract(amount).doubleValue() < 0)
            return false;

        return true;
    }
}
