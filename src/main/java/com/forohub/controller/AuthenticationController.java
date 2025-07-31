package com.forohub.controller;

import com.forohub.dto.AuthRequest;
import com.forohub.dto.RegisterRequest;
import com.forohub.entity.User;
import com.forohub.repository.UserRepository;
import com.forohub.security.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Map<String,String> login(@RequestBody @Valid AuthRequest req) {
        // Authenticate
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );
        // Generate token
        String jwt = tokenService.generateToken((UserDetails) auth.getPrincipal());
        return Collections.singletonMap("token", jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest req) {
        User user = new User();
        user.setNombre(req.getNombre());
        user.setCorreoElectronico(req.getEmail());
        user.setContrasena(passwordEncoder.encode(req.getPassword()));
        // assign default profile (e.g. ROLE_USER)… fetch Profile entity and set
        userRepo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
