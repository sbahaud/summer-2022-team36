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
    `<button class="btn redirect-btn" onclick="location.href='../pages/dashboard.html'" id="back">Back to Dashboard</button>`;
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

        postNewTrip(title.value, fromDate.value, toDate.value, participants.value, budget.value, userName, userId);
    });
}

// Send tripId to database
async function postNewTrip(title, fromDate, toDate, participants, budget, userName, userId) {
    const params = new URLSearchParams();

    params.append('text-input-title', title);
    params.append('text-input-start-date', fromDate);
    params.append('text-input-end-date', toDate);
    params.append('text-input-participants', participants);
    params.append('text-input-totalBudget', budget);
    params.append('text-input-userName', userName);
    params.append('text-input-userID', userId);

    console.log(userName+userId);

    const response = await fetch('/NewTrip', {method: 'POST', body: params});
    const responseMsg = await response.text();

    var numbers = /^[+-]?\d+$/;
    if(responseMsg.match(numbers)){
        msgDiv.innerHTML = `<p class="success">Success!</p>`;

        sessionStorage.setItem("tripId", responseMsg);
        sessionStorage.setItem("tripTitle", title);
        
        window.setTimeout(function() {
            window.location.href = "/trip/createEvent.html";
        }, 400);
    
    } else {
        msgDiv.innerHTML = responseMsg;
    }
}

// Limit date ranges
var date = new Date();
var tdate = date.getDate();
var month = date.getMonth() + 1;
var year = date.getUTCFullYear();
if(tdate < 10){
    tdate = '0' + tdate;
}
if(month < 10) {
    month = '0' + month
}
var minDate = year + "-" + month + "-" + tdate
document.getElementById("text-input-start-date").setAttribute('min', minDate)

var fromDate;
$('#text-input-start-date').on('change', function() {
    fromDate = $(this).val();
    $('#text-input-end-date').prop('min', function() {
        return fromDate;
    })
});

var toDate;
$('#text-input-end-date').on('change', function() {
    toDate = $(this).val();
    $('#text-input-start-date').prop('max', function() {
        return toDate;
    })
});