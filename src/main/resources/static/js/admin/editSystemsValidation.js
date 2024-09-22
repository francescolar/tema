document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            sites: [],
            systemName: '',
            selectedSiteId: '',
            systemErrors: {
                systemName: false,
                selectedSiteId: false,
            }
        },
        mounted() {
            try {
                const info = JSON.parse(this.$refs.systemDiv.getAttribute('data-info').replace(/'/g, '"'));
                this.systemName = info.systemName;
                this.selectedSiteId = info.selectedSiteId;
            } catch (error) {
                console.error('Error parsing JSON:', error);
            }
            this.fetchSites();
        },
        methods: {
            validateSystemForm() {
                this.clearSystemErrors();
                if (!this.systemName) {
                    this.systemErrors.systemName = true;
                }
                if (!this.selectedSiteId) {
                    this.systemErrors.selectedSiteId = true;
                }

                if (Object.values(this.systemErrors).every(value => !value)) {
                    this.$refs.editSystemForm.submit();
                }
            },
            clearSystemErrors() {
                this.systemErrors = {
                    systemName: false,
                    selectedSiteId: false,
                };
            },
            fetchSites() {
                axios.get('/api/sites/all')
                    .then(response => {
                    this.sites = response.data;
                })
                    .catch(error => {
                    console.error('Error fetching systems:', error);
                });
            },
            cancel() {
                this.$refs.cancel.submit();
            }
        }
    });
});