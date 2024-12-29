package com.example.weddingSitter.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginDetails) {
        String username = loginDetails.get("username");
        String password = loginDetails.get("password");

        Map<String, String> response = new HashMap<>();
        if ("admin".equals(username) && "password".equals(password)) {
            response.put("message", "Login successful");
            response.put("username", username);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
        return response;
    }
}

