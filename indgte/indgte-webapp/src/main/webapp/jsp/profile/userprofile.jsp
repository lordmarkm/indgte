<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@include file="../tiles/links.jsp" %>

<title>${target.user.username } InDumaguete</title>
<script src="http://ajax.microsoft.com/ajax/jQuery.Validate/1.6/jQuery.Validate.min.js"></script>
<link rel="stylesheet" href="<spring:url value='/resources/css/review.css' />" />
<link rel="stylesheet" href="<spring:url value='/resources/css/userprofile/userprofile.css' />" />

<div class="grid_9">

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

<section class="feed">
</section>

<c:if test="${not empty target.businesses }">
<section class="businesses">
	<div class="section-header">Businesses</div>
	<ul>
	<c:forEach items="${target.businesses }" var="business">
		<li>
			${business.name }
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

</div><!-- grid_8 -->


<div class="grid_3">
	<section class="interact">
		<button class="btn-subscribe-toggle">Subscribe to ${target.user.username }</button>
	</section>
</div>

<script>
window.urls = {
	user : '<spring:url value="/p/user/" />',
	subscribe: '<spring:url value="/i/subscribe/user/${target.id}.json" />',
	unsubscribe: '<spring:url value="/i/unsubscribe/user/${target.id}.json" />',
	review: '<spring:url value="/i/review/user/" />',
	allReviews: '<spring:url value="/i/allreviews/user/" />'
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