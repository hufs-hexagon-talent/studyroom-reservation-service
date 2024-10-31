package hufs.computer.studyroom.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Value("${env.base-url}") private String backendBaseURL;


    @Bean
    public OpenAPI OpenApiConfig(OpenApiCustomizer openApiCustomizer) {
        // Servers 에 표시되는 정보 설정
        Server server = new Server();
        server.setUrl(backendBaseURL);
        server.setDescription("HUFS Reservation Service API");

        OpenAPI openAPI = new OpenAPI()
                // Servers 에 표시되는 정보 설정
                .components(new Components().addSecuritySchemes("JWT",
                        new SecurityScheme()
                        .name("JWT")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"))
                )
                // 기본적으로 모든 엔드포인트에 대한 JWT 인증이 필요한 것으로 설정
//                .addSecurityItem(new SecurityRequirement().addList(JWT))
                .info(apiInfo())
                // 서버 정보 추가
                .servers(List.of(server));

        openApiCustomizer.customise(openAPI);

        return openAPI;
    }
    private Info apiInfo() {
        return new Info()
                .title("HUFS Reservation Service") // API의 제목
                .description("User Side, Admin Side API") // API에 대한 설명
                .version("2.4.6"); // API의 버전
    }
}
