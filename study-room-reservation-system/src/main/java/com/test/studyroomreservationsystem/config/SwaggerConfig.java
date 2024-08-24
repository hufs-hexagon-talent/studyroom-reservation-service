package com.test.studyroomreservationsystem.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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


        return new OpenAPI()
                .info(apiInfo())
                .components(components);
    }
    private Info apiInfo() {
        return new Info()
                .title("HUFS Reservation Service") // API의 제목
                .description("User Side, Admin Side API") // API에 대한 설명
                .version("1.3.5"); // API의 버전
    }
}
