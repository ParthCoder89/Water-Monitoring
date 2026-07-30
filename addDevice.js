// ==============================
// ADD DEVICE SETUP
// ==============================

let currentStep = 1;

const steps = document.querySelectorAll(".setup-step");
const progress = document.querySelectorAll(".stepper .step");

function showStep(step){

    steps.forEach((item)=>{
        item.classList.remove("active");
    });

    progress.forEach((item)=>{
        item.classList.remove("active");
    });

    document.getElementById("step"+step).classList.add("active");

    for(let i=0;i<step;i++){
        progress[i].classList.add("active");
    }

    currentStep = step;

}

// ==============================
// STEP 1
// Verify Device
// ==============================

document.getElementById("verifyBtn").onclick = function(e){

    e.preventDefault();

    let mac = document.getElementById("macAddress").value.trim();
    let pass = document.getElementById("devicePassword").value.trim();

    if(mac=="" || pass==""){

        alert("Please enter MAC Address and Password");
        return;

    }

    // Backend API yaha call hogi

    showStep(2);

}



// ==============================
// STEP 2
// Device Details
// ==============================

document.getElementById("continue1").onclick = function(e){

    e.preventDefault();

    showStep(3);

}


document.getElementById("back1").onclick = function(e){

    e.preventDefault();

    showStep(1);

}



// ==============================
// STEP 3
// Change Password?
// ==============================

document.getElementById("changePassword").onclick = function(e){

    e.preventDefault();

    showStep(4);

}


document.getElementById("skipPassword").onclick = function(e){

    e.preventDefault();

    showStep(5);

}



// ==============================
// STEP 4
// Save Password
// ==============================

document.getElementById("back2").onclick = function(e){

    e.preventDefault();

    showStep(3);

}


document.getElementById("savePassword").onclick = function(e){

    e.preventDefault();

    let passwordInputs = document.querySelectorAll("#step4 input");

    let current = passwordInputs[0].value.trim();
    let newPass = passwordInputs[1].value.trim();
    let confirm = passwordInputs[2].value.trim();

    if(current=="" || newPass=="" || confirm==""){

        alert("Please fill all fields");
        return;

    }

    if(newPass!==confirm){

        alert("Passwords do not match");
        return;

    }

    // Backend API

    showStep(5);

}



// ==============================
// PASSWORD SHOW / HIDE
// ==============================

const eyeButtons = document.querySelectorAll(".toggle-password");

eyeButtons.forEach((button)=>{

    button.addEventListener("click",()=>{

        let input = button.previousElementSibling;

        if(input.type==="password"){

            input.type="text";
            button.innerHTML='<i class="fa-solid fa-eye-slash"></i>';

        }

        else{

            input.type="password";
            button.innerHTML='<i class="fa-solid fa-eye"></i>';

        }

    });

});



// ==============================
// GO DASHBOARD
// ==============================

const finishBtn = document.querySelector("#step5 .primary-btn");

finishBtn.onclick = function(){

    window.location.href="dashboard.html";

}