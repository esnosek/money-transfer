package transfer.controller;

import com.google.inject.Inject;
import transfer.dto.TransferDto;
import transfer.service.TransferService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
    @Path("")
    public boolean transfer(TransferDto transferDto) {
        return transferService.transfer(transferDto);
    }

}
