package com.neerpilot.backend.service;

import com.neerpilot.backend.dto.TankDataRequest;
import com.neerpilot.backend.dto.TankDataResponse;
import com.neerpilot.backend.exception.ApiException;
import com.neerpilot.backend.model.TankData;
import com.neerpilot.backend.repository.TankDataRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TankService {

    private final TankDataRepository tankDataRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final LogService logService;

    public TankService(TankDataRepository tankDataRepository,
                        SimpMessagingTemplate messagingTemplate,
                        LogService logService) {
        this.tankDataRepository = tankDataRepository;
        this.messagingTemplate = messagingTemplate;
        this.logService = logService;
    }

    public TankDataResponse ingest(TankDataRequest request) {
        TankData entity = new TankData();
        entity.setDeviceId(request.getDeviceId());
        entity.setLevelPercent(request.getLevelPercent());
        entity.setCapacityLiters(request.getCapacityLiters());
        entity.setBatteryPercent(request.getBatteryPercent());
        entity.setCharging(request.isCharging());
        entity.setSolarCharging(request.isSolarCharging());
        entity.setVoltage(request.getVoltage());
        entity.setSignalDbm(request.getSignalDbm());
        entity.setTempC(request.getTempC());
        entity.setHumidity(request.getHumidity());
        entity.setUltrasonicCm(request.getUltrasonicCm());
        entity.setProbesWet(request.getProbesWet());
        entity.setConnected(true);

        TankData saved = tankDataRepository.save(entity);

        // Auto-log important threshold events, mirroring the dashboard's log panel
        if (saved.getLevelPercent() >= 97) {
            logService.createLog("danger", "Tank " + saved.getDeviceId() + " reached overflow level.");
        } else if (saved.getLevelPercent() <= 5) {
            logService.createLog("danger", "Tank " + saved.getDeviceId() + " reported dry condition.");
        }

        TankDataResponse response = TankDataResponse.fromEntity(saved);
        messagingTemplate.convertAndSend("/topic/tank", response);
        return response;
    }

    public TankDataResponse getLatest(String deviceId) {
        TankData data = tankDataRepository.findTopByDeviceIdOrderByTimestampDesc(deviceId)
                .orElseThrow(() -> new ApiException("No tank data found for device " + deviceId, HttpStatus.NOT_FOUND));
        return TankDataResponse.fromEntity(data);
    }

    public List<TankDataResponse> getHistory(String deviceId, int limit) {
        return tankDataRepository.findByDeviceIdOrderByTimestampDesc(deviceId, PageRequest.of(0, limit))
                .stream()
                .map(TankDataResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
