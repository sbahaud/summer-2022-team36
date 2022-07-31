var username = sessionStorage.getItem("userName");
var btnDiv = document.getElementById("enter-btn-div");

if(username === "" || username === null){
    btnDiv.innerHTML = `
        <button class="btn login-btn" onclick="location.href='./pages/userSignin.html'">Log In</button>
        <button class="btn signup-btn" onclick="location.href='./pages/userSignup.html'">Sign Up</button>
    `;
} else {
    btnDiv.innerHTML = 
        `<p>Hi ` + 
        username + 
        `!</p>` +
        `<button class="btn create-btn" onclick="location.href='./pages/dashboard.html'">Dashboard</button>
        <button class="btn logout-btn" onclick="logout()">Log out</button>`;
}

// Log out
function logout(){
    sessionStorage.clear();
    alert("Successully logged out");
    window.setTimeout(function() {
        window.location.href = "../index.html";
    }, 400);
}
