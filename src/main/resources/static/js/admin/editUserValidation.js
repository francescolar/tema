document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            username: document.getElementById('username').value,
            name: document.getElementById('name').value,
            surname: document.getElementById('surname').value,
            email: document.getElementById('email').value,
            errors: {
                username: [],
                email: [],
                name: [],
                surname: [],
            }
        },
        computed: {
            isFormInvalid() {
                return (
                !this.username || !this.email || !this.name || !this.surname ||
                this.errors.username.length > 0 || this.errors.email.length > 0 ||
                this.errors.name.length > 0 || this.errors.surname.length > 0
                );
            }
        },
        methods: {
            validateForm() {
                this.clearErrors();

                this.validateUsername();
                this.validateEmail();
                this.validateName();
                this.validateSurname();

                if (Object.values(this.errors).every(errorArray => errorArray.length === 0)) {
                    this.$refs.registerForm.submit();
                }
            },
            validateUsername() {
                this.errors.username = [];

                if (this.username.length === 0) {
                    this.errors.username.push("L'username non può essere vuoto.");
                } else {
                    if (this.username.length < 4) {
                        this.errors.username.push("L'username deve avere minimo 4 caratteri.");
                    }
                    if (this.username.length > 25) {
                        this.errors.username.push("L'username può avere al massimo 25 caratteri.");
                    }
                    if (!/^[a-zA-Z0-9.]+$/.test(this.username)) {
                        this.errors.username.push("L'username può contenere solo lettere e numeri.");
                    }
                }
            },
            validateEmail() {
                this.errors.email = [];

                if (!this.email) {
                    this.errors.email.push("L'email non può essere vuota.");
                } else {
                    if (!/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@(([^<>()[\]\.,;:\s@"]+\.[a-zA-Z]{2,}))$/.test(this.email)) {
                        this.errors.email.push("L'email non è valida.");
                    }
                    if (this.email.length > 50) {
                        this.errors.email.push("L'email può avere al massimo 50 caratteri.");
                    }
                    if (/[^A-Za-z0-9\s'’\-@$!%*?&.]/.test(this.email)) {
                        this.errors.email.push("L'email contiene caratteri non consentiti.");
                    }
                }
            },
            validateName() {
                this.errors.name = [];

                if (!this.name) {
                    this.errors.name.push("Inserisci il nome.");
                } else {
                    if (this.name.length > 30) {
                        this.errors.name.push("Il nome può avere al massimo 30 caratteri.");
                    }
                    if (/[^A-Za-z0-9\s'’\-@$!%*?&.]/.test(this.name)) {
                        this.errors.name.push("Il nome contiene caratteri non consentiti.");
                    }
                }
            },
            validateSurname() {
                this.errors.surname = [];

                if (!this.surname) {
                    this.errors.surname.push("Inserisci il cognome.");
                } else {
                    if (this.surname.length > 30) {
                        this.errors.surname.push("Il cognome può avere al massimo 30 caratteri.");
                    }
                    if (/[^A-Za-z0-9\s'’\-@$!%*?&.]/.test(this.surname)) {
                        this.errors.surname.push("Il cognome contiene caratteri non consentiti.");
                    }
                }
            },
            clearErrors() {
                this.errors = {
                    username: [],
                    email: [],
                    name: [],
                    surname: [],
                };
            },
            cancel() {
                this.$refs.cancel.submit();
            },
            submitForm() {
                this.$refs.editContactForm.submit();
            },
            submitDeleteForm() {
                this.$refs.deleteContactForm.submit();
            }
        }
    });
});