package com.forohub.controller;

import com.forohub.dto.AuthRequest;
import com.forohub.dto.RegisterRequest;
import com.forohub.entity.User;
import com.forohub.repository.ProfileRepository;
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
import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;


    @PostMapping("/login")
    public Map<String,String> login(@RequestBody @Valid AuthRequest req) {
        // Autenticar
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );
        // Generar token
        String jwt = tokenService.generateToken((UserDetails) auth.getPrincipal());
        return Collections.singletonMap("token", jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid RegisterRequest req) {
        // Verificar si ya existe un usuario con ese correo
        if (userRepo.findByCorreoElectronico(req.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El correo electrónico ya está registrado"));
        }

        // Buscar el perfil ROLE_USER
        var perfilUser = profileRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Perfil ROLE_USER no encontrado"));

        // Crear el usuario
        User user = new User();
        user.setNombre(req.getNombre());
        user.setCorreoElectronico(req.getEmail());
        user.setContrasena(passwordEncoder.encode(req.getPassword()));
        user.setProfiles(Set.of(perfilUser)); // ← Aquí se asigna el perfil
        userRepo.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Usuario registrado correctamente"));
    }

}
