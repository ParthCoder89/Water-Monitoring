package com.neerpilot.backend.service;

import com.neerpilot.backend.dto.*;
import com.neerpilot.backend.exception.ApiException;
import com.neerpilot.backend.model.HomeCommand;
import com.neerpilot.backend.model.HomeData;
import com.neerpilot.backend.repository.HomeCommandRepository;
import com.neerpilot.backend.repository.HomeDataRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeService {

    private final HomeDataRepository homeDataRepository;
    private final HomeCommandRepository homeCommandRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final LogService logService;

    public HomeService(HomeDataRepository homeDataRepository,
                        HomeCommandRepository homeCommandRepository,
                        SimpMessagingTemplate messagingTemplate,
                        LogService logService) {
        this.homeDataRepository = homeDataRepository;
        this.homeCommandRepository = homeCommandRepository;
        this.messagingTemplate = messagingTemplate;
        this.logService = logService;
    }

    /** ESP32 Home Unit -> POST /api/home/data (telemetry/status report) */
    public HomeDataResponse ingest(HomeDataRequest request) {
        HomeData entity = new HomeData();
        entity.setDeviceId(request.getDeviceId());
        entity.setBatteryPercent(request.getBatteryPercent());
        entity.setCharging(request.isCharging());
        entity.setAcVoltage(request.getAcVoltage());
        entity.setMotorOn(request.isMotorOn());
        entity.setRelayOn(request.isRelayOn());
        entity.setBuzzerOn(request.isBuzzerOn());
        entity.setRgbColor(request.getRgbColor());
        entity.setOledOn(request.isOledOn());
        entity.setAutoMode(request.isAutoMode());
        entity.setSignalDbm(request.getSignalDbm());

        HomeData saved = homeDataRepository.save(entity);

        if (saved.getAcVoltage() < 190) {
            logService.createLog("danger", "Home unit " + saved.getDeviceId() + " under-voltage detected.");
        } else if (saved.getAcVoltage() > 250) {
            logService.createLog("warn", "Home unit " + saved.getDeviceId() + " over-voltage detected.");
        }

        HomeDataResponse response = HomeDataResponse.fromEntity(saved);
        messagingTemplate.convertAndSend("/topic/home", response);
        return response;
    }

    public HomeDataResponse getLatest(String deviceId) {
        HomeData data = homeDataRepository.findTopByDeviceIdOrderByTimestampDesc(deviceId)
                .orElseThrow(() -> new ApiException("No home data found for device " + deviceId, HttpStatus.NOT_FOUND));
        return HomeDataResponse.fromEntity(data);
    }

    public List<HomeDataResponse> getHistory(String deviceId, int limit) {
        return homeDataRepository.findByDeviceIdOrderByTimestampDesc(deviceId, PageRequest.of(0, limit))
                .stream()
                .map(HomeDataResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /** Dashboard (JWT user) -> POST /api/home/control : set desired actuator state */
    public HomeCommandResponse setControl(HomeControlRequest request) {
        HomeCommand command = homeCommandRepository.findByDeviceId(request.getDeviceId())
                .orElseGet(() -> {
                    HomeCommand c = new HomeCommand();
                    c.setDeviceId(request.getDeviceId());
                    return c;
                });

        command.setMotorOn(request.isMotorOn());
        command.setRelayOn(request.isRelayOn());
        command.setBuzzerOn(request.isBuzzerOn());
        command.setRgbColor(request.getRgbColor());
        command.setOledOn(request.isOledOn());
        command.setAutoMode(request.isAutoMode());

        HomeCommand saved = homeCommandRepository.save(command);

        logService.createLog("ok", "Control command updated for " + saved.getDeviceId()
                + " (motor=" + saved.isMotorOn() + ", relay=" + saved.isRelayOn() + ").");

        HomeCommandResponse response = HomeCommandResponse.fromEntity(saved);
        messagingTemplate.convertAndSend("/topic/home-command", response);
        return response;
    }

    /** ESP32 Home Unit -> GET /api/home/command : poll for latest desired actuator state */
    public HomeCommandResponse getCommand(String deviceId) {
        return homeCommandRepository.findByDeviceId(deviceId)
                .map(HomeCommandResponse::fromEntity)
                .orElseGet(() -> {
                    HomeCommand defaultCommand = new HomeCommand();
                    defaultCommand.setDeviceId(deviceId);
                    return HomeCommandResponse.fromEntity(defaultCommand);
                });
    }
}
