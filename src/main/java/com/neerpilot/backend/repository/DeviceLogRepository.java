package com.neerpilot.backend.repository;

import com.neerpilot.backend.model.DeviceLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceLogRepository extends JpaRepository<DeviceLog, Long> {
    List<DeviceLog> findAllByOrderByTimestampDesc(Pageable pageable);
}
