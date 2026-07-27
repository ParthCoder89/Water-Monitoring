package com.neerpilot.backend.controller;

import com.neerpilot.backend.dto.*;
import com.neerpilot.backend.service.HomeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    /** Called by the ESP32 Home Unit to report telemetry. Requires header: X-API-KEY */
    @PostMapping("/data")
    public ResponseEntity<HomeDataResponse> receiveData(@Valid @RequestBody HomeDataRequest request) {
        return ResponseEntity.ok(homeService.ingest(request));
    }

    /** Called by the dashboard to read the latest status. Requires JWT. */
    @GetMapping("/latest")
    public ResponseEntity<HomeDataResponse> getLatest(
            @RequestParam(defaultValue = "HOME-01") String deviceId) {
        return ResponseEntity.ok(homeService.getLatest(deviceId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<HomeDataResponse>> getHistory(
            @RequestParam(defaultValue = "HOME-01") String deviceId,
            @RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(homeService.getHistory(deviceId, limit));
    }

    /** Called by the dashboard when a user flips motor/relay/buzzer/oled/rgb/mode toggles. Requires JWT. */
    @PostMapping("/control")
    public ResponseEntity<HomeCommandResponse> setControl(@Valid @RequestBody HomeControlRequest request) {
        return ResponseEntity.ok(homeService.setControl(request));
    }

    /** Called by the ESP32 Home Unit to poll for pending actuator commands. Requires header: X-API-KEY */
    @GetMapping("/command")
    public ResponseEntity<HomeCommandResponse> getCommand(
            @RequestParam(defaultValue = "HOME-01") String deviceId) {
        return ResponseEntity.ok(homeService.getCommand(deviceId));
    }
}
