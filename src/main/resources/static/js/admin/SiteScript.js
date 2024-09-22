document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            sites: [],
            systems: [],
            siteId: null,
            siteName: '',
            systemName: '',
            selectedSiteId: '',
            // Filtri siti
            siteNameFilter: '',
            cityFilter: '',
            numImpiantiCondition: 'equal',
            numImpiantiValue: null,
            totManutenzioneCondition: 'equal',
            totManutenzioneValue: null,
            excludeDisabledSites: false,
            excludeZeroSystems: false,

            // Filtri impianti
            systemNameFilter: '',
            totManutenzioneSystemCondition: 'equal',
            totManutenzioneSystemValue: null,
            excludeDisabledSystems: false,

            selectedSite: {},
            siteErrors: {
                siteName: false,
            },
            systemErrors: {
                systemName: false,
                selectedSiteId: false,
            },
            googleApiKey: null
        },
        mounted() {
            this.fetchSites();
            this.fetchSystems();
            this.loadGoogleMapsApi();
        },
        computed: {
            filteredSites() {
                let filtered = this.sites;

                // Filtro per nome sede
                if (this.siteNameFilter) {
                    filtered = filtered.filter(site => site.name.toLowerCase().includes(this.siteNameFilter.toLowerCase()));
                }

                // Filtro per città
                if (this.cityFilter) {
                    filtered = filtered.filter(site => site.address.toLowerCase().includes(this.cityFilter.toLowerCase()));
                }

                // Filtro per numero impianti
                if (this.numImpiantiValue !== null) {
                    filtered = filtered.filter(site => {
                        if (this.numImpiantiCondition === 'greater') return site.totalSystems > this.numImpiantiValue;
                        if (this.numImpiantiCondition === 'less') return site.totalSystems < this.numImpiantiValue;
                        return site.totalSystems == this.numImpiantiValue;
                    });
                }

                // Filtro per ore manutenzione
                if (this.totManutenzioneValue !== null) {
                    filtered = filtered.filter(site => {
                        if (this.totManutenzioneCondition === 'greater') return site.totalWorkHours > this.totManutenzioneValue;
                        if (this.totManutenzioneCondition === 'less') return site.totalWorkHours < this.totManutenzioneValue;
                        return site.totalWorkHours == this.totManutenzioneValue;
                    });
                }

                // Escludi sedi disabilitate
                if (this.excludeDisabledSites) {
                    filtered = filtered.filter(site => site.enabled);
                }

                // Escludi sedi con 0 impianti
                if (this.excludeZeroSystems) {
                    filtered = filtered.filter(site => site.totalSystems === 0);
                }

                return filtered;
            },
            filteredSystems() {
                let filtered = this.getSystemsForSite();

                // Filtro per nome impianto
                if (this.systemNameFilter) {
                    filtered = filtered.filter(system => system.name.toLowerCase().includes(this.systemNameFilter.toLowerCase()));
                }

                // Filtro per ore manutenzione
                if (this.totManutenzioneSystemValue !== null) {
                    filtered = filtered.filter(system => {
                        if (this.totManutenzioneSystemCondition === 'greater') return system.totalWorkHours > this.totManutenzioneSystemValue;
                        if (this.totManutenzioneSystemCondition === 'less') return system.totalWorkHours < this.totManutenzioneSystemValue;
                        return system.totalWorkHours == this.totManutenzioneSystemValue;
                    });
                }

                // Escludi impianti disabilitati
                if (this.excludeDisabledSystems) {
                    filtered = filtered.filter(system => system.enabled);
                }

                return filtered;
            }
        },
        methods: {
            showSiteDetails(site) {
                this.selectedSite = site; // Imposta il sito selezionato nel modal
            },
            validateSiteForm() {
                this.clearSiteErrors();
                if (!this.siteName) {
                    this.siteErrors.siteName = true;
                }

                if (Object.values(this.siteErrors).every(value => !value)) {
                    this.$refs.siteForm.submit();
                }
            },
            clearSiteErrors() {
                this.siteErrors = {
                    siteName: false,
                };
            },
            validateSystemForm() {
                this.clearSystemErrors();
                if (!this.systemName) {
                    this.systemErrors.systemName = true;
                }
                if (!this.selectedSiteId) {
                    this.systemErrors.selectedSiteId = true;
                }

                if (Object.values(this.systemErrors).every(value => !value)) {
                    this.$refs.systemForm.submit();
                }
            },
            clearSystemErrors() {
                this.systemErrors = {
                    systemName: false,
                    selectedSiteId: false,
                };
            },
            applySiteFilter() {
                this.filteredSites; // Forza il ricalcolo dei filtri
            },
            // Applica i filtri agli impianti
            applySystemFilter() {
                this.filteredSystems; // Forza il ricalcolo dei filtri
            },
            // Reset filtri siti
            resetSiteFilters() {
                this.siteNameFilter = '';
                this.cityFilter = '';
                this.numImpiantiCondition = 'equal';
                this.numImpiantiValue = null;
                this.totManutenzioneCondition = 'equal';
                this.totManutenzioneValue = null;
                this.excludeDisabledSites = false;
                this.excludeZeroSystems = false;
                this.applySiteFilter(); // Ricalcola i dati filtrati
            },
            // Reset filtri impianti
            resetSystemFilters() {
                this.systemNameFilter = '';
                this.totManutenzioneSystemCondition = 'equal';
                this.totManutenzioneSystemValue = null;
                this.excludeDisabledSystems = false;
                this.applySystemFilter(); // Ricalcola i dati filtrati
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
            fetchSystems() {
                axios.get('/api/systems/all')
                    .then(response => {
                    this.systems = response.data;
                })
                    .catch(error => {
                    console.error('Error fetching systems:', error);
                });
            },
            getSystemsForSite() {
                return this.systems.filter(system => system.siteId === this.siteId);
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