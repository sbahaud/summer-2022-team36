// input validator
function input_check(input){
    var letters = /^[\w \-']+$/;
    if(letters.test(input)){
        console.log("passed check");
        return true;
    } else {
        console.log("illegal input");
        return false;
    }
}

// send username to backend
function postUsername(username) {
    const params = new URLSearchParams();

    params.append('text-input-user-name', username);

    fetch('/LogIn', {method: 'POST', username});
}


// show/hide message
var msgDiv = document.getElementById("status-msg");

// get username
var username = document.getElementById("signup-user-name");

username.addEventListener("keypress", function(event) {
    if(event.key === "Enter") {
        event.preventDefault();
        if(username.value === "") {
            msgDiv.innerHTML = `<p class="error">Username cannot be empty.</p>`;
        } else if(input_check(username.value)){
            postUsername(username.value);
            msgDiv.innerHTML = `<p class="success">Log In successful!</p>`;
        } else {
            msgDiv.innerHTML = `<p class="error">Usernames are letters and numbers only.</p>`;
        }

        // clear input box
        username.value = "";
    }
});
