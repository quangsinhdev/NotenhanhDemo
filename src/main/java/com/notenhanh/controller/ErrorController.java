package com.notenhanh.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class ErrorController {
    @GetMapping("/error")
    public String showErrorNotification() {
    	return "error";
    }
}
