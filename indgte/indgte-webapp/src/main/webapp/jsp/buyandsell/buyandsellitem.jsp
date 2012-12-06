<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@include file="../tiles/links.jsp" %>

<title>${item.name }</title>
<link rel="stylesheet" href="<spring:url value='/resources/css/buyandsell.css' />" />
<script type="text/javascript" src="${jsApplication }"></script>
<script src="http://ajax.microsoft.com/ajax/jQuery.Validate/1.6/jQuery.Validate.min.js"></script>

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
	<a href="${urlUserProfile }${item.owner.username}"><img class="seller-img" src="${item.owner.imageUrl }" /></a>
	<ul class="trade-seller-details">
		<li><a href="${urlUserProfile }${item.owner.username}">${item.owner.username }</a></li>
		<li>Reputation: Scoundrel</li>
		<li><a href="${item.owner.profileUrl }">View Facebook profile</a></li>
	</ul>
</section>

</div>

<sec:authorize access="hasRole('ROLE_USER')">
<div class="buyandsell-controls grid_4 ui-widget sidebar-section">
	<div class="sidebar-section-header">Buy&Sell Item Actions</div>
	
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
			<div class="fb-like" data-send="true" data-width="450" data-show-faces="true"></div>
		</div>
	</div>	
	<div class="sidebar-divider"></div>
</div>
</sec:authorize>

<style>
/*buyandsell.css overrides */
.trade-item-details {
	margin: 0 0 0 205px;
}

/*page unique styles*/
.seller-img {
	float: left;
}

.auction-winning {
	font-weight: bold;
}
.auction-winning-amount {
	font-size: 1.5em;
	color: green;
}

.auction-countdown-container {
	font-weight: bold;
	width: 200px;
}
.auction-countdown {
	color: red;
}

.auction-bids-list-container {
	margin: 15px 0 0 0;
}
.auction-bids-list {
	margin-top: 0;
}

.item-controls-container {
	padding: 5px;
}
.auction-bid-amount {
	width: 65%;
}
.auction-bid {
	width: 31%;
}

.item-actions button {
	margin-top: 5px;
	width: 100%;
}
</style>

<script>
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


<!-- Top Tens -->
<div class="toptens-container grid_4 ui-widget sidebar-section">
<div class="sidebar-section-header">Top Tens</div>
<div class="toptens">
	Popular:
	<ul class="popular"></ul>
	Recent:
	<ul class="recent"></ul>
	
	<a href="<spring:url value='/i/toptens/' />">View all...</a>
</div>
</div>
<link rel="stylesheet" href="<spring:url value='/resources/css/grids/toptens.css' />" />
<script>
window.urls.topTens = '<spring:url value="/i/toptens.json" />',
window.urls.topTenLeader = '<spring:url value="/i/toptens/leader/" />',
window.urls.topTensPage = '<spring:url value="/i/toptens/" />'
</script>
<script src="${jsTopTens }"></script>