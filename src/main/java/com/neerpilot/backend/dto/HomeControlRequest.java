package com.neerpilot.backend.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * JSON body the DASHBOARD (frontend, JWT-authenticated user) posts to
 * /api/home/control when a user flips motor/relay/buzzer/oled/rgb/mode toggles.
 * The ESP32 Home Unit polls GET /api/home/command to pick this up.
 */
public class HomeControlRequest {

    @NotBlank
    private String deviceId;

    private boolean motorOn;
    private boolean relayOn;
    private boolean buzzerOn;
    private String rgbColor = "#00BFFF";
    private boolean oledOn = true;
    private boolean autoMode = true;

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public boolean isMotorOn() { return motorOn; }
    public void setMotorOn(boolean motorOn) { this.motorOn = motorOn; }

    public boolean isRelayOn() { return relayOn; }
    public void setRelayOn(boolean relayOn) { this.relayOn = relayOn; }

    public boolean isBuzzerOn() { return buzzerOn; }
    public void setBuzzerOn(boolean buzzerOn) { this.buzzerOn = buzzerOn; }

    public String getRgbColor() { return rgbColor; }
    public void setRgbColor(String rgbColor) { this.rgbColor = rgbColor; }

    public boolean isOledOn() { return oledOn; }
    public void setOledOn(boolean oledOn) { this.oledOn = oledOn; }

    public boolean isAutoMode() { return autoMode; }
    public void setAutoMode(boolean autoMode) { this.autoMode = autoMode; }
}
