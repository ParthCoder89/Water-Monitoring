package com.neerpilot.backend.dto;

import com.neerpilot.backend.model.HomeCommand;
import java.time.LocalDateTime;

public class HomeCommandResponse {

    private String deviceId;
    private boolean motorOn;
    private boolean relayOn;
    private boolean buzzerOn;
    private String rgbColor;
    private boolean oledOn;
    private boolean autoMode;
    private LocalDateTime updatedAt;

    public static HomeCommandResponse fromEntity(HomeCommand c) {
        HomeCommandResponse r = new HomeCommandResponse();
        r.deviceId = c.getDeviceId();
        r.motorOn = c.isMotorOn();
        r.relayOn = c.isRelayOn();
        r.buzzerOn = c.isBuzzerOn();
        r.rgbColor = c.getRgbColor();
        r.oledOn = c.isOledOn();
        r.autoMode = c.isAutoMode();
        r.updatedAt = c.getUpdatedAt();
        return r;
    }

    public String getDeviceId() { return deviceId; }
    public boolean isMotorOn() { return motorOn; }
    public boolean isRelayOn() { return relayOn; }
    public boolean isBuzzerOn() { return buzzerOn; }
    public String getRgbColor() { return rgbColor; }
    public boolean isOledOn() { return oledOn; }
    public boolean isAutoMode() { return autoMode; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
