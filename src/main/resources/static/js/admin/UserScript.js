document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            users: [],
            searchUser: '',
            selectedFilter: '', // Filtro selezionato
            excludeDisabled: false, // Checkbox per escludere disabilitati
            workHoursCondition: 'equal', // Riordina "uguale" come default
            workHoursValue: null, // Valore per totale ore manutenzione
            startDate: '', // Data di inizio
            endDate: '', // Data di fine
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
                let filtered = this.users;

                // Escludi gli utenti disabilitati
                if (this.excludeDisabled) {
                    filtered = filtered.filter(user => user.enabled);
                }

                // Filtra per ricerca testuale
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

                // Filtro per totale ore manutenzione
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

                // Filtro per data di creazione
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
                this.filteredUsers; // Forza il ricalcolo dei filtri
            },
            resetFilters() {
                this.searchUser = '';
                this.selectedFilter = '';
                this.excludeDisabled = false;
                this.workHoursCondition = 'equal';
                this.workHoursValue = null;
                this.startDate = '';
                this.endDate = '';
                this.applyFilter();  // Ricalcola i dati filtrati
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
            formatDate(dateString) {
                if (!dateString) return '';

                // Converti la stringa in oggetto Date
                const date = new Date(dateString);

                // Formatta la data in "YYYY-MM-DD HH:mm:ss"
                const year = date.getFullYear();
                const month = String(date.getMonth() + 1).padStart(2, '0');  // I mesi vanno da 0 a 11
                const day = String(date.getDate()).padStart(2, '0');
                const hours = String(date.getHours()).padStart(2, '0');
                const minutes = String(date.getMinutes()).padStart(2, '0');
                const seconds = String(date.getSeconds()).padStart(2, '0');

                return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
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