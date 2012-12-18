<!-- Notifications -->
<div class="dgte-preview"></div>

<div class="notifications-container grid_4 sidebar-section">
	<div class="sidebar-container">
		<div class="notifications-container relative">
			<img src="${logo }" />
			<span class="msg-uptodate">You're completely up to date. Yey!</span>
			<ul class="notifications hasnotifs"></ul>
		</div>
		<div class="old-notifications-container hide relative">
			<div class="sidebar-section-header">Previous notifications</div>
			<span class="msg-clearhistory hide">You're notification history is empty. Yey!</span>
			<ul class="old-notifications hasnotifs"></ul>
		</div>
		<a class="link-showoldnotifs" href="javascript:;">Show old notifications...</a>
		<a class="link-clearoldnotifs hide" href="javascript:;">Clear all</a>
	</div>
</div>
<link rel="stylesheet" href="<spring:url value='/resources/css/grids/notifs.css' />" />
<!-- Notifications -->