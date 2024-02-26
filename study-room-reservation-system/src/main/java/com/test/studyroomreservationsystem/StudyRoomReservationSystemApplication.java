package com.test.studyroomreservationsystem;

import com.test.studyroomreservationsystem.config.JpaConfig;
import com.test.studyroomreservationsystem.repository.ReservationRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Import(JpaConfig.class)
@SpringBootApplication
public class StudyRoomReservationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyRoomReservationSystemApplication.class, args);
	}
	@Bean
	@Profile("local")
	public TestDataInit testDataInit(ReservationRepository reservationRepository){
		return new TestDataInit(reservationRepository);
	}
}
