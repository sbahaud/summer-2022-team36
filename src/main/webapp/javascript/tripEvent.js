// Get userId for current page
var msgDiv = document.getElementById("status-msg");

let userId = sessionStorage.getItem("userId");
console.log(userId);

let userName = sessionStorage.getItem("userName");
console.log(userName);

var title = document.getElementById("text-input-title");
var fromDate = document.getElementById("text-input-start-date");
var toDate = document.getElementById("text-input-end-date");
var participants = document.getElementById("text-input-participants");
var budget = document.getElementById("text-input-totalBudget");

if (userId !== "" && userId !== null) {
    var submit = document.getElementById("submit-trip");
    submit.addEventListener("click", function(event) {
        event.preventDefault();

        postNewTrip(title.value, fromDate.value, toDate.value, participants.value, budget.value, userId);
    });
}

// Create new trip
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
        
        window.setTimeout(function() {
            window.location.href = "/trip/tripDetails.html";
        }, 500);
    
    } else {
        msgDiv.innerHTML = responseMsg;
    }
}