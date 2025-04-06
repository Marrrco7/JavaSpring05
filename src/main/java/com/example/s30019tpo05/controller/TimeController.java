package com.example.s30019tpo05.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class TimeController {

    private static final String DEFAULT_FORMAT = "HH:mm:ss.SSSS yyyy/MM/dd";

    @GetMapping(value = "/current-time", produces = "text/html")
    public String getCurrentTime(@RequestParam(value = "timezone", required = false) String timezone,
                                 @RequestParam(value = "format", required = false) String format) {
        String warningMessage = "";
        ZonedDateTime currentTime;
        DateTimeFormatter formatter;

        try {
            ZoneId zone = (timezone != null && !timezone.trim().isEmpty())
                    ? ZoneId.of(timezone.trim())
                    : ZoneId.systemDefault();
            currentTime = ZonedDateTime.now(zone);
        } catch (Exception ex) {
            currentTime = ZonedDateTime.now();
            warningMessage += "<p style='color:red;'>Invalid time zone provided. Defaulting to system time zone.</p>";
        }

        try {
            if (format != null && !format.trim().isEmpty()) {
                formatter = DateTimeFormatter.ofPattern(format.trim());
                formatter.format(currentTime);
            } else {
                formatter = DateTimeFormatter.ofPattern(DEFAULT_FORMAT);
            }
        } catch (Exception ex) {
            formatter = DateTimeFormatter.ofPattern(DEFAULT_FORMAT);
            warningMessage += "<p style='color:red;'>Invalid format provided. Defaulting to default format.</p>";
        }

        String formattedTime = currentTime.format(formatter);
        return "<h1>" + formattedTime + "</h1>" + warningMessage;
    }

    @GetMapping(value = "/current-year", produces = "text/html")
    public String getCurrentYear() {
        int year = java.time.Year.now().getValue();
        return "<h1>" + year + "</h1>";
    }

}
