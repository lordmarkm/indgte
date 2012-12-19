<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@include file="../tiles/links.jsp" %>

<title>${item.name }</title>
<link rel="stylesheet" href="<spring:url value='/resources/css/buyandsell.css' />" />
<script src="${jsValidator}"></script>

<c:if test="${item.buyAndSellMode eq 'auction' }">
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery-countdown/1.6.0/jquery.countdown.min.js"></script>
<link rel="stylesheet" href="<spring:url value='/resources/css/jquery-countdown.css' />" />
</c:if>

<div class="grid_8 maingrid">

<span class="page-header">${item.name }</span>

<section class="item-details">
	<div class="section-header">Item</div>
	<a href="${urlImgur }${item.imgur.hash }" target="_blank"><img class="trade-img" src="${item.imgur.largeThumbnail }" /></a>
	<ul class="trade-item-details">
		<li>
			<strong><a href="${urlTrade}${item.id}">${item.name }
			<c:if test="${item.buyAndSellMode eq 'auction' }">(Auction)</c:if>
			<c:if test="${item.buyAndSellMode eq 'trade' }">(Trade)</c:if>
			</a></strong>
		</li>
		<li class="italic">${item.description }</li>
		
		<!-- bidding -->
		<c:if test="${item.buyAndSellMode eq 'auction' }">
		<li>Start: &#8369;${item.start }</li>
		<li>Buyout: &#8369;${item.buyout }</li>
		<li>Auction ends: <span class="time">${item.biddingEnds.time }</span></li>
		</c:if>
	
		<!-- trade -->
		<c:if test="${item.buyAndSellMode eq 'trade' }">
		<li>trade for: ${item.tradefor }</li>
		</c:if>
			
		<!-- fixed -->
		<c:if test="${item.buyAndSellMode eq 'fixed' }">
		<li>&#8369; ${item.price } <c:if test="${item.negotiable }">(negotiable)</c:if></li>
		</c:if>
				
		<li class="subtitle">Posted <span class="time">${item.time.time }</span> by <a href="${urlUserProfile }${item.owner.username}">${item.owner.username }</a></li>
		<li class="subtitle">${item.views } views</li>
	</ul>
</section>

<c:if test="${item.buyAndSellMode eq 'auction' }">
<div class="clear"></div>
<section class="auction">
	<h2>Auction</h2>
	
	<c:if test="${not empty item.winning }">
	<div class="auction-winning">
		Winning:  <span class="auction-winning-amount">&#8369;${item.winning.amount } <c:if test="${item.winning.bidder.username eq user.username }">(You)</c:if></span>
	</div>
	</c:if>
	
	<c:if test="${empty item.winning }">
		<div class="auction-winning">
			Winning:  <span class="auction-winning-amount">No bids yet</span>
		</div>
	</c:if>
	
	<c:if test="${owner }">
		<div class="auction-bids-list-container">
		Bids:
		<ol class="auction-bids-list">
			<c:forEach items="${item.reversedBids }" var="bid">
			<li><a href="${urlUserProfile }${bid.bidder.username}">${bid.bidder.username }</a> - ${bid.amount } (<span class="time">${bid.time.time }</span>)</li>
			</c:forEach>	
		</ol>
		</div>
	</c:if>
</section>
</c:if>

<div class="clear"></div>

<section class="seller-details">
	<div class="section-header">Seller</div>
	<a class="dgte-previewlink" previewtype="user" href="${urlUserProfile }${item.owner.username}"><img class="seller-img" src="${item.owner.imageUrl }" /></a>
	<ul class="trade-seller-details">
		<li><a class="dgte-previewlink fatlink" previewtype="user" href="${urlUserProfile }${item.owner.username}">${item.owner.username }</a></li>
		<li>${item.owner.rank }</li>
		<li><a href="${item.owner.profileUrl }">View <span class="capitalize">${item.owner.user.providerId }</span> profile</a></li>
	</ul>
</section>

<section>
	<div class="section-header">Comments</div>
	<div class="mt5">
		<div class="fb-like" data-send="true" data-width="450" data-show-faces="true"></div>
	</div>
	<div class="fb-comments" data-href="${baseURL}/t/${item.id}" data-width="620"></div>
</section>

</div>

<sec:authorize ifNotGranted="ROLE_USER">
<div class="grid_4 sidebar-section">
	<div class="sidebar-container">
		<img src="${logo }" />
	</div>
</div> 
</sec:authorize>

<sec:authorize access="hasRole('ROLE_USER')">
<div class="buyandsell-controls grid_4 sidebar-section">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Buy&Sell Item Actions</div>
		
		<c:if test="${owner }">
			<button class="btn-promote">Promote</button>
			<div class="dialog-promote hide" title="Promote this Item">
				<span><spring:message code="entity.promote.dialog" arguments="${user.billingInfo.coconuts },${user.billingInfo.coconuts / 10 },${item.name }" /></span>
				<form class="form-promote" method="post" action="<c:url value='/o/sidebar/buyandsellitem/${item.id }' />" >
					<table>
						<tr>
							<td><label for="start-date">Promote from</label></td>
							<td><input type="date" id="start-date" name="start" readonly="readonly" placeholder="Click to choose" /></td>
						</tr>
						<tr>
							<td><label for="end-date">Promote until</label></td>
							<td><input type="date" id="end-date" name="end" readonly="readonly" placeholder="Click to choose"/></td>
						</tr>
					</table>
				</form>
				<span class="coconut-cost"><spring:message code="promote.dialog.comp" /></span>
			</div>
		</c:if>
		
		<div class="item-controls-container">
			<c:if test="${item.buyAndSellMode eq 'auction' && !finished }">
				<div class="auction-countdown-container">
					Time left:
					<div class="auction-countdown"></div>
				</div>
			
				<div class="clear"></div>
			
				<c:if test="${not owner}">
					<div class="bid-container">
						<input type="number" class="auction-bid-amount" />
						<button class="auction-bid">Bid</button>
					</div>
				</c:if>
			</c:if>
		
			<div class="item-actions">
				<c:if test="${!inwishlist }">
				<button class="btn-wishlist-add">Add to Wishlist</button>
				</c:if>
			</div>
		</div>	
	</div>
</div>
</sec:authorize>
<script src="<spring:url value='/resources/javascript/promote.js' />" ></script>
<link rel="stylesheet" href="<c:url value='/resources/css/buyandsell/buyandsellitem.css' />" />

<script>
window.user = {
	coconuts: '${user.billingInfo.coconuts}'
}

window.constants = {
	bidIncrement: '${bidIncrement}'
}

window.urls = {
	bid: '<spring:url value="/t/bid/" />',
	wishlist: '<spring:url value="/i/wishlist/buyandsell/" />'
}

window.item = {
	id: '${item.id}'
}

$(function(){
	//wishlist
	
	$btnWishlistAdd = $('.btn-wishlist-add');
	
	$btnWishlistAdd.click(function(){
		$.post(urls.wishlist + item.id + '.json', function(response) {
			switch(response.status) {
			case '200':
				alert('Wish noted!');
				break;
			default:
				debug('Error making wish.');
				debug(response);
			}
		}).error(function(){
			if(confirm('Error! Your session may be expired. Reload page?'))
			window.location.reload();
		});
	});
	
	$('.time').each(function(i, div) {
		var fromnow = moment(parseInt($(div).text())).fromNow();
		$(div).text(fromnow);
	});
});

<c:if test="${item.buyAndSellMode eq 'auction' }">
<c:set var="winning" value="${item.winning.amount}" />

item.start = '${item.start}';
item.biddingEnds = '${item.biddingEnds.time}';

$(function(){
	//auction only script!
	var winning = '${winning}';
	
	var $winningAmount = $('.auction-winning-amount'),
		$countdown = $('.auction-countdown'),
		$iptBidAmount = $('.auction-bid-amount'),
		$btnBid = $('.auction-bid');
	
	function setBidAmount(amount) {
		if(!amount && winning) {
			var minBid = Math.ceil(parseFloat(winning) + (constants.bidIncrement * item.start));
			$iptBidAmount.val(minBid);
		} else if(!amount) {
			$iptBidAmount.val(item.start);
		} else {
			$iptBidAmount.val(amount);
		}
	}
	setBidAmount();
	
	$btnBid.click(function(){
		var amount = $iptBidAmount.val();
		//TODO validate amount
		$btnBid.button('disable');
		$.post(urls.bid + item.id + '/' + amount + '.json', function(response) {
			switch(response.status) {
			case '200':
				alert('You are now winning this auction.');
				$winningAmount.html('&#8369;' + amount + ' (You)');
				winning = amount;
				setBidAmount();
				break;
			case '418':
				debug(response);
				alert('You must bid at least ' + response.minimum);
				setBidAmount(response.minimum);
				break;
			default:
				debug('Bid failed.');
				debug(response);
			}
		})
		.error(function(){
			alert('wat');
		})
		.complete(function(){
			$btnBid.button('enable');
		});
	});
	
	$countdown.countdown({until: new Date(parseInt(item.biddingEnds))});
});
</c:if>

</script>

<sec:authorize access="hasRole('ROLE_USER')">
<!-- Watched Tags -->
<div class="watched-tags-container sidebar-section grid_4">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Watched tags</div>
		<c:choose>
		<c:when test="${not empty user.watchedTags }">
		<div class="watched-tag-link-container">
			<a class="watched-tag selected" href="javascript:;" tag="all">All</a>
		<c:forEach items="${user.watchedTags }" var="watchedTag">
			<a class="watched-tag" href="${urlTag }${watchedTag.tag }" tag="${watchedTag.tag }">${watchedTag.tag }</a>
		</c:forEach>
		</div>
		</c:when>
		<c:otherwise>
			<spring:url var="urlFaqWatchingTags" value="/h/buy-and-sell#watching-tags" />
			<p class="no-watched-tags"><spring:message code="watchedtags.empty" arguments="${urlFaqWatchingTags }"/></p>
		</c:otherwise>
		</c:choose>
		<div class="watched-tag-items-container">
			<ul class="watched-tag-items"></ul>
		</div>
		<div class="sidebar-divider"></div>
	</div>
</div>
<c:if test="${not empty user.watchedTags }">
<script src="${jsWatchedTags }" /></script>
<link rel="stylesheet" href="<c:url value='/resources/css/grids/watchedtags.css' />" />
</c:if>
<!-- End watched tags -->

<!-- Tagcloud -->
<div class="tagcloud-container sidebar-section grid_4">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Popular tags</div>
		<div class="tagcloud"></div>
	</div>
</div>
<script>
window.urls.tagweights = '<spring:url value="/s/tags.json" />';
</script>
<script src="${jsTagcloud }"></script>
<script src="${jsDgteTagCloud }"></script>
<link rel="stylesheet" href="<spring:url value='/resources/css/grids/tagcloud.css' />" />
<!-- End tagcloud -->
</sec:authorize>

<div class="dgte-preview"></div>
<sec:authorize access="hasRole('ROLE_USER')">
<!-- Notifications -->
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
</sec:authorize>