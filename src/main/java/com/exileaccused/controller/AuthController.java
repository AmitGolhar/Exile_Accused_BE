package com.exileaccused.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exileaccused.auth.jwt.JwtTokenUtil;
import com.exileaccused.entity.BlacklistedToken;
import com.exileaccused.entity.User;
import com.exileaccused.repository.BlacklistedTokenRepository;
import com.exileaccused.repository.UserRepository;
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")

public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private JwtTokenUtil jwtTokenUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired private BlacklistedTokenRepository blacklistedTokenRepository;

    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String fullName = body.getOrDefault("fullName", username);

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", "Username already taken"));
        }

        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return ResponseEntity.ok(Collections.singletonMap("message", "registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401)
                    .body(Collections.singletonMap("error", "Invalid credentials"));
        }

        String token = jwtTokenUtil.generateToken(username);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", username);

        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            blacklistedTokenRepository.save(new BlacklistedToken(token));
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "Logged out successfully"));
    }
    
}