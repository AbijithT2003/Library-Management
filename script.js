document.getElementById('adminLogin').addEventListener('click', adminLogin);
document.getElementById('userLogin').addEventListener('click', userLogin);
function adminLogin() {
    window.location.href = 'admin-login.html'
}

function userLogin() {
    window.location.href = 'user-login.html';
}
