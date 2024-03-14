package com.test.studyroomreservationsystem.domain.repository;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomOperationPolicyRepository extends JpaRepository<RoomOperationPolicy,Long> {
}
