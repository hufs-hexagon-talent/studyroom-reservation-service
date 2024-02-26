package com.test.studyroomreservationsystem.config;

import com.test.studyroomreservationsystem.repository.ReservationRepository;
import com.test.studyroomreservationsystem.repository.jpa.JpaReservationRepo;
import com.test.studyroomreservationsystem.service.ReservationService;
import com.test.studyroomreservationsystem.service.ReservationServiceV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaConfig {
    @Bean
    public ReservationService reservationService(){
        return new ReservationServiceV1(reservationRepository());
    }
    @Bean
    public ReservationRepository reservationRepository(){
        return new JpaReservationRepo();
    }
}
