document.getElementById('adminLogin').addEventListener('click', adminLogin);
document.getElementById('userLogin').addEventListener('click', userLogin);
function adminLogin() {
    window.location.href = 'admin-login.html'
}

function userLogin() {
    window.location.href = 'user-login.html';
}

 function adminLogin() {
            // You can redirect to admin login page
            alert('Redirecting to Admin Login...');
            // window.location.href = 'admin-login.html';
        }

        function userLogin() {
            // You can redirect to user login page
            alert('Redirecting to Member Login...');
            // window.location.href = 'user-login.html';
        }

        // Smooth scrolling for navigation links
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                e.preventDefault();
                const target = document.querySelector(this.getAttribute('href'));
                if (target) {
                    target.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            });
        });
