document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            username: document.getElementById('username').value,
            name: document.getElementById('name').value,
            surname: document.getElementById('surname').value,
            email: document.getElementById('email').value,
            errors: {
                username: false,
                name: false,
                surname: false,
                email: false
            }
        },
        methods: {
            validateForm() {
                this.errors.username = this.username.length < 4;
                this.errors.name = !this.name;
                this.errors.surname = !this.surname;
                this.errors.email = !this.email;

                if (!this.errors.username && !this.errors.name && !this.errors.surname && !this.errors.email) {
                    this.$refs.editContactForm.submit();
                }
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