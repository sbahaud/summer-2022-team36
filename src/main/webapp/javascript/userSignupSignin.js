/* 
 *  Helper functions
 */

// input validator
function input_check(input){
    var letters = /^[\w \-']+$/;
    if(letters.test(input)){
        return true;
    } else {
        return false;
    }
}

// show/hide message
var msgDiv = document.getElementById("status-msg");


// User Sign up
var signup_username = document.getElementById("text-input-user-name");
signup_username.addEventListener("keypress", function(event) {
    if(event.key === "Enter") {
        event.preventDefault();
        if(signup_username.value === "") {
            // show empty error message
            msgDiv.innerHTML = `<p class="error">Username cannot be empty.</p>`;
        } else if(input_check(signup_username.value)){
            // show success sign up message
            msgDiv.innerHTML = `<p class="success">Sign up successful!</p>`;
            // post username to database
            postUsername(signup_username.value);
        } else {
            // show invalid error message
            msgDiv.innerHTML = `<p class="error">Please use letters and numbers only.</p>`;
        }

        // clear input box
        signup_username.value = "";
    }
});

// Create New User
// send username to database
function postUsername(username) {
    const params = new URLSearchParams();

    params.append('text-input-user-name', username);

    fetch('/SignUp', {method: 'POST', body: params});
}


// User Log In
var login_username = document.getElementById("signup-user-name");
login_username.addEventListener("keypress", function(event) {
    if(event.key === "Enter") {
        event.preventDefault();
        if(login_username.value === "") {
            msgDiv.innerHTML = `<p class="error">Username cannot be empty.</p>`;
        } else if(input_check(login_username.value)){
            if(postUsername(login_username.value)){
                msgDiv.innerHTML = `<p class="success">Log In successful!</p>`;
            } else {
                msgDiv.innerHTML = `<p class="success">Wrong username!</p>`;
            }
        } else {
            msgDiv.innerHTML = `<p class="error">Usernames are letters and numbers only.</p>`;
        }

        // clear input box
        login_username.value = "";
    }
});

// Search for existing user
// send username to database, redirect on status
function getUsername(username) {
    const params = new URLSearchParams();

    params.append('text-input-user-name', username);

    const response = await fetch('/LogIn', {method: 'POST', body: params});

    console.log(response);

    if (response !== ""){
        return true;
    }

    return false;
}