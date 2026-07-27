package com.neerpilot.backend.controller;

import com.neerpilot.backend.dto.DeviceLogResponse;
import com.neerpilot.backend.service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/recent")
    public ResponseEntity<List<DeviceLogResponse>> getRecent(@RequestParam(defaultValue = "40") int limit) {
        return ResponseEntity.ok(logService.getRecent(limit));
    }
}
