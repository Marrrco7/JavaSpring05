package com.example.s30019tpo05.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

@Controller
public class ConverterController {

    private static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*()_+-=[]{}|;:<>,.?/";

    @PostMapping(value = "/convert", produces = "text/html")
    @ResponseBody
    public String doConversion(@RequestParam("value") String input,
                               @RequestParam("fromBase") int src,
                               @RequestParam("toBase") int tgt) {
        String outHtml, resBlock = "", errBlock = "";
        try {
            checkBases(src, tgt);
            long dec = toDecimal(input, src);
            resBlock = generateOutput(dec, tgt);
        } catch (Exception ex) {
            errBlock = "<p style='color:red;'><strong>Error:</strong> " + ex.getMessage() + "</p>";
        }
        outHtml = fetchTemplate("src/main/resources/static/base-conversion-result.html");
        if (outHtml == null) return "<h1>Error</h1><p>Template load failed.</p>";
        return outHtml.replace("[[RESULTS]]", resBlock)
                .replace("[[ERROR_MESSAGE]]", errBlock);
    }

    private void checkBases(int a, int b) {
        if (a < 2 || a > 100) throw new IllegalArgumentException("fromBase must be between 2 and 100.");
        if (b < 2 || b > 100) throw new IllegalArgumentException("toBase must be between 2 and 100.");
    }

    private long toDecimal(String str, int base) throws Exception {
        long num = 0;
        for (char c : str.toCharArray()) {
            int pos = CHARS.indexOf(c);
            if (pos < 0 || pos >= base) throw new Exception("Invalid digit '" + c + "' for base " + base);
            num = num * base + pos;
        }
        return num;
    }

    private String fromDecimal(long num, int base) {
        if (num == 0) return "0";
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            int r = (int) (num % base);
            sb.insert(0, CHARS.charAt(r));
            num /= base;
        }
        return sb.toString();
    }

    private String generateOutput(long dec, int target) {
        StringBuilder sb = new StringBuilder();
        sb.append("<p><strong>Converted (Base ").append(target).append("):</strong> ")
                .append(fromDecimal(dec, target)).append("</p>")
                .append("<hr>")
                .append("<p><strong>BIN:</strong> ").append(Long.toBinaryString(dec)).append("</p>")
                .append("<p><strong>OCT:</strong> ").append(Long.toOctalString(dec)).append("</p>")
                .append("<p><strong>DEC:</strong> ").append(dec).append("</p>")
                .append("<p><strong>HEX:</strong> ").append(Long.toHexString(dec).toUpperCase()).append("</p>");
        return sb.toString();
    }

    private String fetchTemplate(String path) {
        try {
            return Files.readString(Paths.get(path));
        } catch (IOException e) {
            return null;
        }
    }
}
