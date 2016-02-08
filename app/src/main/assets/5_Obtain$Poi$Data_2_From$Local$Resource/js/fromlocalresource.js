// implementation of AR-Experience (aka "World")
var World = {
	// you may request new data from server periodically, however: in this sample data is only requested once
	isRequestingData: false,

	// true once data was fetched
	initiallyLoadedData: false,

	// different POI-Marker assets
	markerDrawable_idle: null,
	markerDrawable_selected: null,
	markerDrawable_directionIndicator: null,

	// list of AR.GeoObjects that are currently shown in the scene / World
	markerList: [],

	// The last selected marker
	currentMarker: null,

	//kr: radar
	locationUpdateCounter: 0,
    updatePlacemarkDistancesEveryXLocationUpdates: 10,

	// called to inject new POI data
	loadPoisFromJsonData: function loadPoisFromJsonDataFn(poiData) {

		//kr: show radar & set click-listener
        PoiRadar.show();
        $('#radarContainer').unbind('click');
        $("#radarContainer").click(PoiRadar.clickedRadar);

		// empty list of visible markers
		World.markerList = [];

		// start loading marker assets
		World.markerDrawable_idle = new AR.ImageResource("assets/marker_idle.png");
		World.markerDrawable_selected = new AR.ImageResource("assets/marker_selected.png");
		World.markerDrawable_directionIndicator = new AR.ImageResource("assets/indi.png");

		// loop through POI-information and create an AR.GeoObject (=Marker) per POI
		for (var currentPlaceNr = 0; currentPlaceNr < poiData.length; currentPlaceNr++) {
			var singlePoi = {
				"id": poiData[currentPlaceNr].id,
				"latitude": parseFloat(poiData[currentPlaceNr].latitude),
				"longitude": parseFloat(poiData[currentPlaceNr].longitude),
				"altitude": parseFloat(poiData[currentPlaceNr].altitude),
				"title": poiData[currentPlaceNr].name,
				"description": poiData[currentPlaceNr].description
			};

			World.markerList.push(new Marker(singlePoi));
		}

        //kr: updates distance information of all placemarks
        World.updateDistanceToUserValues();

		World.updateStatusMessage(currentPlaceNr + ' places loaded');
	},

    //kr: sets/updates distances of all makers so they are available way faster than calling (time-consuming) distanceToUser() method all the time
    updateDistanceToUserValues: function updateDistanceToUserValuesFn() {
        for (var i = 0; i < World.markerList.length; i++) {
            World.markerList[i].distanceToUser = World.markerList[i].markerObject.locations[0].distanceToUser();
        }
    },

	// updates status message shon in small "i"-button aligned bottom center
	updateStatusMessage: function updateStatusMessageFn(message, isWarning) {

		var themeToUse = isWarning ? "e" : "c";
		var iconToUse = isWarning ? "alert" : "info";

		$("#status-message").html(message);
		$("#popupInfoButton").buttonMarkup({
			theme: themeToUse
		});
		$("#popupInfoButton").buttonMarkup({
			icon: iconToUse
		});
	},

	/*KR:
    		It may make sense to display POI details in your native style.
    		In this sample a very simple native screen opens when user presses the 'More' button in HTML.
    		This demoes the interaction between JavaScript and native code.
    	*/
    	// user clicked "More" button in POI-detail panel -> fire event to open native screen
    	onPoiDetailMoreButtonClicked: function onPoiDetailMoreButtonClickedFn() {
    		var currentMarker = World.currentMarker;
    		var architectSdkUrl = "architectsdk://markerselected?id=" + encodeURIComponent(currentMarker.poiData.id) + "&title=" + encodeURIComponent(currentMarker.poiData.title) + "&description=" + encodeURIComponent(currentMarker.poiData.description);
    		/*
    			The urlListener of the native project intercepts this call and parses the arguments.
    			This is the only way to pass information from JavaSCript to your native code.
    			Ensure to properly encode and decode arguments.
    			Note: you must use 'document.location = "architectsdk://...' to pass information from JavaScript to native.
    			! This will cause an HTTP error if you didn't register a urlListener in native architectView !
    		*/
    		document.location = architectSdkUrl;
    	},

	// location updates, fired every time you call architectView.setLocation() in native environment
	locationChanged: function locationChangedFn(lat, lon, alt, acc) {

		// request data if not already present
		if (!World.initiallyLoadedData) {
			World.requestDataFromLocal(lat, lon);
			World.initiallyLoadedData = true;
		}

		//kr: helper used to update placemark information every now and then (e.g. every 10 location upadtes fired)
        World.locationUpdateCounter = (++World.locationUpdateCounter % World.updatePlacemarkDistancesEveryXLocationUpdates);
	},

	// fired when user pressed maker in cam
	onMarkerSelected: function onMarkerSelectedFn(marker) {

		// deselect previous marker
		if (World.currentMarker) {
			if (World.currentMarker.poiData.id == marker.poiData.id) {
				return;
			}
			World.currentMarker.setDeselected(World.currentMarker);
		}

		// highlight current one
		marker.setSelected(marker);
		World.currentMarker = marker;
	},

	// screen was clicked but no geo-object was hit
	onScreenClick: function onScreenClickFn() {
		if (World.currentMarker) {
			World.currentMarker.setDeselected(World.currentMarker);
		}
	},

	/*
		In case the data of your ARchitect World is static the content should be stored within the application. 
		Create a JavaScript file (e.g. myJsonData.js) where a globally accessible variable is defined.
		Include the JavaScript in the ARchitect Worlds HTML by adding <script src="js/myJsonData.js"/> to make POI information available anywhere in your JavaScript.
	*/

	// request POI data
	requestDataFromLocal: function requestDataFromLocalFn(lat, lon) {

		/*var poisNearby = Helper.bringPlacesToUser(myJsonData, lat, lon);
		World.loadPoisFromJsonData(poisNearby);*/

		/*
		For demo purpose they are relocated randomly around the user using a 'Helper'-function.
		Comment out previous 2 lines and use the following line > instead < to use static values 1:1. 
		*/

		//World.loadPoisFromJsonData(myJsonData);
	}

};

/*var Helper = {
	*//*
		For demo purpose only, this method takes poi data and a center point (latitude, longitude) to relocate the given places randomly around the user
	*//*
	bringPlacesToUser: function bringPlacesToUserFn(poiData, latitude, longitude) {
		for (var i = 0; i < poiData.length; i++) {
			poiData[i].latitude = latitude + (Math.random() / 5 - 0.1);
			poiData[i].longitude = longitude + (Math.random() / 5 - 0.1);
			*//*
			Note: setting altitude to '0'
			will cause places being shown below / above user,
			depending on the user 's GPS signal altitude. 
				Using this contant will ignore any altitude information and always show the places on user-level altitude
			*//*
			poiData[i].altitude = AR.CONST.UNKNOWN_ALTITUDE;
		}
		return poiData;
	}
}*/


/* forward locationChanges to custom function */
AR.context.onLocationChanged = World.locationChanged;

/* forward clicks in empty area to World */
AR.context.onScreenClick = World.onScreenClick;