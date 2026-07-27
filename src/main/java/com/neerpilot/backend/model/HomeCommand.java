package com.neerpilot.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Holds the latest desired state for the Home Unit's actuators.
 * The dashboard writes to this via POST /api/home/control.
 * The ESP32 polls GET /api/home/command to fetch and apply it.
 */
@Entity
@Table(name = "home_command")
public class HomeCommand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String deviceId = "HOME-01";

    private boolean motorOn;
    private boolean relayOn;
    private boolean buzzerOn;

    @Column(length = 10)
    private String rgbColor = "#00BFFF";

    private boolean oledOn = true;
    private boolean autoMode = true;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onSave() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
