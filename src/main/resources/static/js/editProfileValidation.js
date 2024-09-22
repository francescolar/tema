document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#editForm',
        data: {
            newPassword: '',
            confirmPassword: '',
            oldPassword: '',
            errors: {
                newPassword: false,
                confirmPassword: false,
                oldPassword: false
            }
        },
        methods: {
            validateForm() {
                this.errors.newPassword = this.newPassword && !this.validatePassword(this.newPassword);
                this.errors.confirmPassword = this.newPassword && (this.newPassword !== this.confirmPassword);
                this.errors.oldPassword = this.oldPassword.length < 2;

                if (!this.errors.newPassword && !this.errors.confirmPassword && !this.errors.oldPassword) {
                    this.$refs.editContactForm.submit();
                }
            },
            validateFormFirstLogin() {
                this.errors.newPassword = this.newPassword && !this.validatePassword(this.newPassword);
                this.errors.confirmPassword = this.newPassword && (this.newPassword !== this.confirmPassword);

                if (!this.errors.newPassword && !this.errors.confirmPassword) {
                    this.$refs.editContactForm.submit();
                }
            },
            validatePassword(newPassword) {
                const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
                return passwordRegex.test(newPassword);
            },
            cancel() {
                this.$refs.cancel.submit();
            }
        }
    });
});

function toggleOldPsw() {
    var password = document.getElementById("old-password");
    var pswButton = document.getElementById("show-old-password");
    if (password.type === "password") {
        password.type = "text";
        pswButton.innerHTML = "visibility_off";
    } else {
        password.type = "password";
        pswButton.innerHTML = "visibility";

    }
}

function toggleNewPsw() {
    var password = document.getElementById("newPassword");
    var pswButton = document.getElementById("show-new-password");
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