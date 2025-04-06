package com.example.s30019tpo05.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;

@Controller
public class TimeController {


    private static final String DEFAULT_FORMAT = "HH:mm:ss.SSSS yyyy/MM/dd";


    @GetMapping("/current-time")
    public String getCurrentTime(@RequestParam(value = "timezone", required = false) String timezone,
                                 @RequestParam(value = "format", required = false) String format,
                                 Model model) {
        String warningMessage = "";
        ZonedDateTime currentTime;
        DateTimeFormatter formatter;


        try {
            ZoneId zone = (timezone != null && !timezone.trim().isEmpty())
                    ? ZoneId.of(timezone.trim())
                    : ZoneId.systemDefault();
            currentTime = ZonedDateTime.now(zone);
        } catch (Exception e) {
            currentTime = ZonedDateTime.now();
            warningMessage += "Invalid time zone provided. Defaulting to system time zone.";
        }

        try {
            if (format != null && !format.trim().isEmpty()) {
                formatter = DateTimeFormatter.ofPattern(format.trim());

                formatter.format(currentTime);
            } else {
                formatter = DateTimeFormatter.ofPattern(DEFAULT_FORMAT);
            }
        } catch (Exception e) {
            formatter = DateTimeFormatter.ofPattern(DEFAULT_FORMAT);
            warningMessage += " Invalid format provided. Defaulting to default format.";
        }

        String formattedTime = currentTime.format(formatter);

        model.addAttribute("currentTime", formattedTime);
        model.addAttribute("warningMessage", warningMessage);
        return "current-time";
    }


    @GetMapping("/current-year")
    public String getCurrentYear(Model model) {
        int year = Year.now().getValue();
        model.addAttribute("currentYear", year);
        return "current-year";
    }
}
