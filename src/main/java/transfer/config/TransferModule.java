package transfer.config;

import com.google.inject.AbstractModule;
import transfer.controller.AccountController;
import transfer.controller.TransferController;
import transfer.dao.AccountDao;
import transfer.service.AccountService;
import transfer.service.TransferService;

public class TransferModule extends AbstractModule {

    @Override
    protected void configure() {
        AccountDao accountDao = new AccountDao();
        bind(AccountDao.class).toInstance(accountDao);

        TransferService transferService = new TransferService(accountDao);
        bind(TransferService.class).toInstance(transferService);
        bind(TransferController.class).toInstance(new TransferController(transferService));

        AccountService accountService = new AccountService(accountDao);
        bind(AccountController.class).toInstance(new AccountController(accountService));
    }

}
