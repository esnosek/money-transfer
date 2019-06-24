package transfer.service;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TransferException extends Exception {

    TransferException(String msg){
        super(msg);
    }

    TransferException(String msg, Exception e){
        super(msg, e);
    }
}
