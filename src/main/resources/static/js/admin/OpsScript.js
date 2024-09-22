document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            ops: [],
            sites: [],
            systems: [],
            users: [],
            counter: 1,
            date: '',
            description: '',
            selectedSiteId: '',
            selectedSystemId: '',
            selectedUserId: '',
            searchOps: '',

            // Variabili per i filtri
            selectedSiteFilter: '',
            selectedSystemFilter: '',
            selectedUserFilter: '',
            hoursCondition: 'equal',
            hoursValue: null,
            descriptionFilter: '',
            startDate: '',
            endDate: '',
            createdStartDate: '',
            createdEndDate: '',

            selectedOp: {},
            authId: null,
            errors: {
                selectedSiteId: false,
                selectedSystemId: false,
                selectedUserId: false,
                date: false,
                description: false,
            }
        },
        mounted() {
            this.fetchOps(),
            this.fetchUsers(),
            this.fetchSites(),
            this.fetchSystems(),
            this.authId = this.$el.getAttribute('data-auth-id');
        },
        computed: {
            formattedCounter() {
                return parseFloat(this.counter).toFixed(2);  // Assicurati che il counter sia sempre formattato con due decimali
            },
            filteredOps() {
                let filtered = this.ops;

                // Filtro per sede
                if (this.selectedSiteFilter) {
                    filtered = filtered.filter(op => op.siteId === parseInt(this.selectedSiteFilter));
                }

                // Filtro per impianto
                if (this.selectedSystemFilter) {
                    filtered = filtered.filter(op => op.systemId === parseInt(this.selectedSystemFilter));
                }

                // Filtro per ore impiegate
                if (this.hoursValue !== null && this.hoursValue !== '') {
                    filtered = filtered.filter(op => {
                        if (this.hoursCondition === 'greater') {
                            return op.hoursSpent > this.hoursValue;
                        } else if (this.hoursCondition === 'less') {
                            return op.hoursSpent < this.hoursValue;
                        } else if (this.hoursCondition === 'equal') {
                            return op.hoursSpent == this.hoursValue;
                        }
                        return true;
                    });
                }

                // Filtro per descrizione
                if (this.descriptionFilter) {
                    const searchTerm = this.descriptionFilter.toLowerCase();
                    filtered = filtered.filter(op => op.description.toLowerCase().includes(searchTerm));
                }

                // Filtro per utente
                if (this.selectedUserFilter) {
                    filtered = filtered.filter(op => op.userId === parseInt(this.selectedUserFilter));
                }

                // Filtro per data (inizio - fine)
                if (this.startDate || this.endDate) {
                    filtered = filtered.filter(op => {
                        const opDate = new Date(op.date);
                        const startDate = this.startDate ? new Date(this.startDate) : null;
                        const endDate = this.endDate ? new Date(this.endDate) : null;

                        if (startDate && endDate) {
                            return opDate >= startDate && opDate <= endDate;
                        } else if (startDate) {
                            return opDate >= startDate;
                        } else if (endDate) {
                            return opDate <= endDate;
                        }
                        return true;
                    });
                }

                // Filtro per data di creazione (inizio - fine)
                if (this.createdStartDate || this.createdEndDate) {
                    filtered = filtered.filter(op => {
                        const createdDate = new Date(op.createdAt);
                        const createdStartDate = this.createdStartDate ? new Date(this.createdStartDate) : null;
                        const createdEndDate = this.createdEndDate ? new Date(this.createdEndDate) : null;

                        if (createdStartDate && createdEndDate) {
                            return createdDate >= createdStartDate && createdDate <= createdEndDate;
                        } else if (createdStartDate) {
                            return createdDate >= createdStartDate;
                        } else if (createdEndDate) {
                            return createdDate <= createdEndDate;
                        }
                        return true;
                    });
                }

                return filtered;
            },
            filteredSystems() {
                if (!this.selectedSiteId) {
                    return [];
                }
                return this.systems.filter(system => system.siteId === parseInt(this.selectedSiteId));
            }
        },
        methods: {
            showOpDetails(op) {
                this.selectedOp = op;
            },
            validateForm() {
                this.clearErrors();
                if (!this.selectedSiteId) {
                    this.errors.selectedSiteId = true;
                }
                if (!this.selectedSystemId) {
                    this.errors.selectedSystemId = true;
                }
                if (!this.selectedUserId) {
                    this.errors.selectedUserId = true;
                }
                if (!this.date) {
                    this.errors.date = true;
                }
                if (!this.description) {
                    this.errors.description = true;
                }

                if (Object.values(this.errors).every(value => !value)) {
                    this.$refs.operationForm.submit();
                }
            },
            clearErrors() {
                this.errors = {
                    selectedSiteId: false,
                    selectedSystemId: false,
                    selectedUserId: false,
                    date: false,
                    description: false,
                };
            },
            resetFilters() {
                this.selectedSiteFilter = '';
                this.selectedSystemFilter = '';
                this.selectedUserFilter = '';
                this.hoursCondition = 'equal';
                this.hoursValue = null;
                this.descriptionFilter = '';
                this.startDate = '';
                this.endDate = '';
                this.createdStartDate = '';
                this.createdEndDate = '';
                this.applyFilter(); // Per ricalcolare i dati filtrati
            },
            fetchOps() {
                axios.get('/api/operations/all')
                    .then(response => {
                    this.ops = response.data;
                })
                    .catch(error => {
                    console.error('Error fetching ops:', error);
                });
            },
            fetchSites() {
                axios.get('/api/sites/all')
                    .then(response => {
                    this.sites = response.data;
                })
                    .catch(error => {
                    console.error('Error fetching sites:', error);
                });
            },
            fetchSystems() {
                axios.get('/api/systems/all')
                    .then(response => {
                    this.systems = response.data;
                })
                    .catch(error => {
                    console.error('Error fetching systems:', error);
                });
            },
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
                this.filteredOps;
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
            increase(value) {
                this.counter = parseFloat(this.counter);
                if (this.counter - parseFloat(value) <= 100) {
                    this.counter += parseFloat(value);
                } else {
                    this.counter = 100;
                }
            },
            decrease(value) {
                this.counter = parseFloat(this.counter);
                if (this.counter - parseFloat(value) >= 1) {
                    this.counter -= parseFloat(value);
                } else {
                    this.counter = 1;
                }
            }
        }
    });
});