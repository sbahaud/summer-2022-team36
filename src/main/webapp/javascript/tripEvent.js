// Get userId for current page
let userId = sessionStorage.getItem("userId");
let userName = sessionStorage.getItem("userName");

var msgDiv = document.getElementById("status-msg");
var submit = document.getElementById("submit-btn");

// set header
let btnLogin = document.getElementById("header-btn")

if (userName === "" || userName === null) {
    btnLogin.innerHTML = `<button class="btn login-btn" onclick="location.href='../pages/userSignin.html'">Log In</button>
    `;
    backToLogin();
} else {
    btnLogin.innerHTML = 
    `<button class="btn name-btn">Hi <span>` + userName + ` !</span>` + 
    `<button class="btn dashboard-btn" onclick="location.href='../pages/dashboard.html'" id="back">Back to Dashboard</button>`;
    createTrip();
}

// Redirect to Login page
function backToLogin() {
    submit.addEventListener("click", function(event) {
        event.preventDefault();

        msgDiv.innerHTML = `<p class="success">Please login first!</p>`;
        
        window.setTimeout(function() {
            window.location.href = "/pages/userSignin.html";
        }, 400);

        // clear input box
        title.value = "";
        fromDate.value = "";
        toDate.value = "";
        participants.value = "";
        budget.value = "";
    });
}

// Create new trip
function createTrip() {
    var title = document.getElementById("text-input-title");
    var fromDate = document.getElementById("text-input-start-date");
    var toDate = document.getElementById("text-input-end-date");
    var participants = document.getElementById("text-input-participants");
    var budget = document.getElementById("text-input-totalBudget");

    submit.addEventListener("click", function(event) {
        event.preventDefault();

        postNewTrip(title.value, fromDate.value, toDate.value, participants.value, budget.value, userId);

        // clear input box
        title.value = "";
        fromDate.value = "";
        toDate.value = "";
        participants.value = "";
        budget.value = "";
    });
}

// Send tripId to database
async function postNewTrip(title, fromDate, toDate, participants, budget, userId) {
    const params = new URLSearchParams();

    params.append('text-input-title', title);
    params.append('text-input-start-date', fromDate);
    params.append('text-input-end-date', toDate);
    params.append('text-input-participants', participants);
    params.append('text-input-totalBudget', budget);
    params.append('text-input-userId', userId);

    const response = await fetch('/NewTrip', {method: 'POST', body: params});
    const responseMsg = await response.text();

    var numbers = /^[+-]?\d+$/;
    if(responseMsg.match(numbers)){
        msgDiv.innerHTML = `<p class="success">Success!</p>`;

        sessionStorage.setItem("tripId", responseMsg);
        
        window.setTimeout(function() {
            window.location.href = "/pages/dashboard.html";
        }, 400);
    
    } else {
        msgDiv.innerHTML = responseMsg;
    }
}