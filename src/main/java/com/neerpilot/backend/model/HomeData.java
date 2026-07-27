package com.neerpilot.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "home_data", indexes = {
        @Index(name = "idx_home_device_time", columnList = "deviceId,timestamp")
})
public class HomeData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String deviceId = "HOME-01";

    private double batteryPercent;
    private boolean charging;
    private double acVoltage;

    private boolean motorOn;
    private boolean relayOn;
    private boolean buzzerOn;

    @Column(length = 10)
    private String rgbColor = "#00BFFF";

    private boolean oledOn = true;
    private boolean autoMode = true;
    private int signalDbm;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
