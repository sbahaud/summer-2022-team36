let userId = sessionStorage.getItem("userId");
let userName = sessionStorage.getItem("userName");
sessionStorage.setItem("tripId", "");

if(userId !== "" && userId !== null){
    showUsername();
    fetchTrips(userId);
}

function showUsername() {
    document.getElementById("nameSpan").innerHTML = userName;
}

async function fetchTrips(userId) {
    const params = new Headers();

    params.append('userID', userId);

    await fetch('/get-trips', {method: 'GET', headers: params}).then(response => response.json()).then((trips) => {
        trips.forEach(
            (trip) => {
                displayTrips(trip.tripID, trip.title, trip.totalBudget, trip.start, trip.end)});
    });
}   

function displayTrips(tripId, title, totalBudget, startDate, endDate) {
    const trip = document.createElement("div");
    trip.className = "trip";
    trip.setAttribute("onclick", "showTripDetail()");
    trip.setAttribute("tripIdNumber", tripId);
    trip.setAttribute("tripTitle", title);

    let start = new Date(startDate);
    let end = new Date(endDate);
    var tripBox = 
        '<h3 class="trip-title">' + title + '</h3>'
        + '<p class="trip-info">Budget $ ' + totalBudget + '</p>'
        + '<p class="trip-info">From ' + start.toDateString() + '</p>'
        + '<p class="trip-info">To ' + end.toDateString() + '</p>'
        + ` <button 
            class="btn create-btn hide" 
            onclick="location.href='tripDashboard.html'">
            Go To Trip
            </button>` ;

    trip.innerHTML = tripBox;

    document.getElementById("trips-div").appendChild(trip);
}

function showTripDetail() {
    let trips = document.querySelectorAll(".trip");

    for(let i = 0; i<trips.length; i++){

        trips[i].onclick = function(){
            for(let j = 0; j<trips.length; j++){
                trips[j].classList.remove("selectedTrip");
                trips[j].lastChild.classList.add("hide");
            }

            this.classList.add("selectedTrip");
            this.lastChild.classList.remove("hide");

            let selectedTripId = this.getAttribute("tripIdNumber");
            let selectedTripTitle = this.getAttribute("tripTitle");
            sessionStorage.setItem("tripId",  selectedTripId);
            sessionStorage.setItem("tripTitle", selectedTripTitle)

            fetchBudget(userId, selectedTripId);
        }			
    }
}


// Show Budget
let budgetDiv = document.getElementById("budget-div");

function displayLoading() {
    budgetDiv.innerHTML = `<p class="loading">Loading...</p>`;
}

function hideLoading() {
    budgetDiv.innerHTML = ``;
}

async function fetchBudget(userId, tripId) {
    displayLoading();

    const params = new Headers();

    params.append('userID', userId);

    await fetch('/get-budget', {method: 'GET', headers: params}).then(response => response.json()).then((budgets) => {
        budgets.forEach(
            (budget) => {
                if(budget.tripID === tripId) {
                    hideLoading();
                    displayBudget(budget.title, budget.contribution, budget.tripBudget);
                };
            });
    });
}

function displayBudget(title, contribution, budget) {
    var content = 
        `<p class="budget-title">Trip To</p>
            <p class="budget-data">` + title + `</p>`
        + `<p class="budget-title">Trip Budget</p>
            <p class="budget-data">$ `+ budget + `</p>`
        + `<p class="budget-title">Your Expected Contribution</p>
            <p class="budget-data">$ ` + contribution + `</p>`;
    
    budgetDiv.innerHTML = content;
}


// Log out
var logout = document.getElementById("logout-btn");

logout.addEventListener("click", function() {
    sessionStorage.clear();
    alert("Successully logged out");
    window.setTimeout(function() {
        window.location.href = "../index.html";
    }, 400);
})
