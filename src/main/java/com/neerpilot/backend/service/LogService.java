package com.neerpilot.backend.service;

import com.neerpilot.backend.dto.DeviceLogResponse;
import com.neerpilot.backend.model.DeviceLog;
import com.neerpilot.backend.repository.DeviceLogRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogService {

    private final DeviceLogRepository logRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public LogService(DeviceLogRepository logRepository, SimpMessagingTemplate messagingTemplate) {
        this.logRepository = logRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public DeviceLogResponse createLog(String level, String message) {
        DeviceLog log = new DeviceLog();
        log.setLevel(level);
        log.setMessage(message);
        DeviceLog saved = logRepository.save(log);

        DeviceLogResponse response = DeviceLogResponse.fromEntity(saved);
        messagingTemplate.convertAndSend("/topic/logs", response);
        return response;
    }

    public List<DeviceLogResponse> getRecent(int limit) {
        return logRepository.findAllByOrderByTimestampDesc(PageRequest.of(0, limit))
                .stream()
                .map(DeviceLogResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
