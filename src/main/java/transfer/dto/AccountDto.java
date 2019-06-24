package transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    private String accountId;

    @NotNull(message="Balance must not be null")
    @DecimalMin(value="0.00",  message = "Balance must not be negative number")
    @DecimalMax(value="1000000000.00",  message = "Balance is too large number")
    @Digits(integer = 10, fraction = 2, message = "Incorrect balance value")
    private BigDecimal balance;
}
