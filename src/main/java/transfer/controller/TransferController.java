package transfer.controller;

import com.google.inject.Inject;
import transfer.dto.TransferDto;
import transfer.service.TransferException;
import transfer.service.TransferService;

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
public class TransferController {

    private final TransferService transferService;

    @Inject
    public TransferController(TransferService transferService){
        this.transferService = transferService;
    }

    @POST
    @Path("/")
    @Valid
    public Response transfer(@Valid TransferDto transferDto) {
        try {
            transferService.transfer(transferDto);
            return Response.ok("Money transfer completed successfully").build();
        } catch (TransferException e) {
            e.printStackTrace();
            return Response.status(500, e.getMessage()).build();
        }
    }

}
