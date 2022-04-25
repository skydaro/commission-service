package com.sikorski.commission.application;

import an.awesome.pipelinr.Pipeline;
import com.sikorski.commission.application.command.CalculateCommissionCommand;
import com.sikorski.commission.api.controller.dto.CommissionResponse;
import com.sikorski.commission.api.controller.dto.TransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommissionService {
    private final Pipeline pipeline;

    public CommissionResponse getCommission(TransactionRequest request) {
        var command = new CalculateCommissionCommand(request);
        var commission = pipeline.send(command);
        return CommissionResponse.builder()
                .amount(commission.getAmount().toString())
                .currency(commission.getCurrencyCode())
                .build();
    }
}