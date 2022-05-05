package com.sikorski.commission.domain.command;

import an.awesome.pipelinr.Command;
import com.sikorski.commission.domain.entity.Commission;

import java.time.LocalDate;

public record CalculateCommissionCommand(LocalDate date,
                                         String amount,
                                         String currency,
                                         Integer clientId) implements Command<Commission> {
}