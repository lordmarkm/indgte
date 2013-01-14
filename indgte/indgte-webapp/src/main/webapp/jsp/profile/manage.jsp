<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@include file="../tiles/links.jsp"%>

<title>${user.username } InDumaguete</title>
<link rel="stylesheet" href="<spring:url value='/resources/css/review.css' />" />
<link rel="stylesheet" href="<spring:url value='/resources/css/userprofile/userprofile.css' />" />

<div class="grid_8 maingrid">

	<div class="page-header"><spring:message code="manage.header" /> (${user.username })</div>

	<section class="manage-appearance">
		<div class="section-header"><spring:message code="generics.appearance" /></div>
		<table class="appearance-table">
			<tr>
				<td>
					<label for="sel-theme-maingrid"><spring:message code="generics.theme" /></label>
				</td>
				<td>
					<select class="sel-theme" id="sel-theme-maingrid">
						<c:forEach items="${themes }" var="theme">
							<option value="${theme }">${theme.name }</option>
						</c:forEach>
					</select>
				</td> 
			</tr>
			<tr>		
				<td>
					<label for="sel-bgs-maingrid"><spring:message code="generics.background" /></label> 
				</td>
				<td>
					<select class="sel-bgs" id="sel-bgs-maingrid">
						<c:forEach items="${backgrounds }" var="bg">
							<option value="${bg.filename }">${bg.name }</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sel-lang"><spring:message code="generics.language" /></label>
				</td>
				<td>
					<select id="sel-lang">
						<c:forEach items="${languages }" var="lang">
							<option value="${lang.locale }">${lang.label }</option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>
	</section>
	
	<section class="reputation">
		<div class="section-header">Reputation and Resources</div>
		<table class="reputation-table">
			<tr>
				<td>Karma</td> <td>${user.rank.totalFame} (Post: ${user.rank.postFame }, Review: ${user.rank.reviewFame }, Friendship: ${user.rank.friendshipFame }, Page: ${user.rank.entityFame })</td>
			</tr>
			<tr>
				<td>Prefix</td> <td>${user.rank.prefix }</td>
			</tr>
			<tr>
				<td>Title</td> <td>${user.rank.rank }</td>
			</tr>
			<tr>
				<td>Coconuts</td> <td>${user.billingInfo.coconuts }</td>
			</tr>
		</table>
	</section>
	
	<c:if test="${not empty businessSubscriptions && fn:length(businessSubscriptions) > 0 }">
		<section class="subscriptions">
			<div class="section-header">Business Subscriptions</div>
			<table class="subscription-table">
				<c:forEach items="${businessSubscriptions }" var="bSub">
					<tr>
						<td><a class="fatlink dgte-previewlink" previewtype="business" href="${urlProfile}${bSub.domain}"><img class="preview-image" src="${bSub.profilepic != null ? bSub.profilepic.smallSquare : noimage50 }" /></a></td>
						<td>
							<a class="fatlink dgte-previewlink" previewtype="business" href="${urlProfile}${bSub.domain}">${bSub.fullName }</a>
							<div class="subtitle mh40">${fn:substring(bSub.description,0,140) }<c:if test="${fn:length(bSub.description) > 80 }">...</c:if></div>
						</td>
						<td>
							<button class="btn-unsubscribe" subsType="business" subsId="${bSub.id }">Unsubscribe</button>
						</td>
					</tr>
				</c:forEach>
			</table>
		</section>
	</c:if>
	
	<c:if test="${not empty userSubscriptions && fn:length(userSubscriptions) > 0 }">
		<section class="subscriptions">
			<div class="section-header">User Subscriptions</div>
			<table class="subscription-table">
				<c:forEach items="${userSubscriptions }" var="uSub">
					<tr>
						<td><a class="fatlink dgte-previewlink" previewtype="user" href="${urlUserProfile}${uSub.username}"><img class="preview-image" src="${uSub.imageUrl }" /></a></td>
						<td>
							<a class="fatlink dgte-previewlink" previewtype="user" href="${urlUserProfile}${uSub.username}">${uSub.username }</a>
							<div class="subtitle">${uSub.rank }</div>
						</td>
						<td>
							<button class="btn-unsubscribe" subsType="user" subsId="${uSub.id }">Unsubscribe</button>
						</td>
					</tr>
				</c:forEach>
			</table>
		</section>
	</c:if>
</div>
<!-- grid_9 -->

<script src="<spring:url value='/resources/javascript/userprofile/manage.js' />" ></script>
<script>
window.manage = {
	locale : '${user.appearanceSettings.locale}',
	urlChangeLocale : '<spring:url value="/p/manage/lang/" />'
}
</script>

<style>
section:not(.manage-appearance) {
	margin-top: 20px;
}

.appearance-table td:first-child,
.reputation-table td:first-child {
	min-width: 100px;
}
</style>

<%@include file="../grids/notifications4.jsp"  %>