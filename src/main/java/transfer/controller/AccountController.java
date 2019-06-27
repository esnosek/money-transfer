package transfer.controller;

import com.google.inject.Inject;
import transfer.dto.AccountDto;
import transfer.model.Account;
import transfer.service.AccountService;
import transfer.service.exceptions.AccountException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountController {

  private final AccountService accountService;

  @Inject
  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @POST
  @Path("/")
  @Valid
  public Response createAccount(@Valid AccountDto accountDto) throws AccountException {
    Account account = accountService.create(accountDto);
    return Response.ok()
        .entity(new AccountDto(account.getAccountId(), account.getBalance()))
        .build();
  }

  @GET
  @Path("/{id}")
  @Valid
  public Response getAccount(
      @NotNull(message = "accountId cannot be null") @PathParam("id") String id)
      throws AccountException {
    Account account = accountService.find(id);
    if (null == account)
      return Response.status(Response.Status.NOT_FOUND.getStatusCode())
          .entity("The user with id: " + id + " is not found")
          .build();
    else
      return Response.ok()
          .entity(new AccountDto(account.getAccountId(), account.getBalance()))
          .build();
  }

  @GET
  @Path("/")
  @Valid
  public Response getAccounts() throws AccountException {
    List<AccountDto> accounts =
        accountService.findAll().stream()
            .map(a -> new AccountDto(a.getAccountId(), a.getBalance()))
            .collect(toList());
    return Response.ok().entity(accounts).build();
  }
}
