package com.example.weddingSitter.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    // Mock user database
    private final Map<String, String> users = new HashMap<>();

    public UserService() {
        // Add a test user: username="user", password="password"
        users.put("user", "password");
    }

    public boolean authenticate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}
