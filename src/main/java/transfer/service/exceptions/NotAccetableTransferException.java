package transfer.service.exceptions;

public class NotAccetableTransferException extends TransferException {
  public NotAccetableTransferException(String msg) {
    super(msg);
  }

  public NotAccetableTransferException(String msg, Exception e) {
    super(msg, e);
  }
}
