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
        msgDiv.innerHTML = `<p class="success">Success!</p>`;
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
function postUsername(username) {
    const params = new URLSearchParams();

    params.append('text-input-user-name', username);

    fetch('/SignUp', {method: 'POST', body: params});
}


// User Log In
var login_username = document.getElementById("login-username");
if(login_username){
    login_username.addEventListener("keypress", function(event) {
        if(event.key === "Enter") {
            event.preventDefault();
            
            if(validateUsername(login_username.value)){
                if(getUsername(login_username.value)){
                    msgDiv.innerHTML = `<p class="success">Log In successful!</p>`;
                } else {
                    msgDiv.innerHTML = `<p class="success">Wrong username!</p>`;
                }
            }

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

    console.log(response);

    if (response !== ""){
        return true;
    }

    return false;
}