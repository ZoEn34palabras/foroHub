package com.forohub.config;

import com.forohub.security.JwtTokenFilter;
import com.forohub.security.TokenService;
import com.forohub.service.TopicService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestSecurityConfig {

    //  Mock del filtro JWT para que no intervenga en los tests
    @Bean
    @Primary
    public JwtTokenFilter jwtTokenFilter() {
        return mock(JwtTokenFilter.class);
    }

    @Bean
    public TokenService tokenService() {
        return Mockito.mock(TokenService.class);
    }


    //  Mock del servicio TopicService para usar en los tests
    @Bean
    @Primary
    public TopicService topicService() {
        return mock(TopicService.class);
    }

    //  Configuración de seguridad que permite todas las peticiones
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }
}
