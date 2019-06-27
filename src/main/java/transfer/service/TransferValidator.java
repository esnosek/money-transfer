package transfer.service;

import lombok.NoArgsConstructor;
import transfer.dao.AccountDao;
import transfer.dto.TransferDto;
import transfer.model.Account;
import transfer.service.exceptions.NotAccetableTransferException;
import transfer.service.exceptions.NotFoundTransferException;
import transfer.service.exceptions.TransferException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

@NoArgsConstructor
class TransferValidator {

  void validate(Connection conn, AccountDao accountDao, TransferDto transferDto)
      throws TransferException, SQLException {
    String fromId = transferDto.getFromAccountId();
    String toId = transferDto.getToAccountId();
    BigDecimal amount = transferDto.getAmount();

    if (transferDto.getFromAccountId().equals(transferDto.getToAccountId()))
      throw new NotAccetableTransferException("Sender and recipient must not be the same");

    Account accountFrom = accountDao.find(conn, fromId);
    Account accountTo = accountDao.find(conn, toId);

    if (null == accountFrom) throw new NotFoundTransferException("Sender is not found");
    if (null == accountTo) throw new NotFoundTransferException("Recipient is not found");

    if (accountFrom.getBalance() != null
        && accountFrom.getBalance().subtract(amount).doubleValue() < 0)
      throw new NotAccetableTransferException("Not enough money on the sender account");
  }
}
