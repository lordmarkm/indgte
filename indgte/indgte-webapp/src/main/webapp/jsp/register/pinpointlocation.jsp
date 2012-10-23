<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url value="/r/save/3/" var="urlSubmit" />

<script src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>

<div class="grid_12">
	<h3 style="margin: 10px;">Where in Dumaguete is ${regform.fullName }?</h3>
	<div class="map"></div>
	<div style="text-align: center;">
	<form:form method="post" commandName="regform" action="${urlSubmit }">
		<form:hidden path="latitude" />
		<form:hidden path="longitude" />
		<button class="satisfied">I'm satisfied with this location</button>
		<button class="nolatlng">${regform.fullName } doesn't have a Mappable location</button>
	</form:form>
	</div>
</div>

<div class="dialog" style="display: none;">
	${regform.fullName} is here?<br/>
	<button class="yes">Yes</button>
	<button class="no">No</button>
</div>

<style>
.map {
	width: 780px;
	height: 420px;
	margin: auto;
	border: 1px dotted grey;
}

#regform {
	margin: 10px 0;
}

.dialog {
	text-align: center;
}
</style>

<script>
window.constants = {
	businessName : '${regform.fullName}'
}
$(function(){
	//map
	var $map = $('.map'),
		$btnSatisfied = $('.satisfied').button('disable'),
		$btnNolatlng = $('.nolatlng'),
		$yes = $('.yes'),
		$no = $('.no'),
		$latitude = $('#latitude'),
		$longitude = $('#longitude');
	
	var map,
		infowindow,
		markerHoverInfoWindow,
		marker,
		latLng;
	
	function initializeMap() {
		var edit = $latitude.val() && $latitude.val() != 0;
		var lat = edit ? $latitude.val() : 9.3167;
		var lng = edit ? $longitude.val() : 123.3000;
		var zoom = edit ? 16 : 12;
		
		map = new google.maps.Map($map[0], {
			zoom: zoom,
			scrollwheel: false,
			panControl: false,
			streetViewControl: false,
			center: new google.maps.LatLng(lat, lng),
			mapTypeId: google.maps.MapTypeId.ROADMAP
		});
		
		if(edit) {
			makeMarker(new google.maps.LatLng(lat, lng));
		}
	}
	
	initializeMap();
	
	//click event
	google.maps.event.addListener(map, 'click', function(event){
		latLng = event.latLng;
		
		if(infowindow) infowindow.close();
		infowindow = new google.maps.InfoWindow({
			content: $('.dialog').clone(true).show()[0],
			position: latLng
		});
		infowindow.open(map);
	});
	
	function makeMarker(provLatLng) {
		if(marker) marker.setMap(null);
		marker = new google.maps.Marker({
			animation: google.maps.Animation.DROP,
			position: provLatLng ? provLatLng : latLng
		});
		marker.setMap(map);
		
		google.maps.event.addListener(marker, 'mouseover', function(event) {
			if(markerHoverInfoWindow) markerHoverInfoWindow.close(); 
			markerHoverInfoWindow = new google.maps.InfoWindow({
				content: constants.businessName,
			});
			markerHoverInfoWindow.open(map, marker);
		});
		
		google.maps.event.addListener(marker, 'mouseout', function(){
			if(markerHoverInfoWindow) markerHoverInfoWindow.close();
		});
		
		$btnSatisfied.button('enable');
	}
	
	$yes.click(function(event){
		event.stopPropagation();
		$latitude.val(latLng.lat());
		$longitude.val(latLng.lng());
		makeMarker();
		infowindow.close();
	});
	
	$no.click(function(event){
		event.stopPropagation();
		infowindow.close();
	});
	
	$btnNolatlng.click(function(){
		$latitude.val(0);
		$longitude.val(0);
	});
});
</script>