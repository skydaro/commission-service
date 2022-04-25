package com.sikorski.commission.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class TransactionRequest {
    @NotNull(message = "Date can not be null")
    private LocalDate date;
    @NotNull(message = "Amount can not be null")
    @NotEmpty(message = "Amount can not be empty")
    private String amount;
    @NotNull(message = "Currency can not be null")
    @Size(max = 3, min = 3, message = "Currency must be 3 characters")
    private String currency;
    @NotNull(message = "ClientId can not be null")
    @JsonProperty("client_id")
    private Integer clientId;
}