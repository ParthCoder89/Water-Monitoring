package com.neerpilot.backend.controller;

import com.neerpilot.backend.dto.TankDataRequest;
import com.neerpilot.backend.dto.TankDataResponse;
import com.neerpilot.backend.service.TankService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tank")
public class TankController {

    private final TankService tankService;

    public TankController(TankService tankService) {
        this.tankService = tankService;
    }

    /** Called by the ESP32 Tank Unit. Requires header: X-API-KEY */
    @PostMapping("/data")
    public ResponseEntity<TankDataResponse> receiveData(@Valid @RequestBody TankDataRequest request) {
        return ResponseEntity.ok(tankService.ingest(request));
    }

    /** Called by the dashboard. Requires header: Authorization: Bearer <jwt> */
    @GetMapping("/latest")
    public ResponseEntity<TankDataResponse> getLatest(
            @RequestParam(defaultValue = "TANK-01") String deviceId) {
        return ResponseEntity.ok(tankService.getLatest(deviceId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<TankDataResponse>> getHistory(
            @RequestParam(defaultValue = "TANK-01") String deviceId,
            @RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(tankService.getHistory(deviceId, limit));
    }
}
