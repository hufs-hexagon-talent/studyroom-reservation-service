package com.test.studyroomreservationsystem.config;

import com.test.studyroomreservationsystem.repository.ReservationRepository;
import com.test.studyroomreservationsystem.repository.jpa.JpaReservationRepository;
import com.test.studyroomreservationsystem.service.ReservationService;
import com.test.studyroomreservationsystem.service.ReservationServiceV1;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaConfig {
    private EntityManager em;

    public JpaConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public ReservationService reservationService(){
        return new ReservationServiceV1(reservationRepository());
    }
    @Bean
    public ReservationRepository reservationRepository(){
        return new JpaReservationRepository(em);
    }
}
