<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@include file="../tiles/links.jsp" %>

<spring:url var="urlHome" value="/" />
<spring:url var="urlYellowPages" value="/s/" />
<spring:url var="urlUserProfile" value="/p/user/" />
<spring:url var="urlBuySell" value="/t/" />
<spring:url var="urlMyBusinesses" value="/p/businesses" />
<spring:url var="urlManageAccount" value="/p/manage/" />
<spring:url var="urlHelp" value="/etc/help/" />
<spring:url var="urlLogout" value="/j_spring_security_logout" />
<spring:url var="cssNavbar" value="/resources/css/navbar/navbar.css" />

<link rel="stylesheet" href="${cssNavbar }" />

<div class="grid_12 navbar ui-widget-header">
	<sec:authorize access="hasRole('ROLE_ANONYMOUS')">
		<a href="${urlLogin }"><img class="user-image" src="${noimage50 }" /></a>
		<strong class="user-username"><a href="${urlLogin }">Anonymous</a></strong>
	</sec:authorize>

	<sec:authorize access="hasRole('ROLE_USER')">
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
			<div class="user-menu-item"><a href="${urlOwnProfile }">View Profile</a></div>
			<div class="user-menu-item"><a href="${urlMyBusinesses }">View My Businesses</a></div>
			<div class="user-menu-divider">&nbsp;</div>
			<div class="user-menu-item"><a href="${urlManageAccount }">Manage Account</a></div>
			<div class="user-menu-divider">&nbsp;</div>
			<div class="user-menu-item"><a href="${urlLogout }">Logout</a></div>
			<div class="user-menu-item"><a href="${urlHelp }">Help</a></div>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
			<div class="user-menu-divider">&nbsp;</div>
			<div class="user-menu-item"><a href="<spring:url value='/a/daotest/'/>">Dao Tester</a></div>
			</sec:authorize>
		</div>
	</div>
	</sec:authorize>
	
	<div class="search-padder">
		<div class="search-container">
			<form id="main-search">
			<input class="search-input" type="text" placeholder="<spring:message code='search.placeholder' />"/>
			<button>&nbsp;</button>
			<div class="autocomplete-results ui-state-highlight" style="display: none;"></div>
			</form>
		</div>
	</div>
	
	<div class="navigation-container floatright">
		<strong class="navigation home"><a href="${urlHome }">Home</a></strong>
		<strong class="navigation yellowpages"><a href="${urlYellowPages }">Yellow Pages</a></strong>
		<strong class="navigation buysell"><a href="${urlBuySell }">Buy&Sell</a></strong>
		<strong class="navigation chat">Chat</strong>
	</div>
</div>

<sec:authorize access="hasRole('ROLE_ANONYMOUS')">
<div class="grid_12 anon-alert-container ui-state-highlight">
	<div class="anon-alert ">
		<div class="anon-alert-icon-container">
			<div class="ui-icon ui-icon-alert anon-alert-icon"></div>
		</div>
		<spring:message code="anon.alert" />
	</div>
</div>
</sec:authorize>

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
</script>
<script type="text/javascript" src="${jsApplication }"></script>
<script type="text/javascript" src="<c:url value='/resources/javascript/navbar/navbar.js' />" ></script>

<sec:authorize access="hasRole('ROLE_USER')">
	<%@include file="/resources/javascript/navbar/chat.jsp"  %>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_ANONYMOUS')">
<script>
$(function(){
	function loginToChat() {
		$('<div>').attr('title', 'Must be logged in to chat')
		.text('Sorry, you must be logged in to chat')
		.dialog({
			buttons: {
				'Login': function(){
					window.location.href = '${urlLogin}';
				},
				'Nevermind': function(){
					$(this).dialog('close');
				}
			}
		});
		return false;
	}
	
	$('.navigation.chat').click(loginToChat);
	window.openChat = loginToChat;
	window.openChatWithUser = loginToChat;
});
</script>
</sec:authorize>