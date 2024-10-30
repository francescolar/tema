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

            currentPage: 1,
            itemsPerPage: 20,

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
            this.startDate = this.getOneMonthAgoDate();
            this.fetchOps(),
            this.fetchUsers(),
            this.fetchSites(),
            this.fetchSystems(),
            this.authId = this.$el.getAttribute('data-auth-id');

            const opToast = document.getElementById('opSuccessToast');
            if (opToast) {
                const opSuccessToast = new bootstrap.Toast(opToast);
                opSuccessToast.show();
            }
        },
        computed: {
            formattedCounter() {
                return parseFloat(this.counter).toFixed(2);
            },
            filteredOps() {
                let filtered = this.ops;

                if (this.selectedSiteFilter) {
                    filtered = filtered.filter(op => op.siteId === parseInt(this.selectedSiteFilter));
                }

                if (this.selectedSystemFilter) {
                    filtered = filtered.filter(op => op.systemId === parseInt(this.selectedSystemFilter));
                }

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

                if (this.descriptionFilter) {
                    const searchTerm = this.descriptionFilter.toLowerCase();
                    filtered = filtered.filter(op => op.description.toLowerCase().includes(searchTerm));
                }

                if (this.selectedUserFilter) {
                    filtered = filtered.filter(op => op.userId === parseInt(this.selectedUserFilter));
                }

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

            paginatedOps() {
                let start = (this.currentPage - 1) * this.itemsPerPage;
                let end = start + this.itemsPerPage;
                return this.filteredOps.slice(start, end);
            },

            totalPages() {
                return Math.ceil(this.filteredOps.length / this.itemsPerPage);
            },
            filteredSystems() {
                if (!this.selectedSiteId) {
                    return [];
                }
                return this.systems.filter(system => system.siteId === parseInt(this.selectedSiteId));
            },
            isFormInvalid() {
                return (
                this.errors.selectedSiteId ||
                this.errors.selectedSystemId ||
                this.errors.selectedUserId ||
                this.errors.date ||
                this.errors.description ||
                !this.selectedSiteId ||
                !this.selectedSystemId ||
                !this.selectedUserId ||
                !this.date ||
                !this.description
                );
            }
        },
        methods: {
            getOneMonthAgoDate() {
                let today = new Date();
                let oneMonthAgo = new Date();
                oneMonthAgo.setMonth(today.getMonth() - 1);

                return oneMonthAgo.toISOString().split('T')[0];
            },
            showOpDetails(op) {
                this.selectedOp = op;
            },
            validateForm() {
                this.clearErrors();
                this.validateSite(false);
                this.validateSystem();
                this.validateUser();
                this.validateDate();
                this.validateDescription();

                if (!this.isFormInvalid) {
                    this.$refs.operationForm.submit();
                }
            },

            validateSite(resetSystem = true) {
                this.errors.selectedSiteId = !this.selectedSiteId;
                if (resetSystem) {
                    this.selectedSystemId = '';
                }
            },

            validateSystem() {
                this.errors.selectedSystemId = !this.selectedSystemId;
            },

            validateUser() {
                this.errors.selectedUserId = !this.selectedUserId;
            },

            validateDate() {
                this.errors.date = !this.date;
            },

            validateDescription() {
                this.errors.description = false;
                if (this.description.length < 1) {
                    this.errors.description = "La descrizione non può essere vuota.";
                } else if (this.description.length > 3000) {
                    this.errors.description = "La descrizione può avere al massimo 3000 caratteri.";
                } else if (/[^A-Za-z0-9\s'’\-@$!%*?&,.;:]/.test(this.description)) {
                    this.errors.description = "La descrizione contiene caratteri non consentiti.";
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
                this.applyFilter();
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

                const date = new Date(dateString);

                const year = date.getFullYear();
                const month = String(date.getMonth() + 1).padStart(2, '0');
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