/* ==========================================
   HERO SECTION JAVASCRIPT
========================================== */

document.addEventListener("DOMContentLoaded", () => {

    // ==============================
    // HERO CONTENT FADE
    // ==============================

    const heroLeft = document.querySelector(".hero-left");
    const heroRight = document.querySelector(".hero-right");

    heroLeft.style.opacity = "0";
    heroRight.style.opacity = "0";

    setTimeout(() => {

        heroLeft.style.transition = "all .8s ease";
        heroLeft.style.opacity = "1";
        heroLeft.style.transform = "translateX(0)";

    },150);

    setTimeout(() => {

        heroRight.style.transition = "all .9s ease";
        heroRight.style.opacity = "1";
        heroRight.style.transform = "translateX(0)";

    },300);



    // ==============================
    // BUTTON RIPPLE EFFECT
    // ==============================

    const buttons = document.querySelectorAll(".hero-buttons a");

    buttons.forEach(button=>{

        button.addEventListener("click",function(e){

            const circle = document.createElement("span");

            const diameter = Math.max(this.clientWidth,this.clientHeight);

            circle.style.width = diameter+"px";
            circle.style.height = diameter+"px";

            circle.style.left =
            e.clientX-this.getBoundingClientRect().left-diameter/2+"px";

            circle.style.top =
            e.clientY-this.getBoundingClientRect().top-diameter/2+"px";

            circle.classList.add("ripple");

            const ripple=this.getElementsByClassName("ripple")[0];

            if(ripple){

                ripple.remove();

            }

            this.appendChild(circle);

        });

    });



    // ==============================
    // FEATURE CARD HOVER
    // ==============================

    const cards=document.querySelectorAll(".feature-card");

    cards.forEach(card=>{

        card.addEventListener("mouseenter",()=>{

            card.style.transform="translateY(-8px) scale(1.02)";

        });

        card.addEventListener("mouseleave",()=>{

            card.style.transform="translateY(0px) scale(1)";

        });

    });



    // ==============================
    // SCROLL REVEAL
    // ==============================

    const observer=new IntersectionObserver(entries=>{

        entries.forEach(entry=>{

            if(entry.isIntersecting){

                entry.target.classList.add("show");

            }

        });

    },{

        threshold:0.25

    });


    document.querySelectorAll(".feature-card").forEach(card=>{

        observer.observe(card);

    });

});