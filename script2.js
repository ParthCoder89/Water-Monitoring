/* =======================================================================
   NeerPilot Dashboard — script.js
   Vanilla JS: simulated telemetry, DOM animation, interactivity
   ======================================================================= */


     // ME

const menuBtn = document.querySelector(".menu-btn");
const rightLeft = document.querySelector(".right-left");

menuBtn.addEventListener("click", (e) => {
    e.stopPropagation();
    rightLeft.classList.toggle("active");
});

rightLeft.addEventListener("click", (e) => {
    e.stopPropagation();
});

document.addEventListener("click", () => {
    rightLeft.classList.remove("active");
});

  

(() => {
  'use strict';

  /* ---------------------------------------------------------------------
     STATE — single source of truth for the simulated IoT node
     --------------------------------------------------------------------- */
  const state = {
    connected: true,
    tank: {
      levelPercent: 62,
      capacityLiters: 1000,
      batteryPercent: 78,
      charging: true,
      solarCharging: true,
      voltage: 12.6,
      signalDbm: -58,
      tempC: 29,
      humidity: 64,
      ultrasonicCm: 38,
      probesWet: 3
    },
    home: {
      batteryPercent: 54,
      charging: false,
      acVoltage: 221,
      motorOn: false,
      relayOn: false,
      buzzerOn: false,
      rgbColor: '#00BFFF',
      oledOn: true,
      autoMode: true,
      signalDbm: -66
    }
  };

  let logCount = 0;

  /* ---------------------------------------------------------------------
     UTILITIES
     --------------------------------------------------------------------- */
  const $ = (id) => document.getElementById(id);
  const clamp = (v, min, max) => Math.max(min, Math.min(max, v));
  const jitter = (value, range, min, max) => clamp(value + (Math.random() * 2 - 1) * range, min, max);

  function pad(n) { return n.toString().padStart(2, '0'); }



  /* ---------------------------------------------------------------------
     CLOCK — live time & date in navbar
     --------------------------------------------------------------------- */
  function updateClock() {
    const now = new Date();
    const h = pad(now.getHours());
    const m = pad(now.getMinutes());
    const s = pad(now.getSeconds());
    $('liveTime').textContent = `${h}:${m}:${s}`;

    const days = ['Sun','Mon','Tue','Wed','Thu','Fri','Sat'];
    const months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
    $('liveDate').textContent = `${days[now.getDay()]}, ${now.getDate()} ${months[now.getMonth()]} ${now.getFullYear()}`;
  }

  /* ---------------------------------------------------------------------
     TANK RENDERING
     --------------------------------------------------------------------- */
  function renderTank() {
    const t = state.tank;
    $('tankWater').style.height = `${t.levelPercent}%`;
    $('tankPercent').textContent = `${Math.round(t.levelPercent)}%`;
    $('tankCapacity').textContent = `${t.capacityLiters} L`;

    // Overflow status
    const overflowEl = $('overflowStatus');
    if (t.levelPercent >= 97) {
      overflowEl.textContent = 'Overflow';
      overflowEl.className = 'substat-value status-danger';
    } else if (t.levelPercent >= 88) {
      overflowEl.textContent = 'Near Full';
      overflowEl.className = 'substat-value status-warn';
    } else {
      overflowEl.textContent = 'Normal';
      overflowEl.className = 'substat-value status-ok';
    }

    // Dry tank status
    const dryEl = $('dryStatus');
    if (t.levelPercent <= 5) {
      dryEl.textContent = 'Yes';
      dryEl.className = 'substat-value status-danger';
    } else {
      dryEl.textContent = 'No';
      dryEl.className = 'substat-value status-ok';
    }
  }

  function renderBattery(prefix, percent, charging) {
    const liquid = $(`${prefix}BatteryLiquid`);
    const percentEl = $(`${prefix}BatteryPercent`);
    const chargeIcon = $(`${prefix}ChargeIcon`);
    const statusEl = $(`${prefix}BatteryStatus`);

    liquid.style.height = `${percent}%`;
    percentEl.textContent = `${Math.round(percent)}%`;

    liquid.classList.remove('low', 'mid');
    if (percent < 25) liquid.classList.add('low');
    else if (percent < 55) liquid.classList.add('mid');

    chargeIcon.classList.toggle('active', charging);
    statusEl.textContent = charging ? 'Charging' : (percent < 20 ? 'Low Power' : 'Discharging');
  }

  function renderSolar() {
    const t = state.tank;
    const visual = $('solarVisual');
    const statusText = $('solarStatusText');
    visual.classList.toggle('charging', t.solarCharging);
    statusText.textContent = t.solarCharging ? 'Charging Active' : 'No Sunlight';
    statusText.classList.toggle('on', t.solarCharging);
  }

  function renderSignalBars(containerId, dbm) {
    // dBm roughly -30 (excellent) to -90 (poor)
    const strength = clamp(Math.round((dbm + 90) / 15), 0, 4); // 0..4 bars
    const bars = document.querySelectorAll(`#${containerId} span`);
    bars.forEach((bar, idx) => bar.classList.toggle('active', idx < strength));
  }

  function renderTankSensors() {
    const t = state.tank;
    $('ultrasonicValue').textContent = `${t.ultrasonicCm.toFixed(1)} cm`;
    $('probeStatus').textContent = `${t.probesWet} / 3 Wet`;
    $('tankVoltage').textContent = `${t.voltage.toFixed(2)} V`;
    $('tankSignal').textContent = `${t.signalDbm} dBm`;
    $('tankTemp').textContent = `${t.tempC.toFixed(1)} °C`;
    $('tankHumidity').textContent = `${t.humidity.toFixed(0)} %`;
    renderSignalBars('tankSignalBars', t.signalDbm);

    const espnowEl = $('espnowStatus');
    espnowEl.textContent = state.connected ? 'Synced' : 'Lost Link';
    espnowEl.className = state.connected ? 'mini-value status-ok' : 'mini-value status-danger';
  }

  /* ---------------------------------------------------------------------
     HOME UNIT RENDERING
     --------------------------------------------------------------------- */
  function renderVoltageGauge() {
    const v = state.home.acVoltage;
    const circumference = 427; // 2 * PI * 68
    // map 150V-280V range to 0-100% arc for a natural gauge sweep
    const pct = clamp((v - 150) / (280 - 150), 0, 1);
    const offset = circumference - pct * circumference;
    const fill = $('voltageGaugeFill');
    fill.style.strokeDashoffset = offset;

    $('voltageValue').textContent = `${Math.round(v)}V`;
    const caption = $('voltageCaption');

    if (v < 190) {
      fill.style.stroke = 'var(--c-danger)';
      caption.textContent = 'Under Voltage';
      caption.style.color = 'var(--c-danger)';
    } else if (v > 250) {
      fill.style.stroke = 'var(--c-warning)';
      caption.textContent = 'Over Voltage';
      caption.style.color = 'var(--c-warning)';
    } else {
      fill.style.stroke = 'var(--c-accent)';
      caption.textContent = 'Nominal';
      caption.style.color = 'var(--c-accent)';
    }
  }

  function renderMotor() {
    const on = state.home.motorOn;
    $('motorBlades').classList.toggle('spinning', on);
    $('motorStateText').textContent = on ? 'RUNNING' : 'OFF';
    $('motorToggle').checked = on;
  }

  function renderRelay() {
    const on = state.home.relayOn;
    $('relayBody').classList.toggle('energized', on);
    $('relayStateText').textContent = on ? 'ENERGIZED' : 'OFF';
    $('relayToggle').checked = on;
  }

  function renderBuzzer() {
    const on = state.home.buzzerOn;
    $('buzzerVisual').classList.toggle('buzzing', on);
    $('buzzerStateText').textContent = on ? 'Sounding' : 'Silent';
    $('buzzerToggle').checked = on;
  }

  function renderRGB() {
    const orb = $('rgbOrb');
    orb.style.background = state.home.rgbColor;
    orb.style.boxShadow = `0 0 24px ${state.home.rgbColor}`;
    $('rgbStateText').textContent = state.home.rgbColor.toUpperCase();
  }

  function renderOLED() {
    const on = state.home.oledOn;
    $('oledVisual').querySelector('.oled-screen').classList.toggle('off', !on);
    $('oledStateText').textContent = on ? 'ON' : 'OFF';
    $('oledToggle').checked = on;
    $('oledText').textContent = on ? (state.home.autoMode ? 'SYSTEM OK · AUTO' : 'SYSTEM OK · MANUAL') : '';
  }

  function renderMode() {
    $('modeText').textContent = state.home.autoMode ? 'AUTO' : 'MANUAL';
    $('modeToggle').checked = !state.home.autoMode;
    renderOLED();
  }

  function renderHomeSignal() {
    $('homeSignalValue').textContent = `${state.home.signalDbm} dBm`;
    renderSignalBars('homeSignalBars', state.home.signalDbm);
  }

  function renderConnection() {
    const dot = $('connDot');
    const text = $('connText');
    dot.classList.toggle('offline', !state.connected);
    text.textContent = state.connected ? 'Online' : 'Reconnecting…';
  }

  function renderAll() {
    renderTank();
    renderBattery('tank', state.tank.batteryPercent, state.tank.charging);
    renderSolar();
    renderTankSensors();

    renderBattery('home', state.home.batteryPercent, state.home.charging);
    renderVoltageGauge();
    renderMotor();
    renderRelay();
    renderBuzzer();
    renderRGB();
    renderMode();
    renderHomeSignal();
    renderConnection();
  }

  /* ---------------------------------------------------------------------
     LOGGING — newest entry on top, auto-scrolled, capped length
     --------------------------------------------------------------------- */
  const LOG_TEMPLATES = [
    { level: 'ok',     text: 'Tank telemetry synced via ESP-NOW.' },
    { level: 'ok',     text: 'Solar controller reporting nominal input.' },
    { level: 'warn',   text: 'Water level approaching upper threshold.' },
    { level: 'ok',     text: 'Home unit heartbeat received.' },
    { level: 'danger', text: 'Signal degradation on tank node link.' },
    { level: 'ok',     text: 'Motor relay cycle completed successfully.' },
    { level: 'warn',   text: 'Battery voltage dipped below optimal range.' },
    { level: 'ok',     text: 'AC mains voltage stable at nominal band.' },
    { level: 'ok',     text: 'Ultrasonic sensor calibration check passed.' },
    { level: 'danger', text: 'Water probe reported dry condition.' }
  ];

  function pushLog(entry) {
    const win = $('logWindow');
    const now = new Date();
    const time = `${pad(now.getHours())}:${pad(now.getMinutes())}:${pad(now.getSeconds())}`;

    const row = document.createElement('div');
    row.className = `log-entry level-${entry.level}`;
    row.innerHTML = `<span class="log-time">[${time}]</span><span class="log-text">${entry.text}</span>`;

    // column-reverse layout means "prepend" visually shows on top
    win.prepend(row);

    // cap the log list so DOM doesn't grow unbounded
    while (win.children.length > 40) {
      win.removeChild(win.lastChild);
    }

    logCount++;
    $('logCounter').textContent = `${logCount} event${logCount === 1 ? '' : 's'}`;

    // keep the scroll pinned to the newest entry
    win.scrollTop = 0;
  }

  function randomLog() {
    const template = LOG_TEMPLATES[Math.floor(Math.random() * LOG_TEMPLATES.length)];
    pushLog(template);
  }

  /* ---------------------------------------------------------------------
     SIMULATED TELEMETRY — every 3 seconds
     --------------------------------------------------------------------- */
  function simulateTick() {
    const t = state.tank;
    const h = state.home;

    // Tank level drifts, occasionally direction-biased
    t.levelPercent = jitter(t.levelPercent, 2.5, 0, 100);
    t.ultrasonicCm = jitter(t.ultrasonicCm, 1.2, 4, 120);
    t.probesWet = t.levelPercent > 8 ? 3 : (t.levelPercent > 4 ? 2 : 0);

    // Tank battery — charges when solar is active, drains otherwise
    t.solarCharging = Math.random() > 0.25; // mostly charging during "day" simulation
    t.charging = t.solarCharging && t.batteryPercent < 100;
    t.batteryPercent = clamp(t.batteryPercent + (t.charging ? 0.8 : -0.4), 0, 100);
    t.voltage = jitter(t.voltage, 0.05, 11.5, 13.4);

    t.signalDbm = Math.round(jitter(t.signalDbm, 4, -95, -35));
    t.tempC = jitter(t.tempC, 0.4, 22, 38);
    t.humidity = jitter(t.humidity, 1.5, 35, 90);

    // Home battery + AC voltage
    h.charging = Math.random() > 0.5;
    h.batteryPercent = clamp(h.batteryPercent + (h.charging ? 0.6 : -0.3), 0, 100);
    h.acVoltage = jitter(h.acVoltage, 6, 170, 265);
    h.signalDbm = Math.round(jitter(h.signalDbm, 3, -95, -40));

    // Occasionally auto-flip a control to feel "alive" if in AUTO mode
    if (h.autoMode && Math.random() > 0.7) {
      h.motorOn = t.levelPercent < 30 ? true : (t.levelPercent > 85 ? false : h.motorOn);
      h.relayOn = h.motorOn;
    }

    // Connection occasionally blips
    state.connected = Math.random() > 0.04;

    renderAll();

    // Occasional log line
    if (Math.random() > 0.35) randomLog();
  }

  /* ---------------------------------------------------------------------
     USER CONTROLS — manual toggles
     --------------------------------------------------------------------- */
  function bindControls() {
    $('motorToggle').addEventListener('change', (e) => {
      state.home.motorOn = e.target.checked;
      state.home.relayOn = state.home.motorOn;
      renderMotor();
      renderRelay();
      pushLog({ level: 'ok', text: `Motor manually switched ${state.home.motorOn ? 'ON' : 'OFF'}.` });
    });

    $('relayToggle').addEventListener('change', (e) => {
      state.home.relayOn = e.target.checked;
      renderRelay();
      pushLog({ level: 'ok', text: `Relay manually switched ${state.home.relayOn ? 'ON' : 'OFF'}.` });
    });

    $('buzzerToggle').addEventListener('change', (e) => {
      state.home.buzzerOn = e.target.checked;
      renderBuzzer();
      pushLog({ level: state.home.buzzerOn ? 'warn' : 'ok', text: `Buzzer ${state.home.buzzerOn ? 'activated' : 'silenced'}.` });
    });

    $('oledToggle').addEventListener('change', (e) => {
      state.home.oledOn = e.target.checked;
      renderOLED();
      pushLog({ level: 'ok', text: `OLED display turned ${state.home.oledOn ? 'ON' : 'OFF'}.` });
    });

    $('modeToggle').addEventListener('change', (e) => {
      state.home.autoMode = !e.target.checked;
      renderMode();
      pushLog({ level: 'ok', text: `Control mode set to ${state.home.autoMode ? 'AUTO' : 'MANUAL'}.` });
    });

    // Cycle RGB color on click for a bit of interactivity
    $('rgbOrb').addEventListener('click', () => {
      const palette = ['#00BFFF', '#00E5FF', '#00FF99', '#FFA733', '#FF3B5C'];
      const current = palette.indexOf(state.home.rgbColor);
      state.home.rgbColor = palette[(current + 1) % palette.length];
      renderRGB();
    });
  }

  /* ---------------------------------------------------------------------
     RIPPLE CLICK EFFECT — applied to any .ripple element
     --------------------------------------------------------------------- */
  function bindRipples() {
    document.querySelectorAll('.ripple').forEach((el) => {
      el.addEventListener('click', (e) => {
        const rect = el.getBoundingClientRect();
        const ripple = document.createElement('span');
        const size = Math.max(rect.width, rect.height);
        ripple.className = 'ripple-effect';
        ripple.style.width = ripple.style.height = `${size}px`;
        ripple.style.left = `${e.clientX - rect.left - size / 2}px`;
        ripple.style.top = `${e.clientY - rect.top - size / 2}px`;
        el.appendChild(ripple);
        setTimeout(() => ripple.remove(), 650);
      });
    });
  }

  /* ---------------------------------------------------------------------
     AMBIENT BACKGROUND — floating bubbles + canvas particles
     --------------------------------------------------------------------- */
  function spawnBubbles() {
    const field = $('bubbleField');
    const count = window.innerWidth < 720 ? 12 : 22;
    for (let i = 0; i < count; i++) {
      const bubble = document.createElement('div');
      bubble.className = 'bubble';
      const size = 6 + Math.random() * 22;
      bubble.style.width = `${size}px`;
      bubble.style.height = `${size}px`;
      bubble.style.left = `${Math.random() * 100}%`;
      bubble.style.animationDuration = `${10 + Math.random() * 14}s`;
      bubble.style.animationDelay = `${Math.random() * 14}s`;
      field.appendChild(bubble);
    }
  }

  function initParticles() {
    const canvas = $('particleCanvas');
    const ctx = canvas.getContext('2d');
    let particles = [];

    function resize() {
      canvas.width = window.innerWidth;
      canvas.height = window.innerHeight;
    }

    function createParticles() {
      const count = window.innerWidth < 720 ? 35 : 70;
      particles = Array.from({ length: count }, () => ({
        x: Math.random() * canvas.width,
        y: Math.random() * canvas.height,
        r: Math.random() * 1.8 + 0.4,
        vx: (Math.random() - 0.5) * 0.15,
        vy: (Math.random() - 0.5) * 0.15,
        alpha: Math.random() * 0.5 + 0.1
      }));
    }

    function tick() {
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      particles.forEach((p) => {
        p.x += p.vx;
        p.y += p.vy;
        if (p.x < 0) p.x = canvas.width;
        if (p.x > canvas.width) p.x = 0;
        if (p.y < 0) p.y = canvas.height;
        if (p.y > canvas.height) p.y = 0;

        ctx.beginPath();
        ctx.arc(p.x, p.y, p.r, 0, Math.PI * 2);
        ctx.fillStyle = `rgba(0, 229, 255, ${p.alpha})`;
        ctx.fill();
      });
      requestAnimationFrame(tick);
    }

    resize();
    createParticles();
    tick();

    window.addEventListener('resize', () => {
      resize();
      createParticles();
    });
  }

  /* ---------------------------------------------------------------------
     INIT
     --------------------------------------------------------------------- */
  function init() {
    updateClock();
    setInterval(updateClock, 1000);

    spawnBubbles();
    initParticles();
    bindControls();
    bindRipples();

    renderAll();
    pushLog({ level: 'ok', text: 'NeerPilot control core initialized.' });
    pushLog({ level: 'ok', text: 'Establishing ESP-NOW mesh with field nodes…' });

    setInterval(simulateTick, 3000);
  }

  document.addEventListener('DOMContentLoaded', init);
})();
