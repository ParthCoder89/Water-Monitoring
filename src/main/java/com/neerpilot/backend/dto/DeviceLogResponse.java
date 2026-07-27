package com.neerpilot.backend.dto;

import com.neerpilot.backend.model.DeviceLog;
import java.time.LocalDateTime;

public class DeviceLogResponse {

    private String level;
    private String message;
    private LocalDateTime timestamp;

    public static DeviceLogResponse fromEntity(DeviceLog log) {
        DeviceLogResponse r = new DeviceLogResponse();
        r.level = log.getLevel();
        r.message = log.getMessage();
        r.timestamp = log.getTimestamp();
        return r;
    }

    public String getLevel() { return level; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
