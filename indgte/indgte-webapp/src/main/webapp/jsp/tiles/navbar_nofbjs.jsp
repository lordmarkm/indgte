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
	
	<div class="navigation-container floatright">
		<strong class="navigation home"><a href="${urlHome }">Home</a></strong>
		<strong class="navigation feed">Feed</strong>
		<strong class="navigation chat">Chat</strong>
	</div>
</section>

<script>
$(function(){
	var $btnShowUsermenu = $('.menu-container'),
		$userMenuTopBorder = $('.user-menu-partialborder');
		$userMenu = $('.user-menu')
		$userMenuItem = $('.user-menu-item'),
		$navigation = $('.navigation');
	
	//show user-menu
	$btnShowUsermenu.click(function(event){
		var $that = $(this);
		event.stopPropagation();
		$that.toggleClass('active');
		$userMenuTopBorder.toggle();
		$userMenu.toggle();
	});
	//hide user-menu
	$(document).click(function(){
		$userMenuTopBorder.hide();
		$userMenu.hide();
		$btnShowUsermenu.removeClass('active');
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
	
	//all .loadhere links while navbar is active
	$(document).on('a.loadhere', 'click', function(){
		$('#body').load($(this).attr('href') + '?loadhere=true');
		return false;
	});
});
</script>