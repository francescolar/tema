document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#editForm',
        data: {
            oldPassword: '',
            newPassword: '',
            confirmPassword: '',
            touchedNewPassword: false,  // Per evitare che gli errori appaiano subito
            isFirstLogin: false,  // Flag per distinguere primo login o normale
            errors: {
                oldPassword: [],
                newPassword: [],
                confirmPassword: []
            }
        },
        computed: {
            isFormInvalid() {
                return (
                (!this.isFirstLogin && !this.oldPassword) ||
                !this.newPassword ||
                !this.confirmPassword ||
                this.errors.oldPassword.length > 0 ||
                this.errors.newPassword.length > 0 ||
                this.errors.confirmPassword.length > 0
                );
            }
        },
        methods: {
            validateForm() {
                this.clearErrors();

                if (!this.isFirstLogin) {
                    if (!this.oldPassword) {
                        this.errors.oldPassword.push("La vecchia password è richiesta.");
                    }
                }

                this.validateNewPassword();

                this.validateConfirmPassword();

                if (this.errors.oldPassword.length === 0 && this.errors.newPassword.length === 0 && this.errors.confirmPassword.length === 0) {
                    this.$refs.editContactForm.submit();
                }
            },

            validateNewPassword() {
                this.errors.newPassword = [];

                if (!this.newPassword && this.touchedNewPassword) {
                    this.errors.newPassword.push("La nuova password è richiesta.");
                } else if (this.newPassword) {
                    if (this.newPassword.length < 8) {
                        this.errors.newPassword.push("La password deve avere almeno 8 caratteri.");
                    }
                    if (this.newPassword.length > 50) {
                        this.errors.newPassword.push("La password può avere al massimo 50 caratteri.");
                    }
                    if (!/[A-Z]/.test(this.newPassword)) {
                        this.errors.newPassword.push("La password deve contenere almeno una lettera maiuscola.");
                    }
                    if (!/[a-z]/.test(this.newPassword)) {
                        this.errors.newPassword.push("La password deve contenere almeno una lettera minuscola.");
                    }
                    if (!/[0-9]/.test(this.newPassword)) {
                        this.errors.newPassword.push("La password deve contenere almeno un numero.");
                    }
                    if (!/[@$!%*?&?]/.test(this.newPassword)) {
                        this.errors.newPassword.push("La password deve contenere almeno un carattere speciale (@$!%*?&?).");
                    }
                }
            },
            validateConfirmPassword() {
                this.errors.confirmPassword = [];
                if (this.newPassword !== this.confirmPassword) {
                    this.errors.confirmPassword.push("Le password non corrispondono.");
                }
            },
            validatePassword(newPassword) {
                const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&?])[A-Za-z\d@$!%*?&?]{8,50}$/;
                return passwordRegex.test(newPassword);
            },
            clearErrors() {
                this.errors = {
                    oldPassword: [],
                    newPassword: [],
                    confirmPassword: []
                };
            },
            handleNewPasswordInput() {
                this.touchedNewPassword = true;
                this.validateNewPassword();
            },
            cancel() {
                this.$refs.cancel.submit();
            }
        },
        mounted() {
            this.isFirstLogin = document.querySelector('input[name="role"]').value === "true";
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