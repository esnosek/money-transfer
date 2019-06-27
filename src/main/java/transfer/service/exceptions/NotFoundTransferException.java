package transfer.service.exceptions;

public class NotFoundTransferException extends TransferException {
  public NotFoundTransferException(String msg) {
    super(msg);
  }

  public NotFoundTransferException(String msg, Exception e) {
    super(msg, e);
  }
}
