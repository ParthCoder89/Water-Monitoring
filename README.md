# NeerPilot Backend (Spring Boot)

Backend for the NeerPilot Tank + Home Unit dashboard. ESP32 devices push telemetry
over HTTP/JSON, data is stored in PostgreSQL, and the dashboard reads it back
via a JWT-secured REST API (with optional WebSocket push for live updates).

## 1. Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 13+

## 2. Create the database

```sql
CREATE DATABASE neerpilot_db;
```

Tables are auto-created by Hibernate (`spring.jpa.hibernate.ddl-auto=update`) on first run.

## 3. Configure

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/neerpilot_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

jwt.secret=<base64 string, 32+ bytes>   # generate: openssl rand -base64 64
device.api.key=<a long random string shared with your ESP32 firmware>

cors.allowed-origins=http://localhost:5500,http://127.0.0.1:5500
```

## 4. Run

```bash
mvn spring-boot:run
```

API is served at `http://localhost:8080`.

## 5. Authentication model

Two separate auth mechanisms:

| Caller            | Header                          | Endpoints protected                                    |
|--------------------|----------------------------------|---------------------------------------------------------|
| Dashboard (browser)| `Authorization: Bearer <jwt>`   | everything except `/api/auth/**` and device endpoints  |
| ESP32 devices      | `X-API-KEY: <device.api.key>`   | `POST /api/tank/data`, `POST /api/home/data`, `GET /api/home/command` |

## 6. Endpoints

### Auth
- `POST /api/auth/signup` `{username, email, password}` → `{token, username, email, role}`
- `POST /api/auth/login` `{username, password}` → `{token, username, email, role}`

### Tank Unit
- `POST /api/tank/data` *(device key)* — ESP32 pushes a telemetry sample
- `GET /api/tank/latest?deviceId=TANK-01` *(JWT)* — dashboard reads latest sample
- `GET /api/tank/history?deviceId=TANK-01&limit=50` *(JWT)*

### Home Unit
- `POST /api/home/data` *(device key)* — ESP32 pushes telemetry (battery, AC voltage, current actuator state, signal)
- `GET /api/home/latest?deviceId=HOME-01` *(JWT)*
- `GET /api/home/history?deviceId=HOME-01&limit=50` *(JWT)*
- `POST /api/home/control` *(JWT)* — dashboard toggle (motor/relay/buzzer/oled/rgb/mode) writes the desired state
- `GET /api/home/command?deviceId=HOME-01` *(device key)* — ESP32 polls this to know what to actuate

### Combined / misc
- `GET /api/dashboard/summary?tankDeviceId=TANK-01&homeDeviceId=HOME-01` *(JWT)* — one-shot payload for initial page load
- `GET /api/logs/recent?limit=40` *(JWT)* — event log panel

### Real-time (optional)
STOMP over SockJS at `ws://localhost:8080/ws`. Topics: `/topic/tank`, `/topic/home`, `/topic/home-command`, `/topic/logs`.
Every successful POST from the ESP32 (or a control change from the dashboard) is also broadcast here, so the frontend
can subscribe instead of polling every 3 seconds.

## 7. Example: ESP32 → Tank telemetry

```
POST /api/tank/data
Content-Type: application/json
X-API-KEY: <device.api.key>

{
  "deviceId": "TANK-01",
  "levelPercent": 62.4,
  "capacityLiters": 1000,
  "batteryPercent": 78.2,
  "charging": true,
  "solarCharging": true,
  "voltage": 12.6,
  "signalDbm": -58,
  "tempC": 29.1,
  "humidity": 64.0,
  "ultrasonicCm": 38.5,
  "probesWet": 3
}
```

Arduino/ESP32 sketch snippet:

```cpp
#include <WiFi.h>
#include <HTTPClient.h>

void sendTankData() {
  HTTPClient http;
  http.begin("http://YOUR_SERVER:8080/api/tank/data");
  http.addHeader("Content-Type", "application/json");
  http.addHeader("X-API-KEY", "YOUR_DEVICE_KEY");

  String payload = "{\"deviceId\":\"TANK-01\","
                    "\"levelPercent\":" + String(levelPercent) + ","
                    "\"capacityLiters\":1000,"
                    "\"batteryPercent\":" + String(batteryPercent) + ","
                    "\"charging\":" + String(charging ? "true" : "false") + ","
                    "\"solarCharging\":" + String(solar ? "true" : "false") + ","
                    "\"voltage\":" + String(voltage) + ","
                    "\"signalDbm\":" + String(WiFi.RSSI()) + ","
                    "\"tempC\":" + String(tempC) + ","
                    "\"humidity\":" + String(humidity) + ","
                    "\"ultrasonicCm\":" + String(ultrasonicCm) + ","
                    "\"probesWet\":" + String(probesWet) + "}";

  int code = http.POST(payload);
  http.end();
}
```

### ESP32 Home Unit polling for commands

```
GET /api/home/command?deviceId=HOME-01
X-API-KEY: <device.api.key>
```
Response tells the ESP32 what state the motor/relay/buzzer/oled/rgb/mode should be in;
apply it to the relevant GPIO pins each poll cycle (e.g. every 2–3s).

## 8. Frontend integration notes

Replace the simulated `state` object and `simulateTick()` in `dashboard.js` with real calls:

```js
const API = 'http://localhost:8080';
let token = localStorage.getItem('token'); // set after login

async function loadDashboard() {
  const res = await fetch(`${API}/api/dashboard/summary`, {
    headers: { Authorization: `Bearer ${token}` }
  });
  const data = await res.json();
  state.connected = data.connected;
  Object.assign(state.tank, data.tank);
  Object.assign(state.home, data.home);
  renderAll();
}

// Toggle example (motor)
$('motorToggle').addEventListener('change', async (e) => {
  await fetch(`${API}/api/home/control`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${token}` },
    body: JSON.stringify({ deviceId: 'HOME-01', motorOn: e.target.checked, relayOn: e.target.checked,
                            buzzerOn: state.home.buzzerOn, rgbColor: state.home.rgbColor,
                            oledOn: state.home.oledOn, autoMode: state.home.autoMode })
  });
});
```

For real-time updates, connect with SockJS + STOMP:

```html
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.6.1/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script>
  const socket = new SockJS(`${API}/ws`);
  const stomp = Stomp.over(socket);
  stomp.connect({}, () => {
    stomp.subscribe('/topic/tank', msg => { Object.assign(state.tank, JSON.parse(msg.body)); renderTank(); });
    stomp.subscribe('/topic/home', msg => { Object.assign(state.home, JSON.parse(msg.body)); renderMotor(); /* etc */ });
    stomp.subscribe('/topic/logs', msg => pushLog(JSON.parse(msg.body)));
  });
</script>
```

## 9. Project layout

```
src/main/java/com/neerpilot/backend/
  config/       SecurityConfig, WebSocketConfig
  security/     JwtUtil, JwtAuthFilter, DeviceApiKeyFilter, CustomUserDetailsService
  model/        User, Role, TankData, HomeData, HomeCommand, DeviceLog  (JPA entities)
  repository/   Spring Data JPA repositories
  dto/          Request/response payloads
  service/      AuthService, TankService, HomeService, LogService
  controller/   AuthController, TankController, HomeController, DashboardController, LogController
  exception/    ApiException, GlobalExceptionHandler
```
