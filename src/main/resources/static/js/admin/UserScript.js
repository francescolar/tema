document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            users: [],
            searchUser: '',
            selectedFilter: '',
            excludeDisabled: false,
            workHoursCondition: 'equal',
            workHoursValue: null,
            startDate: '',
            endDate: '',
            username: '',
            email: '',
            name: '',
            surname: '',
            errors: {
                username: [],
                email: [],
                name: [],
                surname: [],
            },
            currentPage: 1,
            itemsPerPage: 20,
            authId: null
        },
        mounted() {
            this.fetchUsers(),
            this.authId = this.$el.getAttribute('data-auth-id');
        },
        computed: {
            paginatedUsers() {
                const start = (this.currentPage - 1) * this.itemsPerPage;
                const end = start + this.itemsPerPage;
                return this.filteredUsers.slice(start, end);  // Applica la paginazione sui dati filtrati
            },
            totalPages() {
                return Math.ceil(this.filteredUsers.length / this.itemsPerPage);
            },
            filteredUsers() {
                let filtered = this.users;

                if (this.excludeDisabled) {
                    filtered = filtered.filter(user => user.enabled);
                }

                if (this.searchUser) {
                    filtered = filtered.filter(user => {
                        const filterField = this.selectedFilter || null;
                        const searchTerm = this.searchUser.toLowerCase();

                        if (filterField && user[filterField] !== undefined) {
                            return String(user[filterField]).toLowerCase().includes(searchTerm);
                        } else {
                            return Object.keys(user).some(key =>
                            String(user[key]).toLowerCase().includes(searchTerm)
                            );
                        }
                    });
                }

                if (this.workHoursValue !== null && this.workHoursValue !== '') {
                    filtered = filtered.filter(user => {
                        if (this.workHoursCondition === 'greater') {
                            return user.totalWorkHours > this.workHoursValue;
                        } else if (this.workHoursCondition === 'less') {
                            return user.totalWorkHours < this.workHoursValue;
                        } else if (this.workHoursCondition === 'equal') {
                            return user.totalWorkHours == this.workHoursValue;
                        }
                        return true;
                    });
                }

                if (this.startDate || this.endDate) {
                    filtered = filtered.filter(user => {
                        const userDate = new Date(user.createdAt);
                        const startDate = this.startDate ? new Date(this.startDate) : null;
                        const endDate = this.endDate ? new Date(this.endDate) : null;

                        if (startDate && endDate) {
                            return userDate >= startDate && userDate <= endDate;
                        } else if (startDate) {
                            return userDate >= startDate;
                        } else if (endDate) {
                            return userDate <= endDate;
                        }
                        return true;
                    });
                }

                return filtered;
            },
            isFormInvalid() {
                return (
                !this.username || !this.email || !this.name || !this.surname ||
                this.errors.username.length > 0 || this.errors.email.length > 0 ||
                this.errors.name.length > 0 || this.errors.surname.length > 0
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
            applyFilter() {
                this.filteredUsers;
            },
            resetFilters() {
                this.searchUser = '';
                this.selectedFilter = '';
                this.excludeDisabled = false;
                this.workHoursCondition = 'equal';
                this.workHoursValue = null;
                this.startDate = '';
                this.endDate = '';
                this.applyFilter();
            },
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
                    if (!/^[a-zA-Z0-9]+$/.test(this.username)) {
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
                }
            },
            validateName() {
                this.errors.name = [];

                if (!this.name) {
                    this.errors.name.push("Inserisci il nome.");
                } else if (this.name.length > 30) {
                    this.errors.name.push("Il nome può avere al massimo 30 caratteri.");
                }
            },
            validateSurname() {
                this.errors.surname = [];

                if (!this.surname) {
                    this.errors.surname.push("Inserisci il cognome.");
                } else if (this.surname.length > 30) {
                    this.errors.surname.push("Il cognome può avere al massimo 30 caratteri.");
                }
            },
            formatDate(dateString) {
                if (!dateString) return '';

                const date = new Date(dateString);

                const year = date.getFullYear();
                const month = String(date.getMonth() + 1).padStart(2, '0');
                const day = String(date.getDate()).padStart(2, '0');
                const hours = String(date.getHours()).padStart(2, '0');
                const minutes = String(date.getMinutes()).padStart(2, '0');
                const seconds = String(date.getSeconds()).padStart(2, '0');

                return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
            },
            clearErrors() {
                this.errors = {
                    username: [],
                    email: [],
                    name: [],
                    surname: [],
                };
            },
        }
    });
});