package transfer.controller;

import transfer.service.exceptions.NotAccetableTransferException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotAccetableTransferExceptionMapper
    implements ExceptionMapper<NotAccetableTransferException> {

  @Override
  public Response toResponse(NotAccetableTransferException exception) {
    return Response.status(Response.Status.NOT_ACCEPTABLE).entity(exception.getMessage()).build();
  }
}
