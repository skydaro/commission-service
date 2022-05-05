package com.sikorski.commission.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Value
@Builder
public class TransactionRequest {
    @NotNull(message = "Date can not be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date;
    @NotNull(message = "Amount can not be null")
    @NotEmpty(message = "Amount can not be empty")
    @Digits(integer = 18, fraction = 2)
    String amount;
    @NotNull(message = "Currency can not be null")
    @Size(max = 3, min = 3, message = "Currency must be 3 characters")
    String currency;
    @NotNull(message = "ClientId can not be null")
    @JsonProperty("client_id")
    Integer clientId;
}