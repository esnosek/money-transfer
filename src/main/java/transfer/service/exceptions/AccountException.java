package transfer.service.exceptions;

public class AccountException extends Exception {

  public AccountException(String msg) {
    super(msg);
  }

  public AccountException(String msg, Exception e) {
    super(msg, e);
  }
}
