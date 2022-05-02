package com.sikorski.commission.api.controller;

import com.sikorski.commission.api.controller.dto.CommissionResponse;
import com.sikorski.commission.api.controller.dto.TransactionRequest;
import com.sikorski.commission.application.service.CommissionService;
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
    public ResponseEntity<CommissionResponse> getCommission(@Valid @RequestBody TransactionRequest request,
                                                            BindingResult bindingResult) {
        log.info("Transaction request received: {}", request);
        return new ResponseEntity<>(service.getCommission(request), HttpStatus.OK);
    }
}