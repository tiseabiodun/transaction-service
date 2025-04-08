package dev.tise.transaction_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {


    @GetMapping()
    public ResponseEntity<Map<String,String>> firstAPI(){
        return ResponseEntity.ok(Map.of("message","I love Java"));
    }
}
