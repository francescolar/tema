document.addEventListener('DOMContentLoaded', function() {
  new Vue({
    el: '#app',
    data: {
      sites: [],
      systems: [],
      siteId: null
    },
    mounted() {
      this.fetchSites();
      this.fetchSystems();
    },
    methods: {
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
      }
    }
  });
});

async function loadGoogleMapsApi() {
    try {
      const response = await fetch('/api/google-maps-key');
      const apiKey = await response.text();

      const script = document.createElement("script");
      script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&libraries=places&callback=initMap`;
      document.head.appendChild(script);
    } catch (error) {
      console.error('Error fetching API key:', error);
    }
  }

  loadGoogleMapsApi();




async function initMap() {
  // Request needed libraries.
  //@ts-ignore
  await google.maps.importLibrary("places");

  // Get the input element by ID.
  const addressInput = document.getElementById("address");

  // Create the autocomplete object, restricting the search to geographical location types.
  //@ts-ignore
  const autocomplete = new google.maps.places.Autocomplete(addressInput, {
    types: ["geocode"], // You can restrict to certain types like geocode if needed
  });

  // When the user selects an address from the dropdown
  autocomplete.addListener("place_changed", () => {
    const place = autocomplete.getPlace();

    // Populate the address field with the formatted address
    if (place.formatted_address) {
      addressInput.value = place.formatted_address;
    }
  });
}

initMap();