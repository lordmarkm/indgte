<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@include file="../tiles/links.jsp" %>

<title>Dumaguete</title>
<link rel="stylesheet" href="<spring:url value='/resources/css/buyandsell.css' />" />
<script src="${jsValidator}"></script>

<div class="grid_8 maingrid">
	<div class="page-header">
		<div class="redtext">Item not found</div>
	</div>
	<div class="subtitle">It was probably deleted</div>
</div>

<sec:authorize access="hasRole('ROLE_USER')">
<!-- Watched Tags -->
<div class="watched-tags-container sidebar-section grid_4">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Watched tags</div>
		<c:choose>
		<c:when test="${not empty user.watchedTags }">
		<div class="watched-tag-link-container">
			<a class="watched-tag selected" href="javascript:;" tag="all">All</a>
		<c:forEach items="${user.watchedTags }" var="watchedTag">
			<a class="watched-tag" href="${urlTag }${watchedTag.tag }" tag="${watchedTag.tag }">${watchedTag.tag }</a>
		</c:forEach>
		</div>
		</c:when>
		<c:otherwise>
			<spring:url var="urlFaqWatchingTags" value="/h/buy-and-sell#watching-tags" />
			<p class="no-watched-tags"><spring:message code="watchedtags.empty" arguments="${urlFaqWatchingTags }"/></p>
		</c:otherwise>
		</c:choose>
		<div class="watched-tag-items-container">
			<ul class="watched-tag-items"></ul>
		</div>
		<div class="sidebar-divider"></div>
	</div>
</div>
<c:if test="${not empty user.watchedTags }">
<script src="${jsWatchedTags }" /></script>
<link rel="stylesheet" href="<c:url value='/resources/css/grids/watchedtags.css' />" />
</c:if>
<!-- End watched tags -->

<!-- Tagcloud -->
<div class="tagcloud-container sidebar-section grid_4">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Popular tags</div>
		<div class="tagcloud"></div>
	</div>
</div>
<script>
window.urls.tagweights = '<spring:url value="/s/tags.json" />';
</script>
<script src="${jsTagcloud }"></script>
<script src="${jsDgteTagCloud }"></script>
<link rel="stylesheet" href="<spring:url value='/resources/css/grids/tagcloud.css' />" />
<!-- End tagcloud -->
</sec:authorize>

<!-- Notifications -->
<%@include file="../grids/notifications4.jsp"  %>