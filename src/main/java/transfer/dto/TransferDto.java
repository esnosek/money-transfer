package transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferDto {
    @JsonProperty(required = true)
    private String fromAccountId;

    @JsonProperty(required = true)
    private String toAccountId;

    @JsonProperty(required = true)
    private BigDecimal amount;
}
