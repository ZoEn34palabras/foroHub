package com.forohub.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StartupCheck {

    @Value("${security.jwt.secret}")
    private String secret;

    @PostConstruct
    public void init() {
        System.out.println("✅ JWT SECRET LOADED: " + secret);
    }
}
