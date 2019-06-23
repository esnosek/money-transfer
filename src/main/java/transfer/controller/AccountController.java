package transfer.controller;

import com.google.inject.Inject;
import transfer.dto.AccountDto;
import transfer.model.Account;
import transfer.service.AccountService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountController {

    private final AccountService accountService;

    @Inject
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @POST
    @Path("/")
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = accountService.create(accountDto);
        return AccountDto.builder()
                .accountId(account.getAccountId())
                .balance(accountDto.getBalance())
                .build();
    }

    @GET
    @Path("/{id}")
    public AccountDto getAccount(@PathParam("id") String id) {
        Account account = accountService.find(id);
        return AccountDto.builder()
                .accountId(account.getAccountId())
                .balance(account.getBalance())
                .build();
    }

    @GET
    @Path("/")
    public List<AccountDto> getAccounts() {
        List<Account> accounts = accountService.findAll();
        return accounts.stream()
                .map(a -> AccountDto.builder()
                    .accountId(a.getAccountId())
                    .balance(a.getBalance())
                    .build())
                .collect(Collectors.toList());
    }

}
