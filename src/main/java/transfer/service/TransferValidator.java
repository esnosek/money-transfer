package transfer.service;

import lombok.NoArgsConstructor;
import transfer.dao.AccountDao;
import transfer.dto.TransferDto;
import transfer.model.Account;

import java.math.BigDecimal;
import java.sql.Connection;

@NoArgsConstructor
class TransferValidator {

    void validate(Connection conn, AccountDao accountDao, TransferDto transferDto) throws TransferException {
        String fromId = transferDto.getFromAccountId();
        String toId = transferDto.getToAccountId();
        BigDecimal amount = transferDto.getAmount();

        Account accountFrom = accountDao.find(conn, fromId);
        Account accountTo = accountDao.find(conn, toId);

        if(null == accountFrom)
            throw new TransferException("Sender is not found");
        if(null == accountTo)
            throw new TransferException("Recipient is not found");
        if (accountFrom.getBalance() != null && accountFrom.getBalance().subtract(amount).doubleValue() < 0)
            throw new TransferException("Not enough money on the sender account");
    }
}
