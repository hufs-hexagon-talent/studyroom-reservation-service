package com.test.studyroomreservationsystem.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaConfig {
    private EntityManager em;

    public JpaConfig(EntityManager em) {
        this.em = em;
    }


}
