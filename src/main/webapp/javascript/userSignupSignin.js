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

function validateUsername(username){
    if(username === ""){
        msgDiv.innerHTML = `<p class="error">Username cannot be empty.</p>`;
    } else if(!input_check(username)) {
        msgDiv.innerHTML = `<p class="error">Please use letters and numbers only.</p>`;
    } else {
        msgDiv.innerHTML = ``;
        return true;
    }

    return false;
}


// User Sign up
var signup_username = document.getElementById("signup-username");
if(signup_username){
    signup_username.addEventListener("keypress", function(event) {
        if(event.key === "Enter") {
            event.preventDefault();

            if(validateUsername(signup_username.value)){
                postUsername(signup_username.value);
            } 

            // clear input box
            signup_username.value = "";
        }
    });
}

// Create New User
// send username to database
async function postUsername(username) {
    const params = new URLSearchParams();

    params.append('text-input-user-name', username);

    const response = await fetch('/SignUp', {method: 'POST', body: params});
    const msgFromResponse = await response.text();

    var numbers = /^[+-]?\d+$/;
    if(msgFromResponse.match(numbers)){
        msgDiv.innerHTML = `<p class="success">Success!</p>`;

        sessionStorage.setItem("userName", username);
        sessionStorage.setItem("userId", msgFromResponse);
        
        window.setTimeout(function() {
            window.location.href = "/pages/dashboard.html";
        }, 500);
    
    } else {
        msgDiv.innerHTML = msgFromResponse;
    }
}


// User Log In
var login_username = document.getElementById("login-username");
if(login_username){
    login_username.addEventListener("keypress", function(event) {
        if(event.key === "Enter") {
            event.preventDefault();
            
            getUsername(login_username.value);

            // clear input box
            login_username.value = "";
        }
    });
}

// Search for existing user
// send username to database, redirect on status
async function getUsername(username) {
    const params = new URLSearchParams();
    params.append('text-input-user-name', username);

    const response = await fetch('/LogIn', {method: 'POST', body: params});
    const msgFromResponse = await response.text();

    var numbers = /^[+-]?\d+$/;
    if(msgFromResponse.match(numbers)){
        msgDiv.innerHTML = `<p class="success">Success!</p>`;

        sessionStorage.setItem("userName", username);
        sessionStorage.setItem("userId", msgFromResponse);
        
        window.setTimeout(function() {
            window.location.href = "/pages/dashboard.html";
        }, 500);
    
    } else {
        msgDiv.innerHTML = msgFromResponse;
    }
}