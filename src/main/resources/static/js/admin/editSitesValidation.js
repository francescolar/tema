document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            siteName: document.getElementById("name").value,
            siteAddress: document.getElementById("address").value,
            siteErrors: {
                siteName: [],
                siteAddress: []
            },
            googleApiKey: null
        },
        mounted() {
            this.loadGoogleMapsApi();
        },
        computed: {
            isSiteFormInvalid() {
                return (
                this.siteErrors.siteName.length > 0 ||
                this.siteErrors.siteAddress.length > 0 ||
                !this.siteName
                );
            }
        },
        methods: {
            validateSiteForm() {
                this.clearSiteErrors();
                this.validateSiteName();
                this.validateSiteAddress();

                if (!this.isSiteFormInvalid) {
                    this.$refs.editSiteForm.submit();
                }
            },

            validateSiteName() {
                this.siteErrors.siteName = [];

                if (!this.siteName) {
                    this.siteErrors.siteName.push("Il nome della sede non può essere vuoto.");
                } else if (this.siteName.length < 4) {
                    this.siteErrors.siteName.push("Il nome della sede deve avere almeno 4 caratteri.");
                } else if (this.siteName.length > 50) {
                    this.siteErrors.siteName.push("Il nome della sede può avere al massimo 50 caratteri.");
                } else if (/[^A-Za-z0-9\s'’\-@$!%*?&.]/.test(this.siteName)) {
                    this.siteErrors.siteName.push("Il nome della sede contiene caratteri non consentiti.");
                }
            },

            validateSiteAddress() {
                this.siteErrors.siteAddress = [];

                if (this.siteAddress && /[^A-Za-z0-9\s'’\-,.]/.test(this.siteAddress)) {
                    this.siteErrors.siteAddress.push("L'indirizzo contiene caratteri non consentiti.");
                }
            },

            clearSiteErrors() {
                this.siteErrors = {
                    siteName: [],
                    siteAddress: []
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
                        types: ["geocode"]
                    });

                    autocomplete.addListener("place_changed", () => {
                        const place = autocomplete.getPlace();

                        if (place.formatted_address) {
                            this.siteAddress = place.formatted_address;
                        }
                    });
                } else {
                    console.error("Indirizzo input non trovato!");
                }
            }
        }
    });
});