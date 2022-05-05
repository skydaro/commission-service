package com.sikorski.commission.api.controller;

import com.sikorski.commission.api.controller.dto.CommissionResponse;
import com.sikorski.commission.api.controller.dto.TransactionRequest;
import com.sikorski.commission.application.CommissionService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@OpenAPIDefinition(info = @Info(
        title = "commission-service",
        description = "REST API for transaction commission calculation",
        version = "0.0.1"))
@Slf4j
@RestController
@RequestMapping("/api/v1/commission")
@RequiredArgsConstructor
@Validated
public class CommissionController {

    private final CommissionService service;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping()
    @Synchronized()
    public ResponseEntity<CommissionResponse> getCommission(@Valid @RequestBody TransactionRequest request,
                                                            BindingResult bindingResult) {
        log.info("Transaction request received: {}", request);
        return new ResponseEntity<>(service.getCommission(request), HttpStatus.OK);
    }
}