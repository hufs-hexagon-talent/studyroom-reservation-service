package com.test.studyroomreservationsystem;

import com.test.studyroomreservationsystem.config.JpaConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(JpaConfig.class)
@SpringBootApplication
public class StudyRoomReservationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyRoomReservationSystemApplication.class, args);
	}

}
