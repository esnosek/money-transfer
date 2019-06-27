package transfer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Account {
  private final String accountId;
  private final BigDecimal balance;
}
