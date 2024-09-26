document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            sites: [],
            systemName: document.getElementById("name").value,
            selectedSiteId: '',
            systemErrors: {
                systemName: [],
                selectedSiteId: []
            }
        },
        mounted() {
            try {
                const info = JSON.parse(this.$refs.systemDiv.getAttribute('data-info').replace(/'/g, '"'));
                this.selectedSiteId = info.selectedSiteId;
            } catch (error) {
                console.error('Error parsing JSON:', error);
            }
            this.fetchSites();
        },
        computed: {
            isSystemFormInvalid() {
                return (
                !this.systemName ||
                this.systemErrors.systemName.length > 0 ||
                this.systemErrors.selectedSiteId.length > 0 ||
                !this.selectedSiteId
                );
            }
        },
        methods: {
            validateSystemForm() {
                this.clearSystemErrors();
                this.validateSystemName();
                this.validateSelectedSiteId();

                if (!this.isSystemFormInvalid) {
                    this.$refs.editSystemForm.submit();
                }
            },

            validateSystemName() {
                this.systemErrors.systemName = [];

                if (!this.systemName) {
                    this.systemErrors.systemName.push("Il nome dell'impianto non può essere vuoto.");
                } else if (this.systemName.length < 4) {
                    this.systemErrors.systemName.push("Il nome dell'impianto deve avere almeno 4 caratteri.");
                } else if (this.systemName.length > 50) {
                    this.systemErrors.systemName.push("Il nome dell'impianto può avere al massimo 50 caratteri.");
                } else if (/[^A-Za-z0-9\s'’\-@$!%*?&.]/.test(this.systemName)) {
                    this.systemErrors.systemName.push("Il nome dell'impianto contiene caratteri non consentiti.");
                }
            },

            validateSelectedSiteId() {
                this.systemErrors.selectedSiteId = [];

                if (!this.selectedSiteId) {
                    this.systemErrors.selectedSiteId.push("Seleziona una sede.");
                }
            },

            clearSystemErrors() {
                this.systemErrors = {
                    systemName: [],
                    selectedSiteId: []
                };
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

            cancel() {
                this.$refs.cancel.submit();
            }
        }
    });
});