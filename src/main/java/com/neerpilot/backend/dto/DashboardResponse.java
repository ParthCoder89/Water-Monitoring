package com.neerpilot.backend.dto;

/**
 * One-shot combined payload for the dashboard's initial load,
 * mirroring the `state` object shape used in dashboard.js.
 */
public class DashboardResponse {

    private boolean connected;
    private TankDataResponse tank;
    private HomeDataResponse home;

    public DashboardResponse(boolean connected, TankDataResponse tank, HomeDataResponse home) {
        this.connected = connected;
        this.tank = tank;
        this.home = home;
    }

    public boolean isConnected() { return connected; }
    public TankDataResponse getTank() { return tank; }
    public HomeDataResponse getHome() { return home; }
}
