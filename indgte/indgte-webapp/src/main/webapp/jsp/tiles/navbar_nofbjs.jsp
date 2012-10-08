<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="urlLogout" value="/j_spring_security_logout" />
<spring:url var="urlHelp" value="/etc/help/" />

<section class="navbar" class="grid_12">
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
			<div class="user-menu-item">Account Settings</div>
			<div class="user-menu-divider">&nbsp;</div>
			<div class="user-menu-item"><a href="${urlLogout }">Logout</a></div>
			<div class="user-menu-item"><a class="loadhere" href="${urlHelp }">Help</a></div>
		</div>
	</div>
	
	<div class="navigation-container floatright">
		<strong class="navigation profile">Profile</strong>
		<strong class="navigation businesses">My Businesses</strong>
		<strong class="navigation feed">Feed</strong>
		<strong class="navigation chat">Chat</strong>
	</div>
</section>

<style>
.navbar {
	height: 35px;
	background-color: grey;
	font-size: 0.9em;
	color:lavenderblush;
}

.user-container {
	height: 100%;
	display: inline-block;
}
.user-image {
	max-width: 20px;
	max-height: 20px;
	vertical-align: bottom;
	margin: 7px 0 0 10px;
}
.user-username {
	display: inline-block;
	vertical-align: middle;
	width: 120px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.menu-container {
	vertical-align: bottom;
	display: inline-block;
	height: 80%;
	width: 16px;
	padding: 1px;
	font-size: 0.8em;
}
.menu-container:hover {
	background-color: lightgrey;
}
.menu-container.active {
	background-color: lightgrey;
	padding: 0;
	border-right: 1px solid white;
	border-top: 1px solid white;
	border-left: 1px solid white;
}
.triangle-container {
	cursor: pointer;
}
.ui-icon-triangle-1-s {
	margin-top: 3px;
}
.user-menu-partialborder {
	width: 144px;
	display: none;
	margin-left: -145px;
	margin-top: -1px;
	border-top: 1px solid white;
}
.user-menu {
	background-color: lightgrey;
	width: 160px;
	margin-left: -145px;
	display: none;
	color: black;
	padding: 10px 0;
	border-left: 1px solid white;
	border-right: 1px solid white;
	border-bottom: 1px solid white;
	z-index: 1;
}

.user-menu-item {
	padding-left: 10px;
	cursor: pointer;
}
.user-menu-item:hover {
	background-color: gainsboro;
}
.user-menu-item > a {
	color: black;
	text-decoration: none;
}

.user-menu-divider {
    background: none repeat scroll 0 0 grey;
    font-size: 0;
    height: 1px;
    line-height: 0;
    margin: 6px 20px;
}

.navigation-container {
	height: 100%;
}
.navigation {
	display: inline-block;
	padding: 7px 10px 5px 10px;
	cursor: pointer;
	height: 23px;
	font-size: 0.9em;
	color:white;
	vertical-align: middle;
}
.navigation:hover {
	background-color: lightgrey;
}


</style>

<script>
$(function(){
	var $btnShowUsermenu = $('.menu-container'),
		$userMenuTopBorder = $('.user-menu-partialborder');
		$userMenu = $('.user-menu')
		$userMenuItem = $('.user-menu-item');
	
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
	
	$('a.loadhere').click(function(){
		$('#body').load($(this).attr('href') + '?loadhere=true');
		return false;
	});
	$userMenuItem.click(function(){
		var $that = $(this);
		var $a = $that.find('a:first-child');
		
		if($a.hasClass('loadhere')) {
			$('#body').load($a.attr('href') + '?loadhere=true');
		} else {
			window.location.href = $that.find('a:first-child').attr('href');	
		}
	});
});
</script>