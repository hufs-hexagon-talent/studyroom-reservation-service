package com.test.studyroomreservationsystem.domain;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.domain.repository.RoomOperationPolicyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoomOperationPolicyRepositoryTest {

    @Autowired
    RoomOperationPolicyRepository policyRepository;
    @BeforeEach
    void beforeRemove() {
        policyRepository.deleteAll();
    }
    @Test
    void save() {
        //given
        LocalTime defaultTime = LocalTime.now();

        LocalTime operationStartTime = defaultTime.plusHours(0);
        LocalTime operationEndTime = defaultTime.plusHours(8);

        RoomOperationPolicy policy = new RoomOperationPolicy();
        policy.setOperationStartTime(operationStartTime);
        policy.setOperationEndTime(operationEndTime);
        policy.setEachMaxMinute(180);

        //when
        RoomOperationPolicy savedPolicy = policyRepository.save(policy);

        //then
        assertThat(savedPolicy).isNotNull();
        assertThat(savedPolicy.getRoomOperationPolicyId()).isEqualTo(policy.getRoomOperationPolicyId());
        assertThat(savedPolicy.getOperationStartTime()).isEqualTo(policy.getOperationStartTime());
        assertThat(savedPolicy.getOperationEndTime()).isEqualTo(policy.getOperationEndTime());
        assertThat(savedPolicy.getEachMaxMinute()).isEqualTo(policy.getEachMaxMinute());

    }

}
