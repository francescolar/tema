document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            siteName: document.getElementById("name").value,
            address: document.getElementById("address").value,
            siteErrors: {
                siteName: false,
            },
            googleApiKey: null
        },
        mounted() {
            this.loadGoogleMapsApi();
        },
        methods: {
            validateSiteForm() {
                this.clearSiteErrors();
                if (!this.siteName) {
                    this.siteErrors.siteName = true;
                }

                if (Object.values(this.siteErrors).every(value => !value)) {
                    this.$refs.editSiteForm.submit();
                }
            },
            clearSiteErrors() {
                this.siteErrors = {
                    siteName: false,
                };
            },
            cancel() {
                this.$refs.cancel.submit();
            },
            async loadGoogleMapsApi() {
                try {
                    const response = await fetch('/api/google-maps-key');
                    const apiKey = await response.text();
                    this.googleApiKey = apiKey;

                    const script = document.createElement("script");
                    script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&libraries=places`;
                    script.async = true;
                    script.defer = true;
                    document.head.appendChild(script);

                    script.onload = this.initAutocomplete;
                } catch (error) {
                    console.error('Error fetching API key:', error);
                }
            },
            initAutocomplete() {
                const addressInput = document.getElementById("address");

                if (addressInput) {
                    const autocomplete = new google.maps.places.Autocomplete(addressInput, {
                        types: ["geocode"],
                    });

                    autocomplete.addListener("place_changed", () => {
                        const place = autocomplete.getPlace();

                        if (place.formatted_address) {
                            addressInput.value = place.formatted_address;
                        }
                    });
                } else {
                    console.error("Indirizzo input non trovato!");
                }
            }
        }
    });
});