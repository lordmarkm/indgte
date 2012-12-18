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
</div>
<!-- grid_9 -->

<style>
section:not(.manage-appearance) {
	margin-top: 20px;
}

.appearance-table td:first-child,
.reputation-table td:first-child {
	min-width: 100px;
}
</style>

<script>
window.manage = {
	locale : '${user.appearanceSettings.locale}',
	urlChangeLocale : '<spring:url value="/p/manage/lang/" />'
}

$(function(){
	$('#sel-lang')
		.val(manage.locale)
		.change(function(){
			window.location.href = manage.urlChangeLocale + $(this).val();
		});
});
</script>

<%@include file="../grids/notifications4.jsp"  %>