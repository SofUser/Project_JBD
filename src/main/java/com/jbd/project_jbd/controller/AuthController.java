package com.jbd.project_jbd.controller;

import com.jbd.project_jbd.dto.AuthRequest;
import com.jbd.project_jbd.dto.AuthResponse;
import com.jbd.project_jbd.entity.Role;
import com.jbd.project_jbd.entity.User;
import com.jbd.project_jbd.security.JwtUtil;
import com.jbd.project_jbd.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        User user = userService.registerUser(request.getEmail(), request.getPassword(), Role.USER);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String jwt = jwtUtil.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(new AuthResponse(jwt, user.getEmail(), user.getRole().name()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(jwt, user.getEmail(), user.getRole().name()));
    }
}