let tripId = sessionStorage.getItem("tripId");
let userId = sessionStorage.getItem("userId");
let userName = sessionStorage.getItem("userName");

if(userId !== "" && userId !== null && tripId !== "" && tripId !== null){
    console.log(tripId);
    showUsername();
    fetchTripDetail(tripId);
    fetchEvents(tripId);
}

function showUsername() {
    console.log(userName);
    document.getElementById("nameSpan").innerHTML = userName;
}

async function fetchTripDetail(tripId) {
    const params = new Headers();

    params.append('tripID', tripId);

    await fetch('/getTripByID', {method: 'GET', headers: params}).then(response => response.json()).then(
        (trip) => {
            displayTripInfo(trip.title, trip.totalBudget, trip.start, trip.end, trip.names)}
    );
}

function displayTripInfo(tripTitle, totalBudget, startDate, endDate, names) {
    document.getElementById("destination").innerHTML = tripTitle;
    document.getElementById("totalBudget").innerHTML = totalBudget;
    document.getElementById("startDate").innerHTML = startDate;
    document.getElementById("endDate").innerHTML = endDate;
    document.getElementById("participants").innerHTML = names;
}

async function fetchEvents(tripId) {
    const params = new Headers();
    params.append('tripID', tripId);

    await fetch('/get-events', {method: 'GET', headers: params}).then(response => response.json()).then((events) => {
        events.forEach(
            (event) => {
                displayEvents(event.eventID, event.title, event.estimatedCost, event.location, event.date)});
    });
}

function displayEvents(eventId, title, estimatedCost, location, date) {
    const event = document.createElement("div");
    event.className = "event";
    event.setAttribute("eventId", eventId);
    let eventDate = new Date(date);

    var eventBox = 
        '<h3 class="event-title">' + title + '</h3>'
        + '<p class="event-info">Estimated Cost $ ' + estimatedCost + '</p>'
        + '<p class="event-info">Location: ' + location + '</p>'
        + '<p class="trip-info">' + eventDate.toDateString() + '</p>'
        ;

    event.innerHTML = eventBox;

    document.getElementById("events-div").appendChild(event);
}