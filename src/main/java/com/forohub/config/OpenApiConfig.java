package com.forohub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI foroHubOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ForoHub API")
                        .version("1.0.0")
                        .description("API REST para la gestión de temas de foros y autenticación de usuarios"));
    }
}
