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