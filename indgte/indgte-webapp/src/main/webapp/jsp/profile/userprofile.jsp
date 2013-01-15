<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@include file="../tiles/links.jsp" %>

<title>${target.user.username } InDumaguete</title>
<script src="${jsValidator}"></script>
<script type="text/javascript" src="<spring:url value="/resources/javascript/feed/feed.js" />"></script>
<link rel="stylesheet" href="<spring:url value='/resources/css/lists.css' />" />
<link rel="stylesheet" href="<spring:url value='/resources/css/review.css' />" />
<link rel="stylesheet" href="<spring:url value='/resources/css/userprofile/userprofile.css' />" />

<div class="grid_9 maingrid">

<section class="userinfo">
	<div class="section-header">${target.user.username }</div>
	<div class="container userinfo-container">
		<img src="${target.user.imageUrl }" />
		<ul>
			<li class="userinfo-username">${target.user.username }</li>
			<li class="userinfo-rank">${target.rank }</li>
			<li class="userinfo-providername"><a href="${target.user.profileUrl}">${target.user.providerId }</a></li>
		</ul>
	</div>
</section>

<c:if test="${not empty target.wishlist }">
<section class="wishlist">
	<div class="section-header">Wishlist</div>
	<div class="container wishlist-container">
		<ol>
			<c:forEach items="${target.wishlist }" var="wish" varStatus="status">
				<li class="wish">
					<div class="index">${status.index + 1	}.</div>
					<div style="display: inline-block;">
						<img src="${wish.activeImgur.smallSquare }" />
					</div>
					<div class="wish-info">
						<div class="wish-text">${wish.text }</div>
						<c:if test="${not empty wish.product }">
							<div class="wish-item-description subtitle">
								${fn:substring(wish.product.description, 0, 140) }
								<c:if test="${fn:length(wish.product.description) > 140 }">
								...
								</c:if>
							</div>
						</c:if>
						<c:if test="${not empty wish.buyAndSellItem }">
							<div class="wish-item-description subtitle">
								${wish.buyAndSellItem.description }
								<c:if test="${fn:length(wish.buyAndSellItem.description) > 140 }">
								...
								</c:if>
							</div>
						</c:if>
					</div>
				</li>
			</c:forEach>
		</ol>
	</div>
</section>
</c:if>

<c:if test="${not empty target.businesses }">
<section class="businesses">
	<div class="section-header">Businesses</div>
	<ul>
		<c:forEach items="${target.businesses }" var="b">
			<li class="userprofile-business-summary">
				<div class="userprofile-business-summary-img-container">
					<a href="${urlBusinessProfile }${b.domain}" class="dgte-previewlink fatlink" previewtype="business">
						<c:choose>
							<c:when test="${not empty b.profilepic }">
								<img class="userprofile-business-summary-img" src="${b.profilepic.smallSquare }">
							</c:when>
							<c:otherwise>
								<img class="userprofile-business-summary-img" src="${noimage50 }">
							</c:otherwise>
						</c:choose>
					</a>
				</div>
				<div class="userprofile-business-summary-info">
					<div class="userprofile-business-summary-title bold"><a href="${urlBusinessProfile }${b.domain}" class="dgte-previewlink fatlink" previewtype="business">${b.fullName }</a></div>
					<div class="userprofile-business-summary-description subtitle">
						${fn:substring(b.description, 0, 80) }<c:if test="${fn:length(b.description) > 80 }">...</c:if>
					</div>
				</div>
			</li>
		</c:forEach>
	</ul>
</section>
</c:if>

<c:if test="${not empty target.buyAndSellItems }">
<section class="businesses">
	<div class="section-header">Items for Sale</div>
	<ul>
		<c:forEach items="${target.buyAndSellItems }" var="item">
			<li class="userprofile-business-summary">
				<div class="userprofile-business-summary-img-container">
					<a href="${urlTrade }${item.id}" class="dgte-previewlink fatlink" previewtype="buyandsellitem">
						<c:choose>
							<c:when test="${not empty item.imgur }">
								<img class="userprofile-business-summary-img" src="${item.imgur.smallSquare }">
							</c:when>
							<c:otherwise>
								<img class="userprofile-business-summary-img" src="${noimage50 }">
							</c:otherwise>
						</c:choose>
					</a>
				</div>
				<div class="userprofile-business-summary-info">
					<div class="userprofile-business-summary-title bold"><a href="${urlTrade }${item.id}" class="dgte-previewlink fatlink" previewtype="buyandsellitem">${item.name }</a></div>
					<div class="userprofile-business-summary-description subtitle">
						${fn:substring(item.description, 0, 80) }<c:if test="${fn:length(item.description) > 80 }">...</c:if>
					</div>
					<div class="subtitle">
						Tags:
						<c:set var="tags" value="${fn:split(item.tags, ' ') }" />
						<c:forEach items="${tags}" var="tag">
							<a href="${urlTag }${tag}">${tag }</a>
						</c:forEach>
					</div>
				</div>
			</li>
		</c:forEach>
	</ul>
</section>
</c:if>

<section class="reviews">
	<div class="section-header">Reviews</div>
	<div class="reviews-container">
		<div class="review-header">
			<div class="review-header-message"></div>
			<span class="review-average-score"></span>
			<span class="review-count"></span>
		</div>
		
		<c:if test="${user.username != target.user.username }">
		<div class="review-container user-review ui-state-highlight">
			<div class="reviewer-container">
				<img class="reviewer-img" src="${user.imageUrl }" />
				<span class="reviewer-name">${user.username }</span>
				<div class="review-star-container"></div>
			</div>
			<div class="review-justification"></div>
			
			<div class="review-form-container">
				<form id="review-form">
					<textarea name="justification" class="justification" rows="3" cols="50" placeholder="Say something about ${business.fullName }"></textarea>
					<input class="review-score" type="hidden" name="score" />
					<div>
						<a class="review-submit button" href="javascript:;">Submit</a>
					</div>
				</form>
			</div>
		</div>
		</c:if>
		
		<div class="reviews-list"></div>
	</div>
</section>

<c:if test="${(not empty businessSubscriptions && fn:length(businessSubscriptions) > 0) || (not empty userSubscriptions && fn:length(userSubscriptions) > 0) }">
	<section>
		<div class="section-header">Subscriptions</div>
		<c:forEach items="${businessSubscriptions }" var="bSub">
			<a class="fatlink dgte-previewlink" previewtype="business" href="${urlProfile}${bSub.domain}"><img class="preview-image" src="${bSub.profilepic != null ? bSub.profilepic.smallSquare : noimage50 }" /></a>
		</c:forEach>
		<c:forEach items="${userSubscriptions }" var="uSub">
			<a class="fatlink dgte-previewlink" previewtype="user" href="${urlUserProfile}${uSub.username}"><img class="preview-image" src="${uSub.imageUrl }" /></a>
		</c:forEach>
	</section>
</c:if>

<section class="feed" id="feed" type="user">
	<div class="section-header">Feed</div>
	<c:if test="${target.username == user.username }">
		<div class="newpost">
			<form id="form-newpost">
				<div class="border-provider ui-state-active inline-block">
					<div class="status-title-container">
						<input name="title" class="status-title ui-state-active hide" maxlength="45" type="text" placeholder="title" />
					</div>
					<textarea name="text" class="status-textarea noattachment" maxlength="140" rows="1" placeholder="<spring:message code="home.status.textarea" />"></textarea>	
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
				<div class="newpost-errors"></div>
				
				<input type="text" name="tags" class="ipt-tags ui-state-active" placeholder="tags 'news buglasan pics-2012' (space-delimited, 3 max)" />
			</form>
			<div class="status-options hide">
				<div class="floatright">
					<span class="status-counter"></span>
					<div class="post-as">
						<div class="menubutton post-as-visibles" title="Posting as ${user.username }">
							<img src="${user.imageUrl }" />
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
								<div class="name"><span class="ui-icon ui-icon-video inline-block mr5"></span>Video</div>
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
		</div>
	</c:if>
	
	<div class="feed-container">
		<div class="alert-container ui-state-highlight hide pd5"></div>
		<ul class="posts"></ul>
		<div class="loadmoreContainer" style="text-align: center; height: 100px; position: relative;">
			<button class="loadmore" style="width: 50%; margin-top: 50px;">Load 10 more</button>
		</div>
	</div>
</section>

</div><!-- grid_8 -->

<sec:authorize access="hasRole('ROLE_ADMIN')">
<div class="grid_3 sidebar-section">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Admin menu</div>
		<button class="btn-grant-coconuts">Grant coconuts</button>
		<p>${target.username } currently has ${target.billingInfo.coconuts } coconuts.
	</div>
</div>
<div class="dlg-grant-coconuts hide">
	<form id="frm-grant-coconuts" method="post" action="<spring:url value='/p/user/grantcoconuts/${target.id }' />">
		<label for="ipt-grant-coconuts">How many?</label>
		<input type="number" name="howmany" id="ipt-grant-coconuts" />
	</form>
</div>
<style>
.btn-grant-coconuts {
	width: 100%;
}
</style>

<script>
$(function(){
	var 
		$btnGrant = $('.btn-grant-coconuts'),
		$dlgGrant = $('.dlg-grant-coconuts'),
		$frmGrant = $('#frm-grant-coconuts');
	
	$frmGrant.validate({
		rules : {
			howmany: {
				required: true,
				number: true,
				range: [1, 1000]
			}
		}
	});
	
	$btnGrant.click(function(){
		$dlgGrant
			.attr('title', 'Grant coconuts to ' + target.username)
			.dialog({
				buttons: {
					'Grant' : function(){
						if(!$frmGrant.valid()) {
							return false;
						}
						$frmGrant.submit();
					},
					'Cancel': function(){
						$(this).dialog('close');
					}
				}
			});
		
	});
});
</script>
</sec:authorize>

<div class="grid_3 sidebar-section">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Interact</div>
		<button class="btn-subscribe-toggle">Subscribe to ${target.user.username }</button>
	</div>
</div>

<script>
window.urls = {
	user : '<spring:url value="/p/user/" />',
	subscribe: '<spring:url value="/i/subscribe/user/${target.id}.json" />',
	unsubscribe: '<spring:url value="/i/unsubscribe/user/${target.id}.json" />',
	review: '<spring:url value="/i/review/user/" />',
	allReviews: '<spring:url value="/i/allreviews/user/" />',
	targetPosts: '<spring:url value="/i/posts/" />', //get the last posts by this user
	
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
	imgurPage : 'http://imgur.com/',
	searchOwn: '<spring:url value="/s/own/" />'
}

window.poster = {
	id: '${target.id}'
}

window.target = {
	facebookId: '${target.user.id}',
	username: '${target.user.username}'
}

window.user = {
	id: '${user.id}',
	username: '${user.username}',
	rank: '${user.rank}'
}

window.reviewconstants = {
	target: {
		id: '${target.id}',
		name: '${target.user.username}'
	}
}

$(function(){
	//subscribe
	var subscribed = '${subscribed}' === 'true';
	var $btnSubscribe = $('.btn-subscribe-toggle');
	
	function refreshSubsButton() {
		if(subscribed) {
			$btnSubscribe
				.button({label: 'Subscribed', icons: {secondary: 'ui-icon-circle-check'}})
				.unbind('hover')
				.hover(function(){
					$btnSubscribe.button({label: 'Unsubscribe', icons: {}});
				}, function(){
					$btnSubscribe.button({label: 'Subscribed', icons:{secondary: 'ui-icon-circle-check'}});
				});
		} else {
			$btnSubscribe
				.button({label: 'Not subscribed', icons: {secondary: 'ui-icon-circle-close'}})				
				.unbind('hover')
				.hover(function(){
					$btnSubscribe.button({label: 'Subscribe', icons: {}});
				}, function(){
					$btnSubscribe.button({label: 'Not subscribed', icons:{secondary: 'ui-icon-circle-close'}});
				});
		}
	}
	refreshSubsButton();

	$btnSubscribe.click(function(){
		var url = subscribed ? urls.unsubscribe : urls.subscribe;
		$.post(url, function(response) {
			switch(response.status) {
			case '200':
				subscribed = !subscribed;
				refreshSubsButton();
				break;
			default:
				debug(response);
			}
		});
	});
});
</script>

<script src="${jsReviews }"></script>

<!-- Notifications -->
<%@include file="../grids/notifications3.jsp"  %>