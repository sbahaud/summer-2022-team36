let tripId = sessionStorage.getItem("tripId");
let userId = sessionStorage.getItem("userId");
let userName = sessionStorage.getItem("userName");

// displayTripInfo("colombia", "10000", "jul 2", "aug 2", "alice, bob, cathy");
// displayEvents("eventId", "event1", "30", "peru", "aug 3");
// displayEvents("eventId", "event2", "400", "lima", "aug 22");
// displayEvents("eventId", "event3", "1000", "sydney", "sep 2");
// displayEvents("eventId", "event4", "100000", "mars", "dec 9");

if(userId !== "" && userId !== null && tripId !== "" && tripId !== null){
    console.log(tripId);
    showUsername();
    fetchTripDetail(tripId);
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

function displayTripInfo(tripTitle, totalBudget, start, end, names) {
    let startDate = new Date(start);
    let endDate = new Date(end);
    document.getElementById("destination").innerHTML = tripTitle;
    document.getElementById("totalBudget").innerHTML = totalBudget;
    document.getElementById("startDate").innerHTML = startDate.toDateString();
    document.getElementById("endDate").innerHTML = endDate.toDateString();
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

    var eventBox = 
        '<h3 class="event-title">' + title + '</h3>'
        + '<p class="event-info">Estimated Cost $ ' + estimatedCost + '</p>'
        + '<p class="event-info">Location: ' + location + '</p>'
        + '<p class="trip-info">' + date.toString() + '</p>'
        ;

    event.innerHTML = eventBox;

    document.getElementById("events-div").appendChild(event);
}