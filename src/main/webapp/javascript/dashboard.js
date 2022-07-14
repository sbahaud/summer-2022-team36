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
                displayTrips(trip.title, trip.totalBudget, trip.start, trip.end)});
    });
}   

function displayTrips(title, totalBudget, startDate, endDate) {
    const trip = document.createElement("div");
    trip.className = "trip";

    var tripBox = 
        '<h3 class="trip-title">' + title + '</h3>'
        + '<p class="trip-info">' + totalBudget + '</p>'
        + '<p class="trip-info">' + startDate.toString() + '</p>'
        + '<p class="trip-info">' + endDate.toString() + '</p>';

    trip.innerHTML = tripBox;

    document.getElementById("trips-div").appendChild(trip);
}