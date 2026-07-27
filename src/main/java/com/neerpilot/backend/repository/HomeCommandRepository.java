package com.neerpilot.backend.repository;

import com.neerpilot.backend.model.HomeCommand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HomeCommandRepository extends JpaRepository<HomeCommand, Long> {
    Optional<HomeCommand> findByDeviceId(String deviceId);
}
