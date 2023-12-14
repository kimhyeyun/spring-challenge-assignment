package com.sparta.springchallengeassignment.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Board APP",
                description = "Bard API 명세",
                version = "v1"
        )
)
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEMA_NAME = "Authorization";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI();
    }
}
