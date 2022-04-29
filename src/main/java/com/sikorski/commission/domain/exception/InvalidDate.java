package com.sikorski.commission.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidDate extends RuntimeException {

    public InvalidDate(LocalDate localDate) {
        super(String.format("Date cannot be future date: %s", localDate.toString()));
    }
}   