document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            username: '',
            password: '',
            usernameMail: '',
            emailHelpId: '',
            message: '',
            subject: '',
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
            const successToast = document.getElementById('successToast');
            if (successToast) {
                const toast = new bootstrap.Toast(successToast);
                toast.show();
            }
        },
        methods: {
            validateCustomerEmail() {
                if (!this.usernameMail || !this.emailHelpId || !this.subject || !this.message) {
                    this.handleErrorAssistance();
                } else {
                    this.$refs.customerEmail.submit();
                }
            },
            validateForm() {
                this.errors.username = !this.username;
                this.errors.password = !this.password;

                if (!this.errors.username && !this.errors.password) {
                    this.$refs.loginForm.submit();
                }
            },
            handleError(error) {
                if (error === '1') {
                    this.errorMessage = 'Utente non registrato.';
                } else if (error === '2') {
                    this.errorMessage = 'Password errata.';
                } else if (error === '3') {
                    this.errorMessage = 'Contatta l\'amministratore.';
                }
            },
            handleErrorAssistance() {
                const toastLiveExample = document.getElementById("liveToast");
                const toast = new bootstrap.Toast(toastLiveExample);
                toast.show();
            }
        }
    });
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