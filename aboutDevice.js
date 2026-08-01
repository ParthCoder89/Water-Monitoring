/* =====================================================================
   ABOUT DEVICE PAGE SCRIPTS — NeerPilot (REDESIGNED — Tab System)
   Vanilla JavaScript only. No external libraries.
   Handles: tab switching (Overview / Tank Unit / Home Unit /
   Comparison / Gallery), sliding glow indicator, "Explore" buttons
   on the always-visible intro cards, scroll reveal animation, and
   button hover ripple effects.
====================================================================== */

document.addEventListener('DOMContentLoaded', function () {

  var root = document.querySelector('.about-device');
  if (!root) return; // Safety check in case the fragment isn't present


  /* -----------------------------------------------------------------
     1. TAB SWITCHING SYSTEM
     Only one panel (.ad-panel) is visible at a time. Switching tabs
     hides the previous panel and fades in the newly selected one.
  ----------------------------------------------------------------- */
  var tabButtons = root.querySelectorAll('.ad-tab');
  var tabNav = document.getElementById('adTabNav');
  var tabGlow = document.getElementById('adTabGlow');
  var panels = root.querySelectorAll('.ad-panel');

  // Moves the glowing pill behind whichever tab is currently active
  function moveGlowToTab(tabEl) {
    if (!tabGlow || !tabEl || !tabNav) return;
    var navRect = tabNav.getBoundingClientRect();
    var tabRect = tabEl.getBoundingClientRect();
    var offsetLeft = tabRect.left - navRect.left;

    tabGlow.style.width = tabRect.width + 'px';
    tabGlow.style.transform = 'translateX(' + offsetLeft + 'px)';
  }

  // Hides a panel after its fade-out transition finishes
  function hidePanel(panel) {
    if (!panel) return;
    panel.classList.remove('ad-panel-visible');
    window.setTimeout(function () {
      if (!panel.classList.contains('ad-panel-visible')) {
        panel.classList.remove('ad-panel-active');
      }
    }, 450);
  }

  // Shows a panel with a fade-in + slide-up transition
  function showPanel(panel) {
    if (!panel) return;
    panel.classList.add('ad-panel-active');
    void panel.offsetWidth; // Force reflow so the transition reliably triggers
    requestAnimationFrame(function () {
      panel.classList.add('ad-panel-visible');
    });

    // Re-run scroll reveal for elements newly shown inside this panel
    observeRevealElements(panel.querySelectorAll('.reveal'));
  }

  function activateTab(tabName) {
    // Update tab button active states
    tabButtons.forEach(function (btn) {
      var isActive = btn.getAttribute('data-tab') === tabName;
      btn.classList.toggle('ad-tab-active', isActive);
      if (isActive) moveGlowToTab(btn);
    });

    // Show the matching panel, hide all others
    panels.forEach(function (panel) {
      if (panel.getAttribute('data-panel') === tabName) {
        showPanel(panel);
      } else {
        hidePanel(panel);
      }
    });

    // Scroll the tab nav into comfortable view (accounts for sticky navbar)
    if (tabNav) {
      var offset = 100;
      var targetPosition = tabNav.getBoundingClientRect().top + window.pageYOffset - offset;
      if (window.pageYOffset > targetPosition) {
        window.scrollTo({ top: targetPosition, behavior: 'smooth' });
      }
    }
  }

  tabButtons.forEach(function (btn) {
    btn.addEventListener('click', function () {
      activateTab(btn.getAttribute('data-tab'));
    });
  });

  // Keep the glow pill aligned correctly on window resize
  window.addEventListener('resize', function () {
    var activeTab = root.querySelector('.ad-tab.ad-tab-active');
    if (activeTab) moveGlowToTab(activeTab);
  });

  // Initialize: make the default active panel visible on page load
  // (without this, the Overview panel stays at opacity:0 until the
  // first tab click, since ad-panel-visible was never added).
  var initialPanel = root.querySelector('.ad-panel.ad-panel-active');
  if (initialPanel) {
    void initialPanel.offsetWidth; // Force reflow so the transition plays
    requestAnimationFrame(function () {
      initialPanel.classList.add('ad-panel-visible');
    });
  }

  // Initialize: Overview tab active, glow positioned, on first load
  window.setTimeout(function () {
    var initialTab = root.querySelector('.ad-tab.ad-tab-active') || tabButtons[0];
    if (initialTab) moveGlowToTab(initialTab);
  }, 50);


  /* -----------------------------------------------------------------
     2. "EXPLORE" BUTTONS ON THE ALWAYS-VISIBLE INTRO CARDS
     Clicking "Explore Tank Unit" / "Explore Home Unit" / "View
     Details" switches directly to that tab.
  ----------------------------------------------------------------- */
  var exploreButtons = root.querySelectorAll('.ad-explore-btn');
  exploreButtons.forEach(function (btn) {
    btn.addEventListener('click', function () {
      activateTab(btn.getAttribute('data-tab'));
    });
  });


  /* -----------------------------------------------------------------
     3. SCROLL REVEAL ANIMATION
     Adds .visible to any .reveal element as it enters the viewport.
     Re-usable so it can also observe elements inside a panel that
     only becomes visible later (after a tab switch).
  ----------------------------------------------------------------- */
  var revealObserver = new IntersectionObserver(function (entries, observer) {
    entries.forEach(function (entry) {
      if (entry.isIntersecting) {
        entry.target.classList.add('visible');
        observer.unobserve(entry.target);
      }
    });
  }, {
    root: null,
    rootMargin: '0px 0px -60px 0px',
    threshold: 0.1
  });

  function observeRevealElements(elements) {
    elements.forEach(function (el) {
      if (!el.classList.contains('visible')) {
        revealObserver.observe(el);
      }
    });
  }

  // Observe everything visible on initial load (header, intro cards,
  // tab nav, and the default Overview panel)
  observeRevealElements(root.querySelectorAll('.reveal'));


  /* -----------------------------------------------------------------
     4. GALLERY STAGGERED ENTRY
     Gallery items already fade in via .reveal; this adds a subtle
     staggered delay so they animate in sequence rather than at once.
  ----------------------------------------------------------------- */
  var galleryItems = root.querySelectorAll('.ad-gallery-item');
  galleryItems.forEach(function (item, index) {
    item.style.transitionDelay = (index * 70) + 'ms';
  });


  /* -----------------------------------------------------------------
     5. BUTTON HOVER / PRESS RIPPLE + GLOW EFFECT
     Lightweight visual feedback on primary buttons.
  ----------------------------------------------------------------- */
  var rippleButtons = root.querySelectorAll('.ad-btn-primary, .ad-btn-secondary');

  rippleButtons.forEach(function (btn) {
    btn.addEventListener('click', function (e) {
      var ripple = document.createElement('span');
      var rect = btn.getBoundingClientRect();
      var size = Math.max(rect.width, rect.height);

      ripple.style.position = 'absolute';
      ripple.style.borderRadius = '50%';
      ripple.style.background = 'rgba(255, 255, 255, 0.35)';
      ripple.style.width = ripple.style.height = size + 'px';
      ripple.style.left = (e.clientX - rect.left - size / 2) + 'px';
      ripple.style.top = (e.clientY - rect.top - size / 2) + 'px';
      ripple.style.pointerEvents = 'none';
      ripple.style.transform = 'scale(0)';
      ripple.style.transition = 'transform 0.6s ease, opacity 0.6s ease';
      ripple.style.opacity = '1';

      btn.style.position = 'relative';
      btn.style.overflow = 'hidden';
      btn.appendChild(ripple);

      requestAnimationFrame(function () {
        ripple.style.transform = 'scale(2.2)';
        ripple.style.opacity = '0';
      });

      window.setTimeout(function () {
        ripple.remove();
      }, 650);
    });
  });

});