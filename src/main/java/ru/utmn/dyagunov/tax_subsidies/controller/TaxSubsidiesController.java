package ru.utmn.dyagunov.tax_subsidies.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaxSubsidiesController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
}
