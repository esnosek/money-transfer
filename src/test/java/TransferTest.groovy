import spock.lang.Specification
import transfer.dao.AccountDao
import transfer.dto.TransferDto
import transfer.service.AccountService
import transfer.service.TransferService

import java.sql.Connection
import java.sql.SQLException

class TransferTest extends Specification {

    def "test"(){
        given:
        TransferDto transferDto1 = new TransferDto().builder()
                .fromAccountId("2f03d08b-a30c-4549-9863-b5cd23723721")
                .toAccountId("4244c80e-9d07-4dbe-90cc-44fc46e121cf")
                .amount(new BigDecimal(10))
                .build()

        TransferDto transferDto2 = new TransferDto().builder()
                .toAccountId("2f03d08b-a30c-4549-9863-b5cd23723721")
                .fromAccountId("4244c80e-9d07-4dbe-90cc-44fc46e121cf")
                .amount(new BigDecimal(10))
                .build()

        def accountDaoMock = new AccountDaoMock()
        def transferServiceMock = new TransferService(accountDaoMock)

        def accountDao = new AccountDao()
        def transferService = new TransferService(accountDao)

        def accountService = new AccountService(accountDaoMock)

        def runner = new Runner()

        when:
        runner.start(transferServiceMock, transferDto1, transferService, transferDto2)

        then:
        println accountService.getBalance("2f03d08b-a30c-4549-9863-b5cd23723721")
        println accountService.getBalance("4244c80e-9d07-4dbe-90cc-44fc46e121cf")
        true
    }

    class AccountDaoMock extends AccountDao{
        @Override
        void updateBalance(Connection conn, String id, BigDecimal newBalance) throws SQLException {
            super.updateBalance(conn, id, newBalance)
            Thread.sleep(5000)
        }
    }
}
