// Date ragne disable 
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
document.getElementById("fromDate").setAttribute('min', minDate)

var fromDate;
$('#fromDate').on('change', function() {
    fromDate = $(this).val();
    $('#toDate').prop('min', function() {
        return fromDate;
    })
});

var toDate;
$('#toDate').on('change', function() {
    toDate = $(this).val();
    $('#fromDate').prop('max', function() {
        return toDate;
    })
});


// Get userId for current page
var msgDiv = document.getElementById("status-msg");

let userId = sessionStorage.getItem("userId");
console.log(userId);

let userName = sessionStorage.getItem("userName");
console.log(userName);

var title = document.getElementById("text-input-title");
var fromDate = document.getElementById("fromDate");
var toDate = document.getElementById("toDate");
var participants = document.getElementById("text-input-participants");
var budget = document.getElementById("text-input-totalBudget");

if (userId !== "" && userId !== null) {
    var submit = document.getElementById("submit-trip");
    submit.addEventListener("click", function(event) {
        event.preventDefault();

        console.log(participants.value);

        postNewTrip(title.value, fromDate.value, toDate.value, participants.value, budget.value, userId);
    });
}

// Create new trip
// Send tripId to database
async function postNewTrip(title, fromDate, toDate, participants, budget, userId) {
    const params = new URLSearchParams();

    params.append('text-input-title', title);
    params.append('text-input-start-date', fromDate);
    params.append('text-input-to-date', toDate);
    params.append('text-input-participants', participants);
    params.append('text-input-totalBudget', budget);
    params.append('text-input-userId', userId);

    const response = await fetch('/NewTrip', {method: 'POST', body: params});
    const responseMsg = await response.text();

    var numbers = /^[+-]?\d+$/;
    if(responseMsg.match(numbers)){
        msgDiv.innerHTML = `<p class="success">Success!</p>`;

        sessionStorage.setItem("userName", username);
        sessionStorage.setItem("userId", responseMsg);
        
        window.setTimeout(function() {
            window.location.href = "/trip/tripDetails.html";
        }, 500);
    
    } else {
        msgDiv.innerHTML = responseMsg;
    }
}