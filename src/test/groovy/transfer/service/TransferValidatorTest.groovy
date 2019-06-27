package transfer.service

import spock.lang.Shared
import spock.lang.Specification
import transfer.dao.AccountDao
import transfer.dto.TransferDto
import transfer.model.Account
import transfer.service.exceptions.NotAccetableTransferException
import transfer.service.exceptions.NotFoundTransferException
import transfer.service.exceptions.TransferException

import java.sql.Connection

class TransferValidatorTest extends Specification {

    @Shared String accountToId
    @Shared String accountFromId
    @Shared Account accountFrom
    @Shared Account accountTo
    @Shared TransferDto transferDto
    @Shared Connection conn
    @Shared AccountDao accountDao
    @Shared TransferValidator validator

    def setup(){
        accountToId = "1"
        accountFromId = "2"
        accountFrom = new Account(accountFromId, new BigDecimal(1000))
        accountTo = new Account(accountToId, new BigDecimal(1000))
        transferDto = new TransferDto(accountFromId, accountToId, new BigDecimal("100"))
        conn = Mock(Connection.class)
        accountDao = Mock(AccountDao.class)
    }

    def "Should transfer validator not throw an exception"(){
        setup:
        accountDao.find(conn, accountFromId) >> accountFrom
        accountDao.find(conn, accountToId) >> accountTo
        validator = new TransferValidator()

        when:
        validator.validate(conn, accountDao, transferDto)

        then:
        notThrown TransferException
    }

    def "Should transfer validator throw an exception with Sender is not found message"(){
        setup:
        accountFrom = null
        accountDao.find(conn, accountFromId) >> accountFrom
        accountDao.find(conn, accountToId) >> accountTo
        validator = new TransferValidator()

        when:
        validator.validate(conn, accountDao, transferDto)

        then:
        NotFoundTransferException ex = thrown()
        ex.message == 'Sender is not found'
    }

    def "Should transfer validator throw an exception with Recipient is not found"(){
        setup:
        accountTo = null
        accountDao.find(conn, accountFromId) >> accountFrom
        accountDao.find(conn, accountToId) >> accountTo
        validator = new TransferValidator()

        when:
        validator.validate(conn, accountDao, transferDto)

        then:
        NotFoundTransferException ex = thrown()
        ex.message == 'Recipient is not found'
    }

    def "Should transfer validator throw an exception with Not enough money"(){
        setup:
        transferDto = new TransferDto(accountFromId, accountToId, new BigDecimal("1001"))
        accountDao.find(conn, accountFromId) >> accountFrom
        accountDao.find(conn, accountToId) >> accountTo
        validator = new TransferValidator()

        when:
        validator.validate(conn, accountDao, transferDto)

        then:
        NotAccetableTransferException ex = thrown()
        ex.message == 'Not enough money on the sender account'
    }

    def "Should transfer validator throw an exception if recipient and sender is the same"(){
        setup:
        transferDto = new TransferDto(accountToId, accountToId, new BigDecimal("100"))
        validator = new TransferValidator()

        when:
        validator.validate(conn, accountDao, transferDto)

        then:
        NotAccetableTransferException ex = thrown()
        ex.message == 'Sender and recipient must not be the same'
    }
}
