package com.neerpilot.backend.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * JSON body the ESP32 Tank Unit POSTs to /api/tank/data
 * Example:
 * {
 *   "deviceId": "TANK-01",
 *   "levelPercent": 62.4,
 *   "capacityLiters": 1000,
 *   "batteryPercent": 78.2,
 *   "charging": true,
 *   "solarCharging": true,
 *   "voltage": 12.6,
 *   "signalDbm": -58,
 *   "tempC": 29.1,
 *   "humidity": 64.0,
 *   "ultrasonicCm": 38.5,
 *   "probesWet": 3
 * }
 */
public class TankDataRequest {

    @NotBlank
    private String deviceId;

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
}
