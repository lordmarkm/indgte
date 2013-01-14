<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@include file="../tiles/links.jsp" %>

<link rel="stylesheet" href="<spring:url value='/resources/css/review/review.css' />" />
<script src="<spring:url value='/resources/javascript/review/review.js' />" ></script>

<title>Review for ${review.revieweeSummary.title }</title>

<div class="review grid_8 maingrid">
	<div class="page-header">Review for ${review.revieweeSummary.title }</div>
	<section class="reviewee-summary">
		<c:choose>
		<c:when test="${review.reviewType eq 'business' && not empty review.revieweeSummary.thumbnailHash}">
			<img class="reviewee-img floatleft" src="${urlImgur }${review.revieweeSummary.thumbnailHash }s.jpg" />
		</c:when>
		<c:when test="${review.reviewType eq 'user' && not empty review.revieweeSummary.thumbnailHash}">
			<img class="reviewee-img floatleft" src="${review.revieweeSummary.thumbnailHash }" />
		</c:when>
		<c:otherwise>
			<img class="reviewee-img floatleft" src="${noimage50 }" />
		</c:otherwise>
		</c:choose>
		<div class="reviewee-info">
			<div><strong>${review.revieweeSummary.title }</strong></div>
			<div>${review.revieweeSummary.description }</div>
		</div>
	</section>
	
	<div class="clear"></div>
	
	<section class="review-proper">
		<div class="section-header">
			The Review (
				<span class="greentext"><span class="agreeCount">${review.agreeCount }</span> agree</span>, 
				<span class="redtext"><span class="disagreeCount">${review.disagreeCount }</span> disagree</span>
			)
		</div>
		<div class="review-text">${review.justification }</div>
	</section>
	
	<section class="reviewer-summary">
		<div class="reviewer-container">
			<div class="reviewer-img-container">
				<img class="reviewer-img" src="${review.reviewerSummary.thumbnailHash}" />
			</div>
			<div class="reviewer-info">
				<div>${review.reviewerSummary.title }</div>
				<div>${review.reviewerSummary.rank }</div>
			</div>
		</div>
	</section>	
	
	<div class="clear"></div>
	
	<c:if test="${review.reviewerSummary.identifier != user.username }">
	<section>
		<div class="section-header">React</div>
		<div class="react-agree-disagree hide"></div>
		<div class="react">
			<div>How do you feel about this review?</div>
			<sec:authorize access="hasRole('ROLE_USER')">
				<div class="centercontent mt10">
					<button class="btn-react agree">I agree with this review</button>
					<button class="btn-react disagree">I disagree with this review</button>
				</div>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_ANONYMOUS')">
				<div class="mt10"><spring:message code="anon.review.react" /></div>
			</sec:authorize>
		</div>
	</section>
	</c:if>
	
	<section>
		<div class="section-header">Comment</div>
		<div class="fb-comments" data-href="${domain}${urlReview }${review.reviewType }/${review.id}" data-width="600"></div>
	</section>
</div>

<script>
window.review = {
	id: '${review.id}',
	type: '${review.reviewType}',
	revieweeIdentifier: '${review.revieweeSummary.identifier}',
	agree: '${agree}' === 'true',
	disagree: '${disagree}' === 'true',
	agreeCount: '${review.agreeCount}',
	disagreeCount: '${review.disagreeCount}'
}

window.urls = {
	deleteReview: '<spring:url value="/i/deletereview/" />',
	user: '${urlUserProfile}',
	business: '${urlProfile}',
	reviewAgree: '<spring:url value="/i/reviewreact/${review.reviewType}/agree/${review.id}.json" />',
	reviewDisagree: '<spring:url value="/i/reviewreact/${review.reviewType}/disagree/${review.id}.json" />'
}
</script>

<!-- Review menu -->
<sec:authorize access="hasRole('ROLE_USER')">
<c:if test="${review.reviewer.username eq user.username }">
<div class="review-menu sidebar-section grid_4">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Review Menu</div>
		<button class="btn-delete-review">Delete review</button>
	</div>
</div>
</c:if>
</sec:authorize>

<sec:authorize access="hasRole('ROLE_MODERATOR')">
<div class="review-menu sidebar-section grid_4">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Moderator Actions</div>
		<div class="ui-state-highlight mb5 pd2"><spring:message code="mod.warning" /></div>
		<button class="btn-delete-review">Delete review</button>
	</div>
</div>
</sec:authorize>

<!-- Notifications -->
<%@include file="../grids/notifications4.jsp"  %>

<sec:authorize access="hasRole('ROLE_USER')">
<!-- Reviews -->
<%@include file="../grids/reviewqueue.jsp" %>
</sec:authorize>

<!-- Sidebar Featured promos -->
<%@include file="../grids/sidebarpromos.jsp" %>