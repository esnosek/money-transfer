package transfer.controller;

import transfer.service.exceptions.AccountException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccountExceptionMapper implements ExceptionMapper<AccountException> {
  @Override
  public Response toResponse(AccountException exception) {
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(exception.getMessage())
        .build();
  }
}
