package com.sikorski.commission.api.controller;

import com.sikorski.commission.api.dto.TransactionRequest;
import com.sikorski.commission.application.CommissionService;
import com.sikorski.commission.domain.fx.ExchangeRateNotFound;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/commission")
@RequiredArgsConstructor
@Validated
public class CommissionController {

    private final CommissionService service;
    @PostMapping()
    @Synchronized()
    public ResponseEntity<?> getCommission(@Valid @RequestBody TransactionRequest request,
                                                            BindingResult bindingResult) {
        try {
            var response = service.getCommission(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ExchangeRateNotFound e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}