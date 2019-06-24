import lombok.AllArgsConstructor;
import transfer.dto.TransferDto;
import transfer.service.TransferException;
import transfer.service.TransferService;

public class Runner {
    public void start(TransferService service1, TransferDto dto1,
                      TransferService service2, TransferDto dto2){
        new Thread(new TransferRunner(service1, dto1)).run();
        new Thread(new TransferRunner(service2, dto2)).run();
    }
}

@AllArgsConstructor
class TransferRunner implements Runnable{

    private final TransferService transferService;
    private final TransferDto transferDto;
    @Override
    public void run() {
        System.out.println("START : " + Thread.currentThread());
        try {
            transferService.transfer(transferDto);
        } catch (TransferException e) {
            e.printStackTrace();
        }
        System.out.println("STOP : " + Thread.currentThread());
    }
}