package com.neerpilot.backend.repository;

import com.neerpilot.backend.model.TankData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TankDataRepository extends JpaRepository<TankData, Long> {
    Optional<TankData> findTopByDeviceIdOrderByTimestampDesc(String deviceId);
    List<TankData> findByDeviceIdOrderByTimestampDesc(String deviceId, Pageable pageable);
}
