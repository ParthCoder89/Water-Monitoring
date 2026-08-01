/* =====================================================================
   NEERPILOT FOOTER SCRIPTS
   Vanilla JavaScript only. Handles:
     - Fade-up animation on load / scroll into view
     - Current year auto-update
     - Hover interaction helpers (ripple-free, CSS-driven; JS only
       adds/removes state classes where needed)
====================================================================== */

document.addEventListener('DOMContentLoaded', function () {

  var footer = document.getElementById('npFooter');
  if (!footer) return; // Safety check in case the fragment isn't present

  /* -----------------------------------------------------------------
     1. CURRENT YEAR
  ----------------------------------------------------------------- */
  var yearEl = document.getElementById('npYear');
  if (yearEl) {
    yearEl.textContent = new Date().getFullYear();
  }


  /* -----------------------------------------------------------------
     2. FADE-UP ANIMATION ON LOAD / SCROLL INTO VIEW
     Reveals each footer block (.np-fade) once it's visible,
     with a small staggered delay for a premium feel.
  ----------------------------------------------------------------- */
  var fadeEls = footer.querySelectorAll('.np-fade');

  fadeEls.forEach(function (el, index) {
    el.style.transitionDelay = (index * 100) + 'ms';
  });

  var fadeObserver = new IntersectionObserver(function (entries, observer) {
    entries.forEach(function (entry) {
      if (entry.isIntersecting) {
        entry.target.classList.add('np-visible');
        observer.unobserve(entry.target);
      }
    });
  }, {
    root: null,
    threshold: 0.15
  });

  fadeEls.forEach(function (el) {
    fadeObserver.observe(el);
  });


  /* -----------------------------------------------------------------
     3. HOVER INTERACTIONS
     Adds a subtle "active" class while hovering social icons and
     feature/contact rows, giving hooks for any extra JS-driven
     effects beyond what CSS :hover already provides.
  ----------------------------------------------------------------- */
  var hoverTargets = footer.querySelectorAll(
    '.np-social-icon, .np-feature-list li, .np-contact-list li, .np-col'
  );

  hoverTargets.forEach(function (el) {
    el.addEventListener('mouseenter', function () {
      el.classList.add('np-hovered');
    });
    el.addEventListener('mouseleave', function () {
      el.classList.remove('np-hovered');
    });
  });

});