package transfer.controller;

import com.google.inject.Inject;
import lombok.extern.java.Log;
import transfer.dto.TransferDto;
import transfer.service.TransferService;
import transfer.service.exceptions.TransferException;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/transfer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log
public class TransferController {

  private final TransferService transferService;

  @Inject
  public TransferController(TransferService transferService) {
    this.transferService = transferService;
  }

  @POST
  @Path("/")
  @Valid
  public Response transfer(@Valid TransferDto transferDto) throws TransferException {
    transferService.transfer(transferDto);
    log.info("Money transfer completed successfully");
    return Response.ok("Money transfer completed successfully").build();
  }
}
