package com.vibe.auth.api;

import com.vibe.auth.security.JwtTokenService;
import com.vibe.auth.store.UserStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserStore userStore;
    private final JwtTokenService jwtTokenService;

    public AuthController(UserStore userStore, JwtTokenService jwtTokenService) {
        this.userStore = userStore;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/register/")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> payload) {
        try {
            Map<String, Object> user = userStore.createUser(payload);
            String token = jwtTokenService.issueToken(String.valueOf(user.get("id")), String.valueOf(user.get("username")));
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", token, "user", user));
        } catch (IllegalArgumentException ex) {
            if ("username".equals(ex.getMessage())) {
                return ResponseEntity.badRequest().body(Map.of("username", List.of("Username already exists")));
            }
            return ResponseEntity.badRequest().body(Map.of("password_confirm", List.of("Password confirmation does not match")));
        }
    }

    @PostMapping("/login/")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> payload) {
        Map<String, Object> user = userStore.login(
                String.valueOf(payload.get("username")),
                String.valueOf(payload.get("password")));
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }
        Map<String, Object> publicUser = userStore.publicUser(user);
        String token = jwtTokenService.issueToken(String.valueOf(publicUser.get("id")), String.valueOf(publicUser.get("username")));
        return ResponseEntity.ok(Map.of("token", token, "user", publicUser));
    }

    @GetMapping("/profile/")
    public ResponseEntity<?> profile(@RequestHeader(name = "X-User-Id", required = false) String userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }
        Map<String, Object> user = userStore.findById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }
        return ResponseEntity.ok(userStore.publicUser(user));
    }

    @GetMapping("/preferences/")
    public ResponseEntity<?> preferences(@RequestHeader(name = "X-User-Id", required = false) String userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }
        return ResponseEntity.ok(userStore.getPreferences(userId));
    }

    @PatchMapping("/preferences/")
    public ResponseEntity<?> updatePreferences(
            @RequestHeader(name = "X-User-Id", required = false) String userId,
            @RequestBody Map<String, Object> payload) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }
        Map<String, Object> prefs = userStore.updatePreferences(userId, new HashMap<>(payload));
        return ResponseEntity.ok(prefs);
    }
}
