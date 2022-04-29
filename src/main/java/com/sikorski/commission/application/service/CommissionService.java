package com.sikorski.commission.application.service;

import an.awesome.pipelinr.Pipeline;
import com.sikorski.commission.api.controller.dto.CommissionResponse;
import com.sikorski.commission.api.controller.dto.TransactionRequest;
import com.sikorski.commission.application.command.CalculateCommissionCommand;
import com.sikorski.commission.domain.exception.InvalidDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommissionService {
    private final Pipeline pipeline;
    private final TimeService timeService;

    public CommissionResponse getCommission(TransactionRequest request) {
        if (request.getDate().isAfter(timeService.getNow())) {
            log.error("Date cannot be future date {}", request.getDate());
            throw new InvalidDate(request.getDate());
        }
        var command = new CalculateCommissionCommand(request);
        var commission = pipeline.send(command);
        return CommissionResponse.builder()
                .amount(commission.getAmount().toString())
                .currency(commission.getCurrencyCode())
                .build();
    }
}