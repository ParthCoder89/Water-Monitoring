package com.neerpilot.backend.controller;

import com.neerpilot.backend.dto.DashboardResponse;
import com.neerpilot.backend.dto.HomeDataResponse;
import com.neerpilot.backend.dto.TankDataResponse;
import com.neerpilot.backend.service.HomeService;
import com.neerpilot.backend.service.TankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Single combined endpoint the dashboard hits on initial page load
 * to populate everything at once (mirrors the `state` object in dashboard.js).
 * After this, the frontend can subscribe to /topic/tank and /topic/home
 * over WebSocket for live updates, or keep polling /api/tank/latest and /api/home/latest.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final TankService tankService;
    private final HomeService homeService;

    public DashboardController(TankService tankService, HomeService homeService) {
        this.tankService = tankService;
        this.homeService = homeService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardResponse> getSummary(
            @RequestParam(defaultValue = "TANK-01") String tankDeviceId,
            @RequestParam(defaultValue = "HOME-01") String homeDeviceId) {

        TankDataResponse tank = tankService.getLatest(tankDeviceId);
        HomeDataResponse home = homeService.getLatest(homeDeviceId);

        return ResponseEntity.ok(new DashboardResponse(true, tank, home));
    }
}
