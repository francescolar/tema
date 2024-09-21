new Vue({
        el: '#registerForm',
        data: {
            username: '',
            password: '',
            confirmPassword: '',
            email: '',
            name: '',
            surname: '',
            errors: {
                username: false,
                password: false,
                confirmPassword: false,
                email: false,
                name: false,
                surname: false,
            }
        },
        methods: {
            validateForm() {
                this.clearErrors();
                if (this.username.length < 4) {
                    this.errors.username = true;
                }
                if (this.password.length < 8 || !this.hasValidPassword(this.password)) {
                    this.errors.password = true;
                }
                if (this.password !== this.confirmPassword) {
                    this.errors.confirmPassword = true;
                }
                if (!this.validateEmail(this.email)) {
                    this.errors.email = true;
                }
                if (!this.name) {
                    this.errors.name = true;
                }
                if (!this.surname) {
                    this.errors.surname = true;
                }

                // If there are no errors, submit the form
                if (Object.values(this.errors).every(value => !value)) {
                    this.$refs.registerForm.submit();  // Submit the form if everything is valid
                }
            },
            hasValidPassword(password) {
                const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*?]).{8,}$/;
                return regex.test(password);
            },
            validateEmail(email) {
                const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                return regex.test(email);
            },
            clearErrors() {
                this.errors = {
                    username: false,
                    password: false,
                    confirmPassword: false,
                    email: false,
                    name: false,
                    surname: false,
                };
            }
        }
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

  function toggleConfirmPsw() {
    var password = document.getElementById("confirmPassword");
    var pswButton = document.getElementById("show-password-confirm");
    if (password.type === "password") {
        password.type = "text";
        pswButton.innerHTML = "visibility_off";
    } else {
        password.type = "password";
        pswButton.innerHTML = "visibility";

    }
  }