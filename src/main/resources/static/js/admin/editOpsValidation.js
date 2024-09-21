document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            date: '',
            description: '',
            ops: [],
            searchOps: '',
            sites: [],
            systems: [],
            users: [],
            counter: 1,
            selectedSiteId: '',
            selectedSystemId: '',
            selectedUserId: '',
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
            this.date = this.$refs.dataDiv.getAttribute('data-date');
            this.fetchOps(),
            this.fetchUsers(),
            this.fetchSites(),
            this.fetchSystems(),
            this.authId = this.$el.getAttribute('data-auth-id');
        },
        computed: {
            filteredOps() {
                if (!this.searchOps) return this.ops;
                return this.ops.filter(op =>
                Object.keys(op).some(key =>
                String(op[key]).toLowerCase().includes(this.searchOps.toLowerCase())
                )
                );
            },
            filteredSystems() {
                if (!this.selectedSiteId) {
                    return [];
                }
                return this.systems.filter(system => system.siteId === parseInt(this.selectedSiteId));
            },
            formattedDate() {
                return this.formatDate(this.date);
            }
        },
        methods: {
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
            fetchOps() {
                axios.get('/api/operations/all')
                    .then(response => {
                    this.ops = response.data;
                })
                    .catch(error => {
                    console.error('Error fetching ops:', error);
                });
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

                return `${year}-${month}-${day} ${hours}:${minutes}`;
            },
            fetchSites() {
                axios.get('/api/sites/all')
                    .then(response => {
                    this.sites = response.data;
                })
                    .catch(error => {
                    console.error('Error fetching sites:', error);
                })
            },
            fetchSystems() {
                axios.get('/api/systems/all')
                    .then(response => {
                    this.systems = response.data;
                })
                    .catch(error => {
                    console.error('Error fetching sites:', error);
                })
            },
            fetchUsers() {
                axios.get('/api/users/all-no-admin')
                    .then(response => {
                    console.log('Users data:', response.data);
                    this.users = response.data;
                })
                    .catch(error => {
                    console.error('Error fetching users:', error);
                })
            },
            increase(value) {
                this.counter += value;
            },
            decrease(value) {
                if (this.counter - value >= 1) {
                    this.counter -= value;
                } else {
                    this.counter = 1;
                }
            }
        }
    });
});