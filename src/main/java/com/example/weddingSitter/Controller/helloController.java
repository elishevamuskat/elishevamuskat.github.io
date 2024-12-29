package com.example.weddingSitter.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class helloController {

    @GetMapping("/hello")  // Maps to HTTP GET requests at /api/hello
    public String sayHello() {
        return "Hello, Spring Boot!";
    }
}
