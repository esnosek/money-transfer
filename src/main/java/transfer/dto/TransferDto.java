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
public class TransferDto {

    @NotNull(message="From account must not be null")
    private String fromAccountId;

    @NotNull(message="To account must not be null")
    private String toAccountId;

    @NotNull(message="Transfer amount cannot be null")
    @DecimalMin(value="0.01",  message = "Transfer value should be positive number")
    @DecimalMax(value="1000000.00",  message = "Transfer value is must not be larger than one million")
    @Digits(integer = 7, fraction = 2, message = "Incorrect balance value")
    private BigDecimal amount;
}
