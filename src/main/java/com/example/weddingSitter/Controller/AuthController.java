package com.example.weddingSitter.Controller;

import com.example.weddingSitter.dependency.JwtUtil;
import com.example.weddingSitter.entities.User;
import com.example.weddingSitter.services.UserRepository;
import com.example.weddingSitter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register/member/{username}/{password}")
    public ResponseEntity<?> registerUser(@PathVariable String username, @PathVariable String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Username is already taken.");
        }

        User user = new User();
        user.setUsername(username);
        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("MEMBER");

        // Save the user to the database
        userRepository.save(user);

        return login(new LoginRequest(username, password));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // Generate JWT
            String token = jwtUtil.generateToken(loginRequest.getUsername());

            return ResponseEntity.ok(Map.of("token", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

//    @GetMapping("/login")
//    public ResponseEntity<String> login() {
//        return ResponseEntity.ok("Successful login");
//    }

//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody User user) {
//        System.out.println("Register endpoint hit for user: " + user);
//        if (userRepository.findByUsername(user.getUsername()) != null) {
//            return ResponseEntity.badRequest().body("Username already exists");
//        }
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//        return ResponseEntity.ok("User registered successfully");
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
//        String username = credentials.get("username");
//        String password = credentials.get("password");
//
//        if (userService.authenticate(username, password)) {
//            String token = jwtUtil.generateToken(username);
//            return ResponseEntity.ok(Map.of("token", token));
//        } else {
//            return ResponseEntity.status(401).body("Invalid username or password");
//        }
//    }
}

class LoginRequest {

    public LoginRequest(String username, String passowrd){
        this.username = username;
        this.password = passowrd;
    }
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    // Getters and Setters
}
