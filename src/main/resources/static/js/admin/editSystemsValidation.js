document.addEventListener('DOMContentLoaded', function() {
new Vue({
    el: '#app',
    data: {
        sites: [],
        name: document.getElementById('name').value,
        siteId: document.getElementById('siteId').value,
        errors: {
            name: false,
            siteId: false,
        }
    },
    mounted() {
        this.fetchSites();
    },
    methods: {
        validateForm() {
            this.errors.name = this.name.length < 4;
            this.errors.siteId = !this.siteId;

            if (!this.errors.name && !this.errors.siteId) {
                this.$refs.editSystemForm.submit();
            }
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
        },
        submitForm() {
            this.$refs.editSystemForm.submit();
        }
    }
});
});