package com.test.studyroomreservationsystem.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";
        Components components = new Components()
                .addSecuritySchemes(jwt,
                        new SecurityScheme()
                                .name(jwt)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
        );

//        String serverUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();


        return new OpenAPI()
                .info(apiInfo())
                .components(components);
    }
    private Info apiInfo() {
        return new Info()
                .title("HUFS Reservation Service") // API의 제목
                .description("User Side, Admin Side API") // API에 대한 설명
                .version("1.0.1"); // API의 버전
    }
}
