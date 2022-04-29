package com.sikorski.commission.application.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;

@Service
public class TimeService {
    public LocalDate getNow() {
        return LocalDate.now(ZoneOffset.UTC);
    }
}