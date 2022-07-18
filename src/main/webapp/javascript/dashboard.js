let userId = sessionStorage.getItem("userId");
let userName = sessionStorage.getItem("userName");

if(userId !== "" && userId !== null){
    console.log(userId);
    fetchTrips(userId);
    showUsername();
}

function showUsername() {
    console.log(userName);
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
    let start = new Date(startDate);
    let end = new Date(endDate);
    var tripBox = 
        '<h3 class="trip-title">' + title + '</h3>'
        + '<p class="trip-info">Budget $ ' + totalBudget + '</p>'
        + '<p class="trip-info">' + start.toDateString() + '</p>'
        + '<p class="trip-info">' + end.toDateString() + '</p>'
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
            sessionStorage.setItem("tripId",  selectedTripId);

            getBudget(selectedTripId);
            showBudget(selectedTripId);
        }			
    }
}


// work in progress
function showBudget(tripId) {
    let budgetDiv = document.getElementById("budget-div");
    if(tripId === "" || tripId === null) {
        let noSelectedTripMsg = `<p class="big-msg">Select a trip to begin!</p>`;
        budgetDiv.innerHTML = noSelectedTripMsg;
    } else {
        // work in progress
        console.log(tripId);
    }
}

function getBudget(tripId) {
    // this function will perform a fetch to get budget of the specific trip
    // work in progress
    console.log(tripId);
}