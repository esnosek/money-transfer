package transfer.controller;

import transfer.service.exceptions.NotFoundTransferException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundTransferExceptionMapper implements ExceptionMapper<NotFoundTransferException> {
  @Override
  public Response toResponse(NotFoundTransferException exception) {
    return Response.status(404).entity(exception.getMessage()).build();
  }
}
