<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../tiles/links.jsp" %>

<title>${tagString }</title>
<link rel="stylesheet" href="<spring:url value='/resources/css/buyandsell.css' />" />
<script type="text/javascript" src="${jsApplication }"></script>

<div class="grid_8">

<c:choose>
<c:when test="${empty tag }">
	<span class="header">No items found with tag <strong>${tagString }</strong></span>
</c:when>
<c:otherwise>
	<span class="header">Items with tag <strong>${tag.tag }</strong></span>
	<div class="items-container">
		<ul class="items">
		<c:forEach items="${items }" var="item" varStatus="index">
			<li class="trade-item trade-item-bytag">
				<a href="${urlTrade }${item.id }"><img class="trade-img thumbnail" src="${item.imgur.smallSquare }"></a>
				<div class="trade-item-details">
					<a href="${urlTrade }${item.id }"><div>
						${item.id } - ${item.name }
						<c:if test="${item.buyAndSellMode eq 'auction' }">(Auction)</c:if>
						<c:if test="${item.buyAndSellMode eq 'trade' }">(Trade)</c:if>
					</div></a>
					<div>${item.owner.username }</div>
					<div class="item-tags">${item.tags }</div>
				</div>
			</li>
			<c:if test="${(index.index+1)%2==0 && index.index != 0 }"><div class="clear"></div></c:if>
		</c:forEach>
		</ul>
	</div>
	<c:if test="${fn:length(items) == 10 }">
		<div class="clear"></div>
		<div class="load-more-container">
			<button class="load-more">Load 10 more..</button>
		</div>
	</c:if>
</c:otherwise>
</c:choose>

</div>

<style>
.header {
	font-size: 2em;
}

.load-more-container {
	text-align: center;
	height: 48px;
	position: relative;
	padding-top: 20px;
}
.load-more {
	width: 50%;
}
</style>

<script>
window.tagEntity = {
	tag: '${tag.tag}',
	items: '${tag.items}'
}
window.urls = {
	trade: '<spring:url value="/t/" />',
	//grids
	tagweights: '<spring:url value="/s/tags.json" />',
	tag: '<spring:url value="/t/tags/" />' //also used by page js here
}

$(function(){
	var $btnLoadMore = $('.load-more'),
		$items = $('ul.items');
	
	var start = 0; //because 1 + 10 = 11 and that's the first item that's not pre-loaded
	var perload = 10;
	var loadedAll = false;
	
	$btnLoadMore.click(function(){
		$('<div class="overlay">').appendTo($btnLoadMore.parent());
		start += perload;
		$.get(urls.tag + tagEntity.tag + '/' + start + '/' + perload + '.json', function(response) {
			switch(response.status) {
			case '200':
				for(var i = 0, len = response.items.length; i < len; ++i) {
					addItem($items, response.items[i]);
				}
				processTags();
				if(response.items.length < perload) {
					debug(response.items.length + '<' + perload);
					loadedAll = true;
				}
				break;
			default:
				debug('error getting additional items');
				debug(response);
			}
		}).complete(function(){
			$btnLoadMore.parent().find('.overlay').fadeOut(200, function() { $(this).remove(); });
			if(loadedAll) {
				$btnLoadMore.replaceWith($('<div>').html('All items with tag <strong>' + tagEntity.tag + '</strong> loaded.' ));
			}
		});
	});
	
	function addItem($container, item) {
		var $li = $('<li class="trade-item trade-item-bytag">').appendTo($container);
		
		//image and link
		var $imgLink = $('<a>').attr('href', urls.trade + item.id).appendTo($li);
		$('<img class="trade-img thumbnail">').attr('src', item.imgur.smallSquare).appendTo($imgLink);

		var $itemDetails = $('<div class="trade-item-details">').appendTo($li);
		var $detailsLink = $('<a>').attr('href', urls.trade + item.id).appendTo($itemDetails);
		
		$detailsLink.append(item.id + '-' + item.name);
		if(item.buyAndSellMode == 'auction') {
			$detailsLink.append('(Auction)');
		} else if(item.buyAndSellMode == 'trade') {
			$detailsLink.append('(Trade)');
		}
		
		$('<div>').text(item.ownerSummary.name).appendTo($itemDetails);
		$('<div class="item-tags">').text(item.tags).appendTo($itemDetails);
		
		$li.hide().fadeIn(1000);
	}
	
	function processTags() {
		$('.item-tags:not(.processed)').each(function(i, tags){
			var $tags = $(tags);
			var tagString = $tags.text();
			var split = tagString.split(' ');
			
			$tags.addClass('processed').html('Tags - ');
			for(var i = 0, len = split.length; i < len; ++i) {
				$('<a>').attr('href', urls.tag + split[i]).text(split[i]).appendTo($tags);
				$tags.append(' ');
			}
		});
	}
	processTags();
});
</script>

<!-- Watched Tags -->
<div class="watched-tags-container sidebar-section grid_4">
	<div class="sidebar-section-header">Watched tags</div>
	<c:choose>
	<c:when test="${not empty user.watchedTags }">
	<c:forEach items="${user.watchedTags }" var="watchedTag">
		<a class="watched-tag" href="javascript:;" tag="${watchedTag }">${watchedTag }</a>
	</c:forEach>
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
<c:if test="${not empty user.watchedTags }">
<script>
urls.watchedtags = '<spring:url value="/t/watchedtags" />'
</script>
<script src="${jsWatchedTags }" /></script>
</c:if>
<!-- End watched tags -->

<!-- Tagcloud -->
<div class="tagcloud-container sidebar-section grid_4">
	<div class="sidebar-section-header">Popular tags</div>
	<div class="tagcloud"></div>
</div>
<script src="${jsTagcloud }"></script>
<script src="${jsDgteTagCloud }"></script>
<link rel="stylesheet" href="<spring:url value='/resources/css/grids/tagcloud.css' />" />
<!-- End tagcloud -->