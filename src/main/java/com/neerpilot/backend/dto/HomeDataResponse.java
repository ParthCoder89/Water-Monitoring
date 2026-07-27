package com.neerpilot.backend.dto;

import com.neerpilot.backend.model.HomeData;
import java.time.LocalDateTime;

public class HomeDataResponse {

    private String deviceId;
    private double batteryPercent;
    private boolean charging;
    private double acVoltage;
    private boolean motorOn;
    private boolean relayOn;
    private boolean buzzerOn;
    private String rgbColor;
    private boolean oledOn;
    private boolean autoMode;
    private int signalDbm;
    private LocalDateTime timestamp;

    public static HomeDataResponse fromEntity(HomeData h) {
        HomeDataResponse r = new HomeDataResponse();
        r.deviceId = h.getDeviceId();
        r.batteryPercent = h.getBatteryPercent();
        r.charging = h.isCharging();
        r.acVoltage = h.getAcVoltage();
        r.motorOn = h.isMotorOn();
        r.relayOn = h.isRelayOn();
        r.buzzerOn = h.isBuzzerOn();
        r.rgbColor = h.getRgbColor();
        r.oledOn = h.isOledOn();
        r.autoMode = h.isAutoMode();
        r.signalDbm = h.getSignalDbm();
        r.timestamp = h.getTimestamp();
        return r;
    }

    public String getDeviceId() { return deviceId; }
    public double getBatteryPercent() { return batteryPercent; }
    public boolean isCharging() { return charging; }
    public double getAcVoltage() { return acVoltage; }
    public boolean isMotorOn() { return motorOn; }
    public boolean isRelayOn() { return relayOn; }
    public boolean isBuzzerOn() { return buzzerOn; }
    public String getRgbColor() { return rgbColor; }
    public boolean isOledOn() { return oledOn; }
    public boolean isAutoMode() { return autoMode; }
    public int getSignalDbm() { return signalDbm; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
