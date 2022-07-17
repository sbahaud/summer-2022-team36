// Get userId and userName for current page
let userId = sessionStorage.getItem("userId");
let userName = sessionStorage.getItem("userName");

// Get tripId and tripName for current page
let tripId = sessionStorage.getItem("tripId");
let tripTitle = sessionStorage.getItem("tripTitle");
console.log("tripId: " + tripId);

var msgDiv = document.getElementById("status-msg");
var submit = document.getElementById("submit-btn");

// set header
// Show selected user name in header
let btnLogin = document.getElementById("header-btn")

if (userName === "" || userName === null) {
    btnLogin.innerHTML =
    `<button class="btn login-btn" onclick="location.href='../pages/userSignin.html'">Log In</button>
    `;
    backToLogin();
} else {
    btnLogin.innerHTML =
    `<button class="btn name-btn">Hi <span>` + userName + ` !</span>` + selectTrip() +
    `<button class="btn redirect-btn" onclick="location.href='../pages/dashboard.html'" id="back">Back to Dashboard</button>`;
    createEvent();
}

// Show selected trip in header
function selectTrip() {
    if (tripId === "" || tripId === null) {
        console.log("tripId is null");
        return `<button class="btn redirect-btn onclick="location.href='../pages/dashboard.html'">Select a trip</button>`;
    } else {
        console.log("tripId EXIST!");
        fetchTripDates();
        return `<button class="btn name-btn">Current trip: <span>` + tripTitle + ` </span></button>`;
    }
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

// Create new Event
function createEvent() {
    var title = document.getElementById("input-title");
    var date = document.getElementById("input-date");
    var location = document.getElementById("input-location");
    var budget = document.getElementById("input-estimatedCost");

    submit.addEventListener("click", function(event) {
        event.preventDefault();

        postNewEvent(tripId, title.value, date.value, location.value, budget.value);
    });
}

// Send eventId to database
async function postNewEvent(tripId, title, date, location, budget) {
    const params = new URLSearchParams();

    params.append('cookie-trip-id', tripId);
    params.append('text-input-title', title);
    params.append('text-input-date', date);
    params.append('text-input-location', location);
    params.append('text-input-estimatedCost', budget);

    const response = await fetch('/NewEvent', {method: 'POST', body: params});
    const responseMsg = await response.text();

    var numbers = /^[+-]?\d+$/;
    if(responseMsg.match(numbers)){
        msgDiv.innerHTML = `<p class="success">Success!</p>`;

        sessionStorage.setItem("eventId", responseMsg);
        
        window.setTimeout(function() {
            window.location.href = "/pages/tripDashboard.html";
        }, 400);
    
    } else {
        msgDiv.innerHTML = responseMsg;
    }
}

// Limit date range within trip from/to dates
async function fetchTripDates() {
    const params = new Headers();

    params.append('tripID', tripId);

    await fetch('/getTripByID', {method: 'GET', headers: params}).then(response => response.json()).then(
        (trip) => {
            let startDate = new Date(trip.start);
            let endDate = new Date(trip.end);
            dateDisable(startDate, endDate)
        }
    )
}

function dateDisable(startDate, endDate) {    
    console.log("start: " + startDate);
    console.log("end: " + endDate);
    fromDate = getDateString(startDate);
    toDate = getDateString(endDate);
    console.log("from: " + fromDate);
    console.log("to: " + toDate);
    document.getElementById("input-date").setAttribute('min', fromDate);
    document.getElementById("input-date").setAttribute('max', toDate);    
}

function getDateString(date) {
    var tdate = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getUTCFullYear();
    if(tdate < 10){
        tdate = '0' + tdate;
    }
    if(month < 10) {
        month = '0' + month
    }
    return year + "-" + month + "-" + tdate
}

