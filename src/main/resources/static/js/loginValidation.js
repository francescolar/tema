document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#loginForm',
        data: {
            username: '',
            password: '',
            errors: {
                username: false,
                password: false
            },
            errorMessage: null
        },
        mounted() {
            const urlParams = new URLSearchParams(window.location.search);
            const error = urlParams.get('error');
            if (error) {
                this.handleError(error);
            }
        },

        methods: {
            validateForm() {
                this.errors.username = !this.username;
                this.errors.password = !this.password;

                if (!this.errors.username && !this.errors.password) {
                    this.$refs.loginForm.submit();
                }
            },
            handleError(error) {
                if (error === 'true') {
                    this.errorMessage = 'Invalid username or password.';
                }
            }
        }
    })
});

function togglePsw() {
    var password = document.getElementById("password");
    var pswButton = document.getElementById("show-password");
    if (password.type === "password") {
        password.type = "text";
        pswButton.innerHTML = "visibility_off";
    } else {
        password.type = "password";
        pswButton.innerHTML = "visibility";

    }
}