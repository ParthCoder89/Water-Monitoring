package com.neerpilot.backend.dto;

import com.neerpilot.backend.model.TankData;
import java.time.LocalDateTime;

public class TankDataResponse {

    private String deviceId;
    private double levelPercent;
    private int capacityLiters;
    private double batteryPercent;
    private boolean charging;
    private boolean solarCharging;
    private double voltage;
    private int signalDbm;
    private double tempC;
    private double humidity;
    private double ultrasonicCm;
    private int probesWet;
    private boolean connected;
    private LocalDateTime timestamp;

    public static TankDataResponse fromEntity(TankData t) {
        TankDataResponse r = new TankDataResponse();
        r.deviceId = t.getDeviceId();
        r.levelPercent = t.getLevelPercent();
        r.capacityLiters = t.getCapacityLiters();
        r.batteryPercent = t.getBatteryPercent();
        r.charging = t.isCharging();
        r.solarCharging = t.isSolarCharging();
        r.voltage = t.getVoltage();
        r.signalDbm = t.getSignalDbm();
        r.tempC = t.getTempC();
        r.humidity = t.getHumidity();
        r.ultrasonicCm = t.getUltrasonicCm();
        r.probesWet = t.getProbesWet();
        r.connected = t.isConnected();
        r.timestamp = t.getTimestamp();
        return r;
    }

    public String getDeviceId() { return deviceId; }
    public double getLevelPercent() { return levelPercent; }
    public int getCapacityLiters() { return capacityLiters; }
    public double getBatteryPercent() { return batteryPercent; }
    public boolean isCharging() { return charging; }
    public boolean isSolarCharging() { return solarCharging; }
    public double getVoltage() { return voltage; }
    public int getSignalDbm() { return signalDbm; }
    public double getTempC() { return tempC; }
    public double getHumidity() { return humidity; }
    public double getUltrasonicCm() { return ultrasonicCm; }
    public int getProbesWet() { return probesWet; }
    public boolean isConnected() { return connected; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
