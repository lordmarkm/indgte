<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../tiles/links.jsp" %>

<title>${fn:toUpperCase(tagString) } In Dumaguete</title>
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

<div class="sidebar-section grid_4">
	<div class="sidebar-section-header">Search for Items tagged ${tag.tag }</div>
	<div class="buyandsell-search">
		<form id="buysellsearchform">
			<input type="text" class="ipt-search" placeholder="<spring:message code='buyandsell.tag.search' arguments="${tag.tag }" />"/>
			<button class="btn-search">Search</button>
		</form>
		<div class="buyandsell-search-results-container">
			<ul class="buyandsell-search-results"></ul>
			<a href="javascript:;" class="showmore hide">Show more...</a>
		</div>
	</div>
</div>

<div class="sidebar-section grid_4">
	<div class="sidebar-section-header">Watch Tag</div>
	<c:choose>
	<c:when test="${watched }">
		<p>You are watching this tag.</p>
	</c:when>
	<c:otherwise>
		<button class="btn-watch-tag" style="width: 100%;">Watch this Tag for New Items</button>
	</c:otherwise>
	</c:choose>
	
	<div class="sidebar-divider"></div>
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
	tagsearch: '<spring:url value="/s/buysell/" />',
	//grids
	tagweights: '<spring:url value="/s/tags.json" />',
	tag: '<spring:url value="/t/tags/" />', //also used by page js here
	watchedtags: '<spring:url value="/t/watchedtags" />' //also used by page js here
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
	
	//watch tag
	var $btnWatchTag = $('.btn-watch-tag');
	
	$btnWatchTag.click(function(){
		$btnWatchTag.button('disable');
		$.post(urls.watchedtags + '/' + tagEntity.tag + '.json', function(response) {
			switch(response.status) {
			case '200':
				window.location.reload();
				break;
			default:
				debug('error adding watch tag');
				debug(response);
			}
		});
	});
	
	//search this tag
	var 
		$searchform = $('#buysellsearchform'),
		$iptSearch = $('.ipt-search'),
		$btnSearch = $('.btn-search'),
		$results = $('.buyandsell-search-results'),
		$showmore = $('.showmore');
	
	$searchform.submit(function(){
		return false;
	});
	
	$iptSearch.on({
		focus: function(){
			this.select();
		},
		click: function(){
			this.select();
		}
	});
	
	var start = 0, perload = 5, waiting = false, lastTerm;
	
	function search(term, clearprevious) {
		waiting = true;
		$btnSearch.button('disable');
		$showmore.hide();
		dgte.overlay($results.parent(), true);
		$.get(urls.tagsearch + tagEntity.tag + '/' + term + '/' + start + '/' + perload + '.json', function(response) {
			switch(response.status) {
			case '200':
				if(clearprevious) {
					$results.html('');
				}
				
				var len = response.items ? response.items.length : 0;
				if(0 == len) {
					$results.html('No results for ' + term);
				}
				if(len == perload) {
					$showmore.show();
				} else {
					$showmore.hide();
				}
				
				for(var i = 0; i < len; ++i) {
					dgte.addBuySellItem($results, response.items[i], urls.trade)
				}
				break
			default:
				debug('error searching buy and sell');
				debug(response);
			}
		}).error(function(){
			$results.html('There was an error during the search. Please try again.');
		}).complete(function(){
			dgte.fadeOverlay($results.parent(), function(){
				waiting = false;
				$btnSearch.button('enable');
			});
		});
	}
	
	$btnSearch.click(function(){
		var term = $iptSearch.val();
		lastTerm = term;
		if(term.length < 2 || waiting) {
			return;
		}
	
		start = 0;
		search(term, true);
	});
	
	$showmore.click(function(){
		if(!lastTerm) return;
		start += 5;
		search(lastTerm, false);
	});
});
</script>

<!-- Watched Tags -->
<div class="watched-tags-container sidebar-section grid_4">
	<div class="sidebar-section-header">Watched tags</div>
	<c:choose>
	<c:when test="${not empty user.watchedTags }">
	<div class="watched-tag-link-container">
		<a class="watched-tag selected" href="javascript:;" tag="all">All</a>
	<c:forEach items="${user.watchedTags }" var="watchedTag">
		<a class="watched-tag" href="javascript:;" tag="${watchedTag.tag }">${watchedTag.tag }</a>
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
<c:if test="${not empty user.watchedTags }">
<script src="${jsWatchedTags }" /></script>
<link rel="stylesheet" href="<c:url value='/resources/css/grids/watchedtags.css' />" />
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