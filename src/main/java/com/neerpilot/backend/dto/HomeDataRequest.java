package com.neerpilot.backend.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * JSON body the ESP32 Home Unit POSTs to /api/home/data
 * Example:
 * {
 *   "deviceId": "HOME-01",
 *   "batteryPercent": 54.0,
 *   "charging": false,
 *   "acVoltage": 221.0,
 *   "motorOn": false,
 *   "relayOn": false,
 *   "buzzerOn": false,
 *   "rgbColor": "#00BFFF",
 *   "oledOn": true,
 *   "autoMode": true,
 *   "signalDbm": -66
 * }
 */
public class HomeDataRequest {

    @NotBlank
    private String deviceId;

    private double batteryPercent;
    private boolean charging;
    private double acVoltage;
    private boolean motorOn;
    private boolean relayOn;
    private boolean buzzerOn;
    private String rgbColor = "#00BFFF";
    private boolean oledOn = true;
    private boolean autoMode = true;
    private int signalDbm;

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public double getBatteryPercent() { return batteryPercent; }
    public void setBatteryPercent(double batteryPercent) { this.batteryPercent = batteryPercent; }

    public boolean isCharging() { return charging; }
    public void setCharging(boolean charging) { this.charging = charging; }

    public double getAcVoltage() { return acVoltage; }
    public void setAcVoltage(double acVoltage) { this.acVoltage = acVoltage; }

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

    public int getSignalDbm() { return signalDbm; }
    public void setSignalDbm(int signalDbm) { this.signalDbm = signalDbm; }
}
