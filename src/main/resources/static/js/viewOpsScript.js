document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            ops: [],
            searchOps: '',
            selectedOp: {},
            authId: null
        },
        mounted() {
            this.fetchOps(),
            this.authId = this.$el.getAttribute('data-auth-id');

            const successToast = document.getElementById('successToast');
            if (successToast) {
                const toast = new bootstrap.Toast(successToast);
                toast.show();
            }
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
            showOpDetails(op) {
                this.selectedOp = op;
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