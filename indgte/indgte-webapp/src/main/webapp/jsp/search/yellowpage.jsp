<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../tiles/links.jsp" %>

<title>Dumaguete &mdash; ${group.name }</title>
<script type="text/javascript" src="${jsColumnize }"></script>
<link rel="stylesheet" href="<c:url value='/resources/css/yellowpages/yellowpage.css' />" />
<link rel="stylesheet" href="<spring:url value='/resources/css/feed.css' />" />
<link rel="stylesheet" href="<spring:url value='/resources/css/lists.css' />" />

<div class="grid_8 ui-corner-all maingrid">
	<section class="yellowpages-content">
		<div class="page-header capitalize">${group.name }</div>
		<a href="<spring:url value='/i/toptens/businessgroup/${group.id }' />">View Ranking</a>
		
		<div class="businesses">
			<c:forEach items="${businesses }" var="business" varStatus="i">
			<div class="business">
				<div class="name" >
					<strong>
						<a class="dgte-previewlink fatlink" previewtype="business" href="${urlProfile }${business.domain}">${business.name }</a>
					</strong>
				</div>
				
				<c:if test="${not empty business.address }">
				<div class="business-details"><strong>Address:</strong> ${business.address }</div>
				</c:if>
				
				<c:if test="${not empty business.landline }">
				<div class="business-details"><strong>Landline:</strong> ${business.landline }</div>
				</c:if>
				
				<c:if test="${not empty business.cellphone }">
				<div class="business-details"><strong>Cellphone:</strong> ${business.cellphone }</div>
				</c:if>
				
				<c:if test="${not empty business.email }">
				<div class="business-details"><strong>Email:</strong> ${business.email }</div>
				</c:if>
			</div>
			<c:if test="${(i.index+1)%3 == 0 && i.index != 0 }">
				<div class="clear"></div>
			</c:if>
			</c:forEach>
		</div>
	</section>
	
	<section class="feedcontainer mt20">
		<div class="section-header">Recent posts by <span class="capitalize">${group.name }</span></div>
		<div class="posts-container relative">
			<ul class="posts"></ul>
		</div>
		<div class="loadmoreContainer" style="text-align: center; height: 100px; position: relative;">
			<button class="loadmore" style="width: 50%; margin-top: 50px;">Load 10 more</button>
		</div>
	</section>
</div>

<!-- Notifications -->
<%@include file="../grids/notifications4.jsp"  %>

<script>
window.group = {
	id: '${group.id}'
}

window.urls = {
	urlPost: '${baseURL}${urlPosts}',
	groupPosts: '<spring:url value="/i/groupposts/json" />',
	//copied from businessprofile for feed
	getProducts : '<spring:url value="/b/products/" />',
	product : '<spring:url value="/b/products/" />',
	status : '<spring:url value="/i/newstatus.json" />',
	subposts : '<spring:url value="/i/subposts.json" />',
	postdetails : '<spring:url value="/i/posts/" />',
	linkpreview : '<spring:url value="/i/linkpreview/" />',
	user : '<spring:url value="/p/user/" />',
	business : '<spring:url value="/" />',
	category: '<spring:url value="/b/categories/" />',
	categoryWithProducts: '<spring:url value="/b/categories/" />',
	getproducts: '<spring:url value="/b/products/" />',
	productwithpics: '<spring:url value="/b/products/withpics/" />',
	imgur : 'http://i.imgur.com/',
	imgurPage : 'http://imgur.com/'
}
</script>
<script src="<spring:url value='/resources/javascript/yellowpages/yellowpage.js' />"></script>