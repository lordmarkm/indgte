<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../tiles/links.jsp" %>

<title>${post.title }</title>
<link rel="stylesheet" href="<spring:url value='/resources/css/lists.css' />" />
<link rel="stylesheet" href="<spring:url value='/resources/css/feed.css' />" />
<script src="${jsValidator}"></script>

<div class="grid_8 maingrid">

	<div class="post">
		<div class="post-pic-container">
			<c:if test="${post.type eq 'business' }">
				<img class="post-pic" src="${urlImgRoot }${post.posterImgurHash}s.jpg" />
			</c:if>
			<c:if test="${post.type eq 'user' }">
				<img class="post-pic" src="${post.posterImgurHash}" />
			</c:if>
		</div>
				
		<div class="post-data-container">
			<strong class="post-title">${post.title }</strong>
			<div class="post-text">${post.text }</div>
		</div>
		
		<div class="post-fb">
			<div class="fb-like" data-send="true" data-width="450" data-show-faces="true"></div>
			<div class="fb-comments" data-href="${domain}${urlPosts }${post.id}" data-width="540"></div>
		</div>
	</div>

</div>

<c:if test="${owner }">
	<div class="grid_4 sidebar-section owner-menu">
		<div class="sidebar-container">	
			<div class="sidebar-section-header">Post Actions</div>
			
			<jsp:useBean id="now" class="java.util.Date" />
			<fmt:formatDate value="${now }" pattern="yyyy-MM-dd" var="formattedNow" /> 
			<c:choose>
				<c:when test="${not empty post.featureEnd && post.featureEnd >= formattedNow }" >
					<div class="ui-state-highlight pd5">This post is featured from <strong>${post.featureStart }</strong> to <strong>${post.featureEnd }</strong></div>
				</c:when>
				<c:otherwise>
					<button class="btn-promote">Promote this post</button>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<div class="dialog-promote hide" title="Promote this post">
		<span><spring:message code="post.promote.dialog" arguments="${user.billingInfo.coconuts }" /></span>
		<form class="form-promote" method="post" action="<c:url value='/o/promotepost/${post.id }' />" >
			<table>
				<tr>
					<td><label for="startDate">Promote from</label></td>
					<td><input type="date" id="start-date" name="startDate" readonly="readonly" placeholder="Click to choose" /></td>
				</tr>
				<tr>
					<td><label for="endDate">Promote until</label></td>
					<td><input type="date" id="end-date" name="endDate" readonly="readonly" placeholder="Click to choose"/></td>
				</tr>
			</table>
		</form>
		<span class="coconut-cost"><spring:message code="post.promote.dialog.comp" /></span>
	</div>
</c:if>

<!-- Notifications -->
<%@include file="../grids/notifications4.jsp"  %>
<!-- Notifications -->

<style>
.owner-menu button {
	width: 100%
}
</style>

<script>
window.user = {
	username : '${user.username}',
	coconuts : '${user.billingInfo.coconuts}'
}

window.post = {
	id : '${post.id}',
	type : '${post.type}',
	attachmentType : '${post.attachmentType}',
	attachmentImgurHash : '${post.attachmentImgurHash}',
	attachmentTitle : '<c:out value="${post.attachmentTitle}" />',
	attachmentIdentifier : '<c:out value="${post.attachmentIdentifier}" />',
	attachmentDescription : '<c:out value="${post.attachmentDescription}" />',
	postTime : '${post.postTime.time }',
	posterIdentifier : '${post.posterIdentifier}',
	posterTitle: '${post.posterTitle }'
}

window.urls = {
	user : '<spring:url value="/p/user/" />',
	business : '<spring:url value="/" />',
	imgur : 'http://i.imgur.com/',
	imgurPage : 'http://imgur.com/',
	categoryWithProducts: '<spring:url value="/b/categories/" />',
	productwithpics: '<spring:url value="/b/products/withpics/" />',
	commentNotify: '<spring:url value="/i/commentnotify/post/" />',
	commentRemove: '<spring:url value="/i/commentremove/post/" />',
	likeNotify: '<spring:url value="/i/likenotify/post/" />',
	unlike: '<spring:url value="/i/unlike/post/" />'
}
</script>
<script type="text/javascript" src="<c:url value='/resources/javascript/posts/post.js' />" ></script>