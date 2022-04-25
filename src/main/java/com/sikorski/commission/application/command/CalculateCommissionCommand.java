package com.sikorski.commission.application.command;

import an.awesome.pipelinr.Command;
import com.sikorski.commission.api.controller.dto.TransactionRequest;
import com.sikorski.commission.domain.entity.Commission;

public record CalculateCommissionCommand(TransactionRequest request) implements Command<Commission> {
}