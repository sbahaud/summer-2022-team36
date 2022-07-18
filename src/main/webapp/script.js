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
        `<button class="btn create-btn" onclick="location.href='./pages/dashboard.html'">Dashboard</button>`;
}

// Direct user to sign up page
document.getElementById("userSignup").onclick = () => {
    location.href = "./pages/userSignup.html";
}

// Direct user to sign in page
document.getElementById("userSignin").onclick = () => {
    location.href = "./pages/userSignin.html";
}
