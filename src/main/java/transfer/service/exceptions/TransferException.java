package transfer.service.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TransferException extends Exception {

  public TransferException(String msg) {
    super(msg);
  }

  public TransferException(String msg, Exception e) {
    super(msg, e);
  }
}
