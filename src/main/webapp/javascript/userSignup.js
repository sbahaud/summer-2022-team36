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

    console.log("params:");
    console.log(params);

    params.append('text-input-user-name', username);

    fetch('/SignUp', {method: 'POST', body: params});
}


// show/hide message
var errorMsg = document.getElementById("error-msg");
var emptyMsg = document.getElementById("empty-msg");
var successMsg = document.getElementById("success-msg");

function show(msg){
    if(msg.classList.contains("hide")){
        msg.classList.remove("hide");
    }
}

function hide(msg){
    if(!msg.classList.contains("hide")){
        msg.classList.add("hide");
    }
}


// get username
var username = document.getElementById("signup-user-name");

username.addEventListener("keypress", function(event) {
    if(event.key === "Enter") {
        event.preventDefault();
        if(username.value === "") {
            hide(errorMsg);
            show(emptyMsg);
        } else if(input_check(username.value)){
            // console.log(username.value);
            hide(errorMsg);
            hide(emptyMsg);
            show(successMsg);
            console.log("here!!");
            postUsername(username.value);
            console.log(username.value);
        } else {
            // console.log("throw error message");
            hide(emptyMsg);
            show(errorMsg);
        }

        // clear input box
        username.value = "";
    }
});
