package com.sikorski.commission.application;

import an.awesome.pipelinr.Pipeline;
import com.sikorski.commission.api.controller.dto.CommissionResponse;
import com.sikorski.commission.api.controller.dto.TransactionRequest;
import com.sikorski.commission.domain.command.CalculateCommissionCommand;
import com.sikorski.commission.domain.exception.InvalidDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommissionService {
    private final Pipeline pipeline;
    private final TimeService timeService;

    public CommissionResponse getCommission(TransactionRequest request) {
        validateDate(request.getDate());

        var command = new CalculateCommissionCommand(
                request.getDate(),
                request.getAmount(),
                request.getCurrency(),
                request.getClientId());

        var commission = pipeline.send(command);

        return CommissionResponse.builder()
                .amount(commission.getAmount().toString())
                .currency(commission.getCurrencyCode())
                .build();
    }

    private void validateDate(LocalDate date) {
        if (date.isAfter(timeService.getNow())) {
            log.error("Date cannot be future date {}", date);
            throw new InvalidDate(date);
        }
    }
}