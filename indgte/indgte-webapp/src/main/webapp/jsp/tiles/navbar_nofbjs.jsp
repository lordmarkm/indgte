<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@include file="../tiles/links.jsp" %>

<spring:url var="urlHome" value="/" />
<spring:url var="urlYellowPages" value="/s/" />
<spring:url var="urlProfile" value="/p/" />
<spring:url var="urlUserProfile" value="/p/user/" />
<spring:url var="urlBuySell" value="/t/" />
<spring:url var="urlMyBusinesses" value="/p/businesses" />
<spring:url var="urlHelp" value="/etc/help/" />
<spring:url var="urlLogout" value="/j_spring_security_logout" />
<spring:url var="cssNavbar" value="/resources/css/navbar/navbar.css" />

<link rel="stylesheet" href="${cssNavbar }" />

<section class="grid_12 navbar ui-widget-header">
	<div class="user-container">
		<a href="${urlUserProfile}${user.username}"><img class="user-image" src="${user.imageUrl }" /></a>
		<strong class="user-username"><a href="${urlUserProfile}${user.username}">${user.username }</a></strong>
	</div>
	
	<div class="menu-container">
		<div class="triangle-container" style="display:inline-block; height: 100%;">
			<span class="ui-icon ui-icon-triangle-1-s"></span>
		</div>
		<div class="user-menu-partialborder"></div>
		<div class="user-menu">
			<div class="user-menu-item"><a href="${urlProfile }">View Profile</a></div>
			<div class="user-menu-item"><a href="${urlMyBusinesses }">View My Businesses</a></div>
			<div class="user-menu-divider">&nbsp;</div>
			<div class="user-menu-item">Account Settings</div>
			<div class="user-menu-divider">&nbsp;</div>
			<div class="user-menu-item"><a href="${urlLogout }">Logout</a></div>
			<div class="user-menu-item"><a href="${urlHelp }">Help</a></div>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
			<div class="user-menu-divider">&nbsp;</div>
			<div class="user-menu-item"><a href="<spring:url value='/a/daotest/'/>">Dao Tester</a></div>
			</sec:authorize>
		</div>
	</div>
	
	<div class="search-padder">
		<div class="search-container">
			<input class="search-input" type="text" placeholder="Search for stuff in Dumaguete"/>
			<button>&nbsp;</button>
			<div class="autocomplete-results ui-state-highlight" style="display: none;"></div>
		</div>
	</div>
	
	<div class="navigation-container floatright">
		<strong class="navigation home"><a href="${urlHome }">Home</a></strong>
		<strong class="navigation yellowpages"><a href="${urlYellowPages }">Yellow Pages</a></strong>
		<strong class="navigation buysell"><a href="${urlBuySell }">Buy&Sell</a></strong>
		<strong class="navigation chat">Chat</strong>
	</div>
</section>

<script>
window.navbar = {
	search : {
		minlength : 3, //god's number trololololol
		maxDisplay : 5, //god's number plus two!!!
		url : '<spring:url value="/s/" />',
		urlBusiness: '<spring:url value="/p/" />',
		urlCategory: '<spring:url value="/b/categories/" />',
		urlProduct: '<spring:url value="/b/products/" />',
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
	
	$btnSearch.button({ icons: {primary: 'ui-icon-search'}}).click(function(){
	});
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
		$.get(navbar.search.url + term + '/json', function(response){
			switch(response.status) {
			case '200':
				$autocompleteResults.html('');
				
				debug(response);
				var visibleResults = 0;
				
				function makeAutocompleteResult(result, urlRoot) {
					var $auto = $('<div class="autocomplete-container">').appendTo($autocompleteResults);
					$('<img class="autocomplete-img">').attr('src', result.thumbnailHash ? navbar.search.urlImgur + result.thumbnailHash + 's.jpg' : dgte.urls.blackSquareSmall).appendTo($auto);
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
				
				if(response.business && response.business.length > 0) {
					$('<div class="ui-widget-header">').text('Businesses').appendTo($autocompleteResults);
					for(var i = 0, length = response.business.length; i < length; i++) {
						makeAutocompleteResult(response.business[i], navbar.search.urlBusiness);
					}
				}
				
				if(response.category && response.category.length > 0) {
					$('<div class="ui-widget-header">').text('Categories').appendTo($autocompleteResults);
					for(var i = 0, length = response.category.length; i < length; i++) {
						makeAutocompleteResult(response.category[i], navbar.search.urlCategory);
					}
				}
				
				if(response.product && response.product.length > 0) {
					$('<div class="ui-widget-header">').text('Products').appendTo($autocompleteResults);
					for(var i = 0, length = response.product.length; i < length; i++) {
						makeAutocompleteResult(response.product[i], navbar.search.urlProduct);
					}
				}
				
				var $deepSearch = $('<div class="autocomplete-allresults">').appendTo($autocompleteResults);
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
	
	$autocompleteResults.on(
		{
			mouseover: function(){$(this).addClass('ui-state-active')}, 
			mouseout:  function(){$(this).removeClass('ui-state-active')}
		},
		'.autocomplete-container');
	
	$(document).on({
		mouseenter : function(){
			var $this = $(this);
			if($this.hasClass('ui-state-active')) return;
			$this.addClass('ui-state-highlight');
		},
		mouseleave : function(){
			$(this).removeClass('ui-state-highlight');
		},
		click : function(event){
			event.stopPropagation();
			$userMenuTopBorder.toggle();
			$userMenu.toggle();
			$(this).removeClass('ui-state-highlight').addClass('ui-state-active');
			$userMenu.addClass('ui-state-active');
			$userMenuTopBorder.addClass('ui-state-active');
		}
	}, '.menu-container');
	
	$(document).on({
		mouseenter : function(){
			$(this).addClass('ui-state-highlight');
		},
		mouseleave : function(){
			$(this).removeClass('ui-state-highlight');
		},
		click : function(){
			window.location.href = $(this).find('a:only-child').attr('href');	
		}
	}, '.user-menu-item');
	
	$(document).on({
		mouseenter : function(){
			$(this).addClass('ui-state-highlight');
		},
		mouseleave : function(){
			$(this).removeClass('ui-state-highlight');
		},
		click: function(event){
			event.stopPropagation();
			$(this).addClass('ui-state-active').removeClass('ui-state-highlight');
			window.location.href = $(this).find('a:only-child').attr('href');
		}
	}, '.navigation')
	
	//hide popups
	$(document).click(function(){
		$btnShowUsermenu.removeClass('ui-state-active');
		
		$userMenuTopBorder.hide();
		$userMenu.hide();
		$autocompleteResults.hide();
	});
});
</script>

<script type="text/javascript" src="${jsApplication }"></script>
<%@include file="/resources/javascript/navbar/chat.jsp"  %>
