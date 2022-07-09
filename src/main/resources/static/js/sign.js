function matchPassword() {
    var pw1 = document.getElementById("pass");
    var pw2 = document.getElementById("re_pass");
    if(pw1 != pw2)
    {
        alert("Passwords did not match");
    } else {
        alert("Password created successfully");
    }
}
function ValidateEmail(input) {
    var validRegex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    if (input.value.match(validRegex)) {
        alert("Valid email address!");
        document.email.focus();
        return true;
    } else {
        alert("Invalid email address!");
        document.email.focus();
        return false;
    }
}