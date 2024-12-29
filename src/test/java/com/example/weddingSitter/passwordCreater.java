package com.example.weddingSitter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class passwordCreater {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin123";
        String hashedPassword = encoder.encode(rawPassword);
        System.out.println("Encoded password: " + hashedPassword);
    }
}