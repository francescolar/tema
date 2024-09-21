document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            ops: [],
            searchOps: '',
            authId: null
        },
        mounted() {
            this.fetchOps(),
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
            }
        },
        methods: {
            fetchOps() {
                axios.get('/api/operations/owned')
                .then(response => {
                    this.ops = response.data;
                })
                .catch(error => {
                    console.error('Error fetching ops:', error);
                });
            }
        }
    });
});