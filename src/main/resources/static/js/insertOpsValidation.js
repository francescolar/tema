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
            this.fetchSites(),
            this.fetchSystems()
        },
           computed: {
              filteredSystems() {
                if (!this.selectedSiteId) {
                  return [];
                }
                return this.systems.filter(system => system.siteId === parseInt(this.selectedSiteId));
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
                })
            },
            fetchSystems() {
                axios.get('/api/systems/all-enabled')
                .then(response => {
                    this.systems = response.data;
                })
                .catch(error => {
                    console.error('Error fetching sites:', error);
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
    })
});