document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            date: '',
            description: document.getElementById('description').value,
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
            try {
                const info = JSON.parse(this.$refs.dataDiv.getAttribute('data-info').replace(/'/g, '"'));
                this.date = info.date;
                this.counter = info.counter;
                this.selectedSiteId = info.selectedSiteId;
                this.selectedSystemId = info.selectedSystemId;
                this.selectedUserId = info.selectedUserId;
            } catch (error) {
                console.error('Error parsing JSON:', error);
            }
            this.fetchOps(),
            this.fetchUsers(),
            this.fetchSites(),
            this.fetchSystems(),
            this.authId = this.$el.getAttribute('data-auth-id');
        },
        computed: {
            formattedCounter() {
                return parseFloat(this.counter).toFixed(2);
            },
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

                const date = new Date(dateString);

                const year = date.getFullYear();
                const month = String(date.getMonth() + 1).padStart(2, '0');
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