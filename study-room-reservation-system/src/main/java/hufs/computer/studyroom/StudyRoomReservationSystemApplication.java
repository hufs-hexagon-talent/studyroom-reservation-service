package hufs.computer.studyroom;

import hufs.computer.studyroom.config.JpaConfig;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Import(JpaConfig.class)
@EnableJpaAuditing
@OpenAPIDefinition(servers = {@Server(url = "https://api.studyroom.computer.hufs.ac.kr", description = "Default Server URL")})
//@OpenAPIDefinition(servers = {@Server(url = "http://localhost:8081", description = "Default Server URL")})
@SpringBootApplication
public class StudyRoomReservationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyRoomReservationSystemApplication.class, args);
	}

}
