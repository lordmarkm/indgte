<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!-- Notifications -->
<div class="dgte-preview"></div>

<div class="notifications-container grid_4 sidebar-section">
	<div class="sidebar-container">
		<div class="notifications-container relative">
			<img src="${logo }" />
			<sec:authorize access="hasRole('ROLE_USER')">
				<span class="msg-uptodate">You're completely up to date. Yey!</span>
				<ul class="notifications hasnotifs"></ul>
			</sec:authorize>
		</div>
		<sec:authorize access="hasRole('ROLE_USER')">
			<div class="old-notifications-container hide relative">
				<div class="sidebar-section-header">Previous notifications</div>
				<span class="msg-clearhistory hide">You're notification history is empty. Yey!</span>
				<ul class="old-notifications hasnotifs"></ul>
			</div>
			<a class="link-showoldnotifs" href="javascript:;">Show old notifications...</a>
			<a class="link-clearoldnotifs hide" href="javascript:;">Clear all</a>
		</sec:authorize>
	</div>
</div>
<link rel="stylesheet" href="<spring:url value='/resources/css/grids/notifs.css' />" />
<!-- Notifications -->