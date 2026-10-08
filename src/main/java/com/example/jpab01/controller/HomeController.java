package com.example.jpab01.controller;
/* 그냥 테스트용 */
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
                "status", "ok",
                "message", "Server is running"
        );
    }
}