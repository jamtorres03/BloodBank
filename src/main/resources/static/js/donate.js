var requests;

function initMap() {
	var broadway = {
		info: '<strong>Chipotle on Broadway</strong><br>\
					5224 N Broadway St<br> Chicago, IL 60640<br>\
					<a href="https://goo.gl/maps/jKNEDz4SyyH2">Get Directions</a>',
		lat: 41.976816,
		long: -87.659916
	};

	var belmont = {
		info: '<strong>Chipotle on Belmont</strong><br>\
					1025 W Belmont Ave<br> Chicago, IL 60657<br>\
					<a href="https://goo.gl/maps/PHfsWTvgKa92">Get Directions</a>',
		lat: 41.939670,
		long: -87.655167
	};

	var sheridan = {
		info: '<strong>Chipotle on Sheridan</strong><br>\r\
					6600 N Sheridan Rd<br> Chicago, IL 60626<br>\
					<a href="https://goo.gl/maps/QGUrqZPsYp92">Get Directions</a>',
		lat: 42.002707,
		long: -87.661236
	};

	var locations = [
      [broadway.info, broadway.lat, broadway.long, 0],
      [belmont.info, belmont.lat, belmont.long, 1],
      [sheridan.info, sheridan.lat, sheridan.long, 2],
    ];

	var map = new google.maps.Map(document.getElementById('map'), {
		zoom: 13,
		center: new google.maps.LatLng(41.976816, -87.659916),
		mapTypeId: google.maps.MapTypeId.ROADMAP
	});

	var infowindow = new google.maps.InfoWindow({});

	// Try HTML5 geolocation.
	if (navigator.geolocation) {
	  navigator.geolocation.getCurrentPosition(function(position) {
	    var pos = {
	      lat: position.coords.latitude,
	      lng: position.coords.longitude
	    };
	
	    infowindow.setPosition(pos);
	    infowindow.setContent('Location found.');
	    infowindow.open(map);
	    map.setCenter(pos);
	  }, function() {
	    handleLocationError(true, infowindow, map.getCenter());
	  });
	} else {
	  // Browser doesn't support Geolocation
	  handleLocationError(false, infowindow, map.getCenter());
	}
	
	$.ajax({
	    type : 'POST',
	    url : '/admin/requests',
	    dataType : 'json',
	    contentType : 'application/json',
	    success : function(data) {
	    	requests = data;
	    	console.log(requests);
	    	var marker, i;
	    	for (i = 0; i < requests.length; i++) {
	    		marker = new google.maps.Marker({
	    			position: new google.maps.LatLng(requests[i].latitude, requests[i].longitude),
	    			map: map
	    		});

	    		google.maps.event.addListener(marker, 'click', (function (marker, i) {
	    			return function () {
	    				infowindow.setContent("<div> Requested by: " + requests[i].requestedBy.name + "</div>" + 
	    						"<div>Blood Type: " + requests[i].requestedBy.bloodType + "</div>" +
	    						"<div>Location: " + requests[i].location + "</div>" +
	    						"<div class='text-right mt10'><button class='donate-btn' onclick=donate('" + requests[i].uuid + "') type='Submit'>Donate</button></div>");
	    				infowindow.open(map, marker);
	    			};
	    		})(marker, i));
	    	}
	    },
	    error : function() {
	        alert('error');
	    }
	});
}

function handleLocationError(browserHasGeolocation, infoWindow, pos) {
	infoWindow.setPosition(pos);
	infoWindow.setContent(browserHasGeolocation ? 'Error: The Geolocation service failed.' : 'Error: Your browser doesn\'t support geolocation.');
    infoWindow.open(map);
}

var placeSearch, autocomplete;

var componentForm = {
	street_number: 'short_name',
	route: 'long_name',
	locality: 'long_name',
	administrative_area_level_1: 'short_name',
	country: 'long_name',
	postal_code: 'short_name'
};

function initAutocomplete() {
	initMap();
	// Create the autocomplete object, restricting the search to geographical location types.
	autocomplete = new google.maps.places.Autocomplete(
    /** @type {!HTMLInputElement} */(document.getElementById('autocomplete')), {types: ['geocode']});

	// When the user selects an address from the dropdown, populate the address
	// fields in the form.
	autocomplete.addListener('place_changed', fillInAddress);
}

function fillInAddress() {
    // Get the place details from the autocomplete object.
	var place = autocomplete.getPlace();

    console.log(place.name);
    $("#latitude").val(place.geometry.location.lat());
    $("#longitude").val(place.geometry.location.lng());
}

function donate (uuid) {
	console.log("test" + uuid);
	$.ajax({
	    type : 'GET',
	    url : '/admin/donate-now',
	    data: ({uuid : uuid}),
	    success : function(data) {
	    	console.log(data);
	    	$(".donee").text(data.requestedBy.name);
	    	$(".contact").text(data.requestedBy.contactNo);
	    	$('#deleteTopicModal').modal('show');
	    },
	    error : function() {
	        alert('error');
	    }
	});
}

