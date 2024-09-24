document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            sites: [],
            systems: [],
            siteId: null,
            siteName: '',
            systemName: '',
            selectedSiteId: null,

            siteNameFilter: '',
            cityFilter: '',
            numImpiantiCondition: 'equal',
            numImpiantiValue: null,
            totManutenzioneCondition: 'equal',
            totManutenzioneValue: null,
            excludeDisabledSites: false,
            excludeZeroSystems: false,

            systemNameFilter: '',
            totManutenzioneSystemCondition: 'equal',
            totManutenzioneSystemValue: null,
            excludeDisabledSystems: false,

            selectedSite: {},
            siteErrors: {
                siteName: [],
            },
            systemErrors: {
                systemName: [],
                selectedSiteId: [],
            },
            googleApiKey: null,
            currentPageSites: 1,
            itemsPerPageSites: 8
        },
        mounted() {
            this.fetchSites();
            this.fetchSystems();
            this.loadGoogleMapsApi();
        },
        computed: {
            filteredSites() {
                let filtered = this.sites;

                if (this.siteNameFilter) {
                    filtered = filtered.filter(site => site.name.toLowerCase().includes(this.siteNameFilter.toLowerCase()));
                }

                if (this.cityFilter) {
                    filtered = filtered.filter(site => site.address.toLowerCase().includes(this.cityFilter.toLowerCase()));
                }

                if (this.numImpiantiValue !== null) {
                    filtered = filtered.filter(site => {
                        if (this.numImpiantiCondition === 'greater') return site.totalSystems > this.numImpiantiValue;
                        if (this.numImpiantiCondition === 'less') return site.totalSystems < this.numImpiantiValue;
                        return site.totalSystems == this.numImpiantiValue;
                    });
                }

                if (this.totManutenzioneValue !== null) {
                    filtered = filtered.filter(site => {
                        if (this.totManutenzioneCondition === 'greater') return site.totalWorkHours > this.totManutenzioneValue;
                        if (this.totManutenzioneCondition === 'less') return site.totalWorkHours < this.totManutenzioneValue;
                        return site.totalWorkHours == this.totManutenzioneValue;
                    });
                }

                if (this.excludeDisabledSites) {
                    filtered = filtered.filter(site => site.enabled);
                }

                if (this.excludeZeroSystems) {
                    filtered = filtered.filter(site => site.totalSystems === 0);
                }

                return filtered;
            },
            paginatedSites() {
                let start = (this.currentPageSites - 1) * this.itemsPerPageSites;
                let end = start + this.itemsPerPageSites;

                return this.filteredSites.slice(start, end);
            },
            totalPagesSites() {
                return Math.ceil(this.filteredSites.length / this.itemsPerPageSites);
            },
            filteredSystems() {
                let filtered = this.getSystemsForSite();

                if (this.systemNameFilter) {
                    filtered = filtered.filter(system => system.name.toLowerCase().includes(this.systemNameFilter.toLowerCase()));
                }

                if (this.totManutenzioneSystemValue !== null) {
                    filtered = filtered.filter(system => {
                        if (this.totManutenzioneSystemCondition === 'greater') return system.totalWorkHours > this.totManutenzioneSystemValue;
                        if (this.totManutenzioneSystemCondition === 'less') return system.totalWorkHours < this.totManutenzioneSystemValue;
                        return system.totalWorkHours == this.totManutenzioneSystemValue;
                    });
                }

                if (this.excludeDisabledSystems) {
                    filtered = filtered.filter(system => system.enabled);
                }

                return filtered;
            },
            isSiteFormInvalid() {
                return (
                !this.siteName || this.siteErrors.siteName.length > 0
                );
            },
            isSystemFormInvalid() {
                return (
                !this.systemName || !this.selectedSiteId ||
                this.systemErrors.systemName.length > 0 || this.systemErrors.selectedSiteId.length > 0
                );
            }
        },
        methods: {
            showSiteDetails(site) {
                this.selectedSite = site;
            },
            validateSiteForm() {
                this.clearSiteErrors();

                this.validateSiteName();

                if (Object.values(this.siteErrors).every(errorArray => errorArray.length === 0)) {
                    this.$refs.siteForm.submit();
                }
            },
            validateSiteName() {
                this.siteErrors.siteName = [];

                if (!this.siteName) {
                    this.siteErrors.siteName.push("Il nome della sede non può essere vuoto.");
                } else {
                    if (this.siteName.length < 4) {
                        this.siteErrors.siteName.push("Il nome della sede deve avere almeno 4 caratteri.");
                    }
                    if (this.siteName.length > 50) {
                        this.siteErrors.siteName.push("Il nome della sede può avere al massimo 50 caratteri.");
                    }
                }
            },
            clearSiteErrors() {
                this.siteErrors = {
                    siteName: [],
                };
            },
            validateSystemForm() {
                this.clearSystemErrors();

                this.validateSystemName();

                if (!this.selectedSiteId) {
                    this.systemErrors.selectedSiteId.push("Seleziona una sede.");
                }

                if (Object.values(this.systemErrors).every(errorArray => errorArray.length === 0)) {
                    this.$refs.systemForm.submit();
                }
            },
            validateSystemName() {
                this.systemErrors.systemName = [];

                if (!this.systemName) {
                    this.systemErrors.systemName.push("Inserisci il nome per l'impianto.");
                } else {
                    if (this.systemName.length < 4) {
                        this.systemErrors.systemName.push("Il nome dell'impianto deve avere almeno 4 caratteri.");
                    }
                    if (this.systemName.length > 50) {
                        this.systemErrors.systemName.push("Il nome dell'impianto può avere al massimo 50 caratteri.");
                    }
                }
            },
            clearSystemErrors() {
                this.systemErrors = {
                    systemName: [],
                    selectedSiteId: [],
                };
            },
            applySiteFilter() {
                this.filteredSites;
            },
            applySystemFilter() {
                this.filteredSystems;
            },
            resetSiteFilters() {
                this.siteNameFilter = '';
                this.cityFilter = '';
                this.numImpiantiCondition = 'equal';
                this.numImpiantiValue = null;
                this.totManutenzioneCondition = 'equal';
                this.totManutenzioneValue = null;
                this.excludeDisabledSites = false;
                this.excludeZeroSystems = false;
                this.applySiteFilter();
            },
            resetSystemFilters() {
                this.systemNameFilter = '';
                this.totManutenzioneSystemCondition = 'equal';
                this.totManutenzioneSystemValue = null;
                this.excludeDisabledSystems = false;
                this.applySystemFilter();
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