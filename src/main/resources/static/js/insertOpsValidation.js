document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#operationForm',
        data: {
            sites: [],
            systems: [],
            counter: 1,
            selectedSiteId: '',
            selectedSystemId: '',
            date: '',
            description: '',
            errors: {
                selectedSiteId: false,
                selectedSystemId: false,
                date: false,
                description: false,
            }
        },
        mounted() {
            this.fetchSites();
            this.fetchSystems();

        },
        computed: {
            formattedCounter() {
                return parseFloat(this.counter).toFixed(2);
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
                this.errors.date ||
                this.errors.description ||
                !this.selectedSiteId ||
                !this.selectedSystemId ||
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
                    date: false,
                    description: false,
                };
            },

            fetchSites() {
                axios.get('/api/sites/all-enabled')
                    .then(response => {
                    this.sites = response.data;
                })
                    .catch(error => {
                    console.error('Error fetching sites:', error);
                });
            },

            fetchSystems() {
                axios.get('/api/systems/all-enabled')
                    .then(response => {
                    this.systems = response.data;
                })
                    .catch(error => {
                    console.error('Error fetching systems:', error);
                });
            },

            increase(value) {
                this.counter = Math.min(100, this.counter + parseFloat(value));
            },

            decrease(value) {
                this.counter = Math.max(1, this.counter - parseFloat(value));
            }
        }
    });
});