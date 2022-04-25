package com.sikorski.commission.configuration.handler;

import com.sikorski.commission.domain.fx.ExchangeRateNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import javax.validation.ConstraintViolationException;
import java.io.IOException;

@Slf4j
@ControllerAdvice
public class CustomErrorHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(ConstraintViolationException exception,
                                                   ServletWebRequest webRequest) throws IOException {
        log.error(exception.getMessage(), exception);
        if (webRequest.getResponse() != null) {
            webRequest.getResponse().sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        }
    }

    @ExceptionHandler(ExchangeRateNotFound.class)
    public void handleServiceException(ExchangeRateNotFound exception,
                                       ServletWebRequest webRequest) throws IOException {
        log.error(exception.getMessage(), exception);
        if (webRequest.getResponse() != null) {
            webRequest.getResponse().sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
        }
    }
}