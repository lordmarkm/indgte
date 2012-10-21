<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url value="/r/save/3/" var="urlSubmit" />

<script src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>

<div class="grid_12">
<div class="map"></div>
<form:form method="post" commandName="regform" action="${urlSubmit }">
	<form:hidden path="latitude" value="0" />
	<form:hidden path="longitude" value="0" />
	<input type="submit" value="Submit fake LatLng" />
</form:form>
</div>

<div class="dialog" style="display: none;">
	${regform.fullName} is here?<br/>
	<button class="yes">Yes</button>
	<button class="no">No</button>
</div>

<style>
.map {
	width: 640px;
	height: 480px;
	margin: auto;
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
		map = new google.maps.Map($map[0], {
			zoom: 12,
			center: new google.maps.LatLng(9.3167, 123.3000),
			mapTypeId: google.maps.MapTypeId.ROADMAP
		});		
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
	
	$yes.click(function(event){
		event.stopPropagation();
		$latitude.val(latLng.lat());
		$longitude.val(latLng.lng());

		if(marker) marker.setMap(null);
		marker = new google.maps.Marker({
			animation: google.maps.Animation.DROP,
			position: latLng
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
		
		infowindow.close();
	});
	
	$no.click(function(event){
		event.stopPropagation();
		infowindow.close();
	});
});
</script>