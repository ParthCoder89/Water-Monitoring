package com.neerpilot.backend.repository;

import com.neerpilot.backend.model.HomeData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HomeDataRepository extends JpaRepository<HomeData, Long> {
    Optional<HomeData> findTopByDeviceIdOrderByTimestampDesc(String deviceId);
    List<HomeData> findByDeviceIdOrderByTimestampDesc(String deviceId, Pageable pageable);
}
