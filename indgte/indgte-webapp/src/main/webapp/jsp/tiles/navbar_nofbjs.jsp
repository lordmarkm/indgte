<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<spring:url var="urlHome" value="/" />
<spring:url var="urlProfile" value="/p/" />
<spring:url var="urlMyBusinesses" value="/p/businesses" />
<spring:url var="urlHelp" value="/etc/help/" />
<spring:url var="urlLogout" value="/j_spring_security_logout" />
<spring:url var="cssNavbar" value="/resources/css/navbar.css" />

<link rel="stylesheet" href="${cssNavbar }" />

<section class="grid_12 navbar ui-widget-header">
	<div class="user-container">
		<img class="user-image" src="${user.imageUrl }" />
		<strong class="user-username">${user.username }</strong>
	</div>
	
	<div class="menu-container">
		<div class="triangle-container" style="display:inline-block; height: 100%;">
			<span class="ui-icon ui-icon-triangle-1-s"></span>
		</div>
		<div class="user-menu-partialborder"></div>
		<div class="user-menu">
			<div class="user-menu-item"><a class="loadhere" href="${urlProfile }">View Profile</a></div>
			<div class="user-menu-item"><a class="loadhere" href="${urlMyBusinesses }">View My Businesses</a></div>
			<div class="user-menu-divider">&nbsp;</div>
			<div class="user-menu-item">Account Settings</div>
			<div class="user-menu-divider">&nbsp;</div>
			<div class="user-menu-item"><a href="${urlLogout }">Logout</a></div>
			<div class="user-menu-item"><a class="loadhere" href="${urlHelp }">Help</a></div>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
			<div class="user-menu-divider">&nbsp;</div>
			<div class="user-menu-item"><a href="<spring:url value='/a/daotest/'/>">Dao Tester</a></div>
			</sec:authorize>
		</div>
	</div>
	
	<div class="search-container">
		<input class="search-input" type="text" placeholder="Search for stuff in Dumaguete"/>
		<button>&nbsp;</button>
		<div class="autocomplete-results ui-state-highlight" style="display: none;"></div>
	</div>
	
	<div class="navigation-container floatright">
		<strong class="navigation home"><a href="${urlHome }">Home</a></strong>
		<strong class="navigation feed">Feed</strong>
		<strong class="navigation chat">Chat</strong>
	</div>
</section>

<script>
window.navbar = {
	search : {
		minlength : 3, //god's number trololololol
		url : '<spring:url value="/s/" />',
		urlBusiness: '<spring:url value="/p/" />',
		urlImgur: 'http://i.imgur.com/'
	}
}

$(function(){
	var $btnShowUsermenu = $('.menu-container'),
		$userMenuTopBorder = $('.user-menu-partialborder');
		$userMenu = $('.user-menu')
		$userMenuItem = $('.user-menu-item'),
		$btnSearch = $('.search-container button'),
		$searchInput = $('.search-input'),
		$autocompleteResults = $('.autocomplete-results')
		$navigation = $('.navigation');
	
	//show user-menu
	$btnShowUsermenu.click(function(event){
		var $that = $(this);
		event.stopPropagation();
		$that.toggleClass('active');
		$userMenuTopBorder.toggle();
		$userMenu.toggle();
	});
	
	//clicking the user-menu-item div follows its a child link
	$userMenuItem.click(function(){
		var $that = $(this);
		var $a = $that.find('a:first-child');
		
		if($a.hasClass('loadhere')) {
			$('#body').load($a.attr('href') + '?loadhere=true');
		} else {
			window.location.href = $that.find('a:only-child').attr('href');	
		}
	});
	
	//same goes for navigation
	$navigation.click(function(){
		var $that = $(this);
		var $a = $that.find('a:first-child');
		window.location.href = $that.find('a:only-child').attr('href');	
	});
	
	$btnSearch.button({ icons: {primary: 'ui-icon-search'}});
	var searchtimeout;
	$searchInput.bind({
		keyup: startTimeout,
		paste: startTimeout,
		focus: startTimeout
	});
	
	function startTimeout() {
		if(searchtimeout) {
			clearTimeout(searchtimeout);
		}
		searchtimeout = setTimeout(autocomplete, 500);
	}
	
	function autocomplete() {
		var term = $searchInput.val();
		if(term.length < navbar.search.minlength) return;
		$.get(navbar.search.url + term + '.json', function(response){
			switch(response.status) {
			case '200':
				$autocompleteResults.html('');
				
				debug(response);
				var visibleResults = 0;
				
				function makeAutocompleteResult(result, urlRoot) {
					var $auto = $('<div class="autocomplete-container">').appendTo($autocompleteResults);
					$('<img class="autocomplete-img">').attr('src', navbar.search.urlImgur + result.thumbnailHash + 's.jpg').appendTo($auto);
					$('<div class="autocomplete-title">').append($('<a>').attr('href', urlRoot + result.identifier).text(result.title)).appendTo($auto);
					
					if(result.description.length < 80) {
						$('<div class="autocomplete-description">').text(result.description).appendTo($auto);
					} else {
						var $morelink = $('<a style="font-weight: bold;">').attr('href', urlRoot + result.identifier).text('more...');
						$('<div class="autocomplete-description">').text(result.description.substring(0, 80) + '... ')
							.append($morelink)
							.appendTo($auto);
					}
					++visibleResults;
				}
				
				$('<div class="ui-widget-header">').text('Businesses').appendTo($autocompleteResults);
				for(var i = 0, length = response.business.length; i < length; i++) {
					makeAutocompleteResult(response.business[i], navbar.search.urlBusiness);
				}
				
				var $deepSearch = $('<div style="font-weight: bolder; padding-left: 2px;">').appendTo($autocompleteResults);
				$('<a>').attr('href', navbar.search.url + term).text('See all results...').appendTo($deepSearch);
				
				if(visibleResults) {
					$autocompleteResults.show();
				} else {
					$autocompleteResults.hide();
				}
				break;
			default:
				debug('Error' + JSON.stringify(response));
			}
		});
	}
	
	
	//all .loadhere links while navbar is active
	//hide popups
	$(document).click(function(){
		$userMenuTopBorder.hide();
		$userMenu.hide();
		$autocompleteResults.hide();
		$btnShowUsermenu.removeClass('active');
	});
	
	$(document).on('a.loadhere', 'click', function(){
		$('#body').load($(this).attr('href') + '?loadhere=true');
		return false;
	});
});
</script>

<spring:url var="jsApplication" value="/resources/javascript/application.js" />
<script type="text/javascript" src="${jsApplication }"></script>