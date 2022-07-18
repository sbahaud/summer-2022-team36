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
        <button class="btn logout-btn" id="logout-btn">Log out</button>`;
}

// Log out
document.getElementById("logout-btn").onclick = function(){
    sessionStorage.clear();
    btnDiv.innerHTML = `<p color=red> Successully logged out</p>`
    window.setTimeout(function() {
        window.location.href = "../index.html";
    }, 500);
}
