package com.neerpilot.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tank_data", indexes = {
        @Index(name = "idx_tank_device_time", columnList = "deviceId,timestamp")
})
public class TankData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String deviceId = "TANK-01";

    private double levelPercent;
    private int capacityLiters = 1000;

    private double batteryPercent;
    private boolean charging;
    private boolean solarCharging;
    private double voltage;

    private int signalDbm;
    private double tempC;
    private double humidity;
    private double ultrasonicCm;
    private int probesWet;

    private boolean connected = true;

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

    public double getLevelPercent() { return levelPercent; }
    public void setLevelPercent(double levelPercent) { this.levelPercent = levelPercent; }

    public int getCapacityLiters() { return capacityLiters; }
    public void setCapacityLiters(int capacityLiters) { this.capacityLiters = capacityLiters; }

    public double getBatteryPercent() { return batteryPercent; }
    public void setBatteryPercent(double batteryPercent) { this.batteryPercent = batteryPercent; }

    public boolean isCharging() { return charging; }
    public void setCharging(boolean charging) { this.charging = charging; }

    public boolean isSolarCharging() { return solarCharging; }
    public void setSolarCharging(boolean solarCharging) { this.solarCharging = solarCharging; }

    public double getVoltage() { return voltage; }
    public void setVoltage(double voltage) { this.voltage = voltage; }

    public int getSignalDbm() { return signalDbm; }
    public void setSignalDbm(int signalDbm) { this.signalDbm = signalDbm; }

    public double getTempC() { return tempC; }
    public void setTempC(double tempC) { this.tempC = tempC; }

    public double getHumidity() { return humidity; }
    public void setHumidity(double humidity) { this.humidity = humidity; }

    public double getUltrasonicCm() { return ultrasonicCm; }
    public void setUltrasonicCm(double ultrasonicCm) { this.ultrasonicCm = ultrasonicCm; }

    public int getProbesWet() { return probesWet; }
    public void setProbesWet(int probesWet) { this.probesWet = probesWet; }

    public boolean isConnected() { return connected; }
    public void setConnected(boolean connected) { this.connected = connected; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
