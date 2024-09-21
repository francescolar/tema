document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            users: [],
            searchUser: '',
            username: '',
            email: '',
            name: '',
            surname: '',
            errors: {
                username: false,
                email: false,
                name: false,
                surname: false,
            },
            authId: null
        },
        mounted() {
            this.fetchUsers(),
            this.authId = this.$el.getAttribute('data-auth-id');
        },
        computed: {
            filteredUsers() {
                if (!this.searchUser) return this.users;
                return this.users.filter(user =>
                    Object.keys(user).some(key =>
                        String(user[key]).toLowerCase().includes(this.searchUser.toLowerCase())
                    )
                );
            }
        },
        methods: {
            fetchUsers() {
                axios.get('/api/users/all-no-admin')
                .then(response => {
                    this.users = response.data;
                })
                .catch(error => {
                    console.error('Error fetching users:', error);
                });
            },
            validateForm() {
                this.clearErrors();
                if (this.username.length < 4) {
                    this.errors.username = true;
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

                if (Object.values(this.errors).every(value => !value)) {
                    this.$refs.registerForm.submit();
                }
            },
            validateEmail(email) {
                const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                return regex.test(email);
            },
            clearErrors() {
                this.errors = {
                    username: false,
                    email: false,
                    name: false,
                    surname: false,
                };
            }
        }
    });
});