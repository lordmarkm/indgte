<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/jquery-ui.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/moment.js/1.7.0/moment.min.js"></script>

<!-- 
    base: Google CDN, Microsoft CDN
    black-tie: Google CDN, Microsoft CDN
    blitzer: Google CDN, Microsoft CDN
    cupertino: Google CDN, Microsoft CDN
    dark-hive: Google CDN, Microsoft CDN
    dot-luv: Google CDN, Microsoft CDN
    eggplant: Google CDN, Microsoft CDN
    excite-bike: Google CDN, Microsoft CDN
    flick: Google CDN, Microsoft CDN
    hot-sneaks: Google CDN, Microsoft CDN
    humanity: Google CDN, Microsoft CDN
    le-frog: Google CDN, Microsoft CDN
    mint-choc: Google CDN, Microsoft CDN
    overcast: Google CDN, Microsoft CDN
    pepper-grinder: Google CDN, Microsoft CDN
    redmond: Google CDN, Microsoft CDN
    smoothness: Google CDN, Microsoft CDN
    south-street: Google CDN, Microsoft CDN
    start: Google CDN, Microsoft CDN
    sunny: Google CDN, Microsoft CDN
    swanky-purse: Google CDN, Microsoft CDN
    trontastic: Google CDN, Microsoft CDN
    ui-darkness: Google CDN, Microsoft CDN
    ui-lightness: Google CDN, Microsoft CDN
    vader: Google CDN, Microsoft CDN
 -->

<c:choose>
	<c:when test="${not empty user.appearanceSettings.theme }">
		<c:set var="theme" value="${user.appearanceSettings.theme.name }" />
	</c:when>
	<c:otherwise>
		<c:set var="theme" value="flick" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${not empty user.appearanceSettings.background }">
		<c:set var="background" value="${user.appearanceSettings.background.filename }	" />
	</c:when>
	<c:otherwise>
		<c:set var="background" value="grass" />
	</c:otherwise>
</c:choose>

<link rel="stylesheet" href="<c:url value='/resources/css/backgrounds/' />${background}.css" />
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/themes/${theme }/jquery-ui.css" type="text/css" media="all" />
<!-- 
<link rel="stylesheet" href="//cachedcommons.org/cache/960/0.0.0/stylesheets/960-min.css" type="text/css" media="all" />
 -->
<link rel="stylesheet" href="<spring:url value='/resources/css/960/960_12_col.css' />" />
<link rel="stylesheet" href="<spring:url value='/resources/css/application.css' />" />