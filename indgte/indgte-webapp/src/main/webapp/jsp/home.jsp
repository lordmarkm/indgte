<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@include file="tiles/links.jsp" %>

<spring:url var="paperclip" value="/resources/images/icons/paperclip.png" />

<title>Dumaguete</title>
<link rel="stylesheet" href="<spring:url value='/resources/css/lists.css' />" />
<link rel="stylesheet" href="<spring:url value='/resources/css/feed.css' />" />
<link rel="stylesheet" href="<spring:url value='/resources/css/home.css' />" />
<script src="<spring:url value='/resources/javascript/home/home.js' />" ></script>
<script src="${jsValidator }"></script>

<div class="grid_8 maingrid">

<sec:authorize access="hasRole('ROLE_ANONYMOUS')">
<section class="newpost">
	<div><spring:message code="anon.newpost" /></div>
</section>
</sec:authorize>

<sec:authorize access="hasRole('ROLE_USER')">
<section class="newpost">
	<form id="form-newpost">
		<div class="border-provider ui-state-active inline-block">
			<div class="status-title-container">
				<input name="title" class="status-title ui-state-active hide" maxlength="45" type="text" placeholder="title" />
			</div>
			<textarea name="text" class="status-textarea noattachment" maxlength="1000" rows="1" placeholder="<spring:message code="home.status.textarea" />"></textarea>	
			<div class="attach-input-container">
				<!-- Hidden -->
				<input class="posterId" type="hidden" value="${user.id }" />
				<input class="posterType" type="hidden" value="user" />
				<input class="iptType" type="hidden" value="none"/>
				
				<!-- Image -->
				<input class="iptFile" type="file" />
				
				<!-- Video and link -->
				<input class="iptUrl" type="text" placeholder="Paste URL"/>
				<div class="link-preview-container ui-state-default">
					<div class="link-preview-images"></div>
					<div class="inline-block">
						<div class="link-preview-title"></div>
						<div class="link-preview-description"></div>
						<div class="link-preview-url"></div>
					</div>
				</div>
				
				<!-- Entity -->
				<input class="iptEntity" type="text" placeholder="Enter Entity name" />
				<div class="entity-preview hide"></div> 
				<div class="entity-suggestions ui-state-default"></div>
			</div>
		</div>
		
		<input type="text" name="tags" class="ipt-tags ui-state-active" placeholder="tags 'news buglasan pics-2012' (space-delimited, 3 max)" />
		<div class="newpost-errors"></div>
	</form>
	<div class="status-options hide">
		<div class="floatright">
			<span class="status-counter"></span>
			<div class="post-as">
				<div class="menubutton post-as-visibles" title="Posting as ${user.username }">
					<img src="${user.imageUrl }" />
					<div class="ui-icon ui-icon-triangle-1-s"></div>
				</div>
				<div class="post-as-menu">
					<div class="post-as-option" posterId="${user.id }" posterType="user">
						<img src="${user.imageUrl }"> 
						<div class="name">${user.username }</div>
						<div class="post-as-category">Personal</div>
					</div>
					<c:forEach items="${user.businesses }" var="business">
					<div class="post-as-option" posterId="${business.id }" posterType="business">
						<c:choose>
						<c:when test="${not empty business.profilepic }">
							<img src="${business.profilepic.smallSquare }" />
						</c:when>
						<c:otherwise>
							<img src="http://i.imgur.com/Y0NTes.jpg" />
						</c:otherwise>
						</c:choose>
						<div class="name">${business.fullName }</div>
						<div class="post-as-category">${business.category.name }</div>
					</div>
					</c:forEach>
				</div>
			</div>
			<div class="attach">
				<div class="menubutton attach-visibles" title="Attach a product or image">
					<img src="${paperclip }" style="max-width: 15px; max-height: 15px;" />
					<div class="ui-icon ui-icon-triangle-1-s attach-triangle"></div>
				</div>
				<div class="attach-menu">
					<div class="attach-option image">
						<div class="name"><span class="ui-icon ui-icon-image inline-block mr5"></span>Image</div>
					</div>
					<div class="attach-option video">
						<div class="name"><span class="ui-icon ui-icon-video inline-block mr5"></span>Embeddable media</div>
					</div>
					<div class="attach-option link">
						<div class="name"><span class="ui-icon ui-icon-link inline-block mr5"></span>Link</div>
					</div>
					<div class="attach-option entity">
						<div class="name"><span class="ui-icon ui-icon-cart inline-block mr5"></span> Indgte Entity</div>
					</div>
					<div class="attach-option none">
						<div class="name"><span class="ui-icon ui-icon-cancel inline-block mr5"></span>None</div>
					</div>
				</div>			
			</div>
			<div class="button btn-post">Post</div>
		</div>
		<div class="clear"></div>
	</div>
</section>
</sec:authorize>

<section class="feedcontainer">
	
	<div class="filter mb5">
		<div class="posts-by-tags news" title="Display posts tagged 'news'">news</div>
		<div class="posts-by-tags events" title="Display posts tagged 'events'">events</div>
		<div class="posts-by-tags pics" title="Display posts tagged 'pics'">pics</div>
		<div class="posts-by-tags questions" title="Display posts tagged 'question'">question</div>
	</div>
	
	<div class="alert-container ui-state-highlight hide pd5"></div>
	<div class="posts-container relative">
		<ul class="posts"></ul>
	</div>
	<div class="loadmoreContainer" style="text-align: center; height: 100px; position: relative;">
		<button class="loadmore" style="width: 50%; margin-top: 50px;">Load 10 more</button>
	</div>
</section>
</div>

<div class="dgte-preview"></div>

<script>
window.constants = {
	imgurKey : '${imgurKey}',
	auth : '<sec:authorize access="hasRole('ROLE_USER')">true</sec:authorize>' === 'true'
}
window.urls = {
	status : '<spring:url value="/i/newstatus.json" />',
	subposts : '<spring:url value="/i/subposts/json" />',
	postdetails : '<spring:url value="/i/posts/" />',
	linkpreview : '<spring:url value="/i/linkpreview/" />',
	user : '<spring:url value="/p/user/" />',
	business : '<spring:url value="/" />',
	category: '<spring:url value="/b/categories/" />',
	categoryWithProducts: '<spring:url value="/b/categories/" />',
	getproducts: '<spring:url value="/b/products/" />',
	product: '<spring:url value="/b/products/" />',
	productwithpics: '<spring:url value="/b/products/withpics/" />',
	buyandsellitem: '<spring:url value="/t/" />',
	imgur : 'http://i.imgur.com/',
	imgurPage : 'http://imgur.com/',
	searchOwn: '<spring:url value="/s/own/" />',
	noimage50: '${noimage50	}',
	fbCommentsUrl: '${baseURL}${urlPosts}'
}
</script>

<sec:authorize ifNotGranted="ROLE_USER">
<div class="grid_4 sidebar-section">
	<div class="sidebar-container">
		<img src="${logo }" />
	</div>
</div> 
</sec:authorize>

<sec:authorize access="hasRole('ROLE_USER')">
<div class="grid_4 sidebar-section">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Home</div>
		<div id="rdo-post-sort">
			<input type="radio" name="rdo-sort-order" value="subs" id="rdo-subs" checked="checked"/> <label for="rdo-subs">Subscriptions</label>
			<input type="radio" name="rdo-sort-order" value="newest" id="rdo-newest" /> <label for="rdo-newest">Newest</label>
			<input type="radio" name="rdo-sort-order" value="popularity" id="rdo-popularity" /> <label for="rdo-popularity">Popularity</label>
		</div>
	</div>
</div>

<!-- Notifications -->
<%@include file="./grids/notifications4.jsp"  %>

<!-- Reviews -->
<%@include file="./grids/reviewqueue.jsp" %>
</sec:authorize>

<!-- Sidebar Featured promos -->
<%@include file="./grids/sidebarpromos.jsp" %>

<!-- Top Tens -->
<%@include file="./grids/toptens.jsp" %>