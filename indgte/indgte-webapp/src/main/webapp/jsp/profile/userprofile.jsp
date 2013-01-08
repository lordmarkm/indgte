<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@include file="../tiles/links.jsp" %>

<title>${target.user.username } InDumaguete</title>
<script src="${jsValidator}"></script>
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

<!-- Notifications -->
<%@include file="../grids/notifications3.jsp"  %>