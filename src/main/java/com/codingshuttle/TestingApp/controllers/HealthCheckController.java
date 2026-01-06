package com.codingshuttle.TestingApp.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Value("${my.variable}")
    private String myVariable;

    @GetMapping("/")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/check")
    public ResponseEntity<String> check(){
        return ResponseEntity.ok("check");
    }

    @GetMapping("/env")
    public ResponseEntity<String> getEnv(){
        return ResponseEntity.ok("Application Env: "+myVariable);
    }
}
