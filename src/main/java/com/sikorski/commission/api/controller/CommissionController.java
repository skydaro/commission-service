package com.sikorski.commission.api.controller;

import com.sikorski.commission.application.CommissionService;
import com.sikorski.commission.api.controller.dto.TransactionRequest;
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
import java.time.LocalDate;

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
        if (request.getDate().isAfter(LocalDate.now())) {
            log.error("Date cannot be future date {}", request.getDate());
            return new ResponseEntity<>("Date cannot be future date", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(service.getCommission(request), HttpStatus.OK);
    }
}