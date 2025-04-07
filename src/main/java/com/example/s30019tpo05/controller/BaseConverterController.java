package com.example.s30019tpo05.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class BaseConverterController {

    private static final String DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*()_+-=[]{}|;:<>,.?/";

    @PostMapping(value = "/convert", produces = "text/html")
    @ResponseBody
    public String convert(@RequestParam String value,
                          @RequestParam int fromBase,
                          @RequestParam int toBase) {

        String resultHtml;
        String resultBlock = "";
        String errorBlock = "";

        try {
            if (fromBase < 2 || fromBase > 100 || toBase < 2 || toBase > 100) {
                throw new IllegalArgumentException("Bases must be between 2 and 100.");
            }

            long decimal = parseToDecimal(value, fromBase);
            String converted = convertFromDecimal(decimal, toBase);

            resultBlock += "<p><strong>Converted (Base " + toBase + "):</strong> " + converted + "</p>";
            resultBlock += "<hr>";
            resultBlock += "<p><strong>BIN:</strong> " + Long.toBinaryString(decimal) + "</p>";
            resultBlock += "<p><strong>OCT:</strong> " + Long.toOctalString(decimal) + "</p>";
            resultBlock += "<p><strong>DEC:</strong> " + decimal + "</p>";
            resultBlock += "<p><strong>HEX:</strong> " + Long.toHexString(decimal).toUpperCase() + "</p>";
        } catch (Exception e) {
            errorBlock = "<p style='color:red;'><strong>Error:</strong> " + e.getMessage() + "</p>";
        }

        try {
            resultHtml = Files.readString(Paths.get("src/main/resources/static/base-conversion-result.html"));
        } catch (IOException e) {
            return "<h1>Error</h1><p>Couldn't load result page.</p>";
        }

        return resultHtml
                .replace("[[RESULTS]]", resultBlock)
                .replace("[[ERROR]]", errorBlock);
    }

    private long parseToDecimal(String value, int base) throws Exception {
        long result = 0;
        for (char c : value.toCharArray()) {
            int digit = DIGITS.indexOf(c);
            if (digit == -1 || digit >= base) {
                throw new Exception("Invalid digit '" + c + "' for base " + base);
            }
            result = result * base + digit;
        }
        return result;
    }

    private String convertFromDecimal(long number, int base) {
        if (number == 0) return "0";
        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            sb.insert(0, DIGITS.charAt((int)(number % base)));
            number /= base;
        }
        return sb.toString();
    }
}
