package com.example.s30019tpo05.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class TimeController {

    @GetMapping("/current-time")
    public String getCurrentTime() {
        // Just a simple example returning the current time
        return ZonedDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSSS yyyy/MM/dd"));
    }
}
