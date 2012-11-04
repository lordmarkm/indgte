<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../tiles/links.jsp" %>

<title>${item.name }</title>
<link rel="stylesheet" href="<spring:url value='/resources/css/buyandsell.css' />" />
<script type="text/javascript" src="${jsApplication }"></script>
<script src="http://ajax.microsoft.com/ajax/jQuery.Validate/1.6/jQuery.Validate.min.js"></script>

<div class="grid_8">

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
	<li>current: &#8369;${item.start }</li>
	<li>buyout: &#8369;${item.buyout }</li>
	<li>end date: <span class="time">${item.biddingEnds.time }</span></li>
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

</div>

<!-- only for overrides of buyandsell.css -->
<style>
.trade-item-details {
	margin: 0 0 0 205px;
}
</style>

<script>
window.urls = {

}

$(function(){
	$('.time').each(function(i, div) {
		var fromnow = moment(parseInt($(div).text())).fromNow();
		$(div).text(fromnow);
	});
});
</script>