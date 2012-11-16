<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../tiles/links.jsp" %>

<title>Buy & Sell In Dumaguete</title>
<link rel="stylesheet" href="<spring:url value='/resources/css/buyandsell.css' />" />
<script type="text/javascript" src="${jsApplication }"></script>
<script src="http://ajax.microsoft.com/ajax/jQuery.Validate/1.6/jQuery.Validate.min.js"></script>

<div class="grid_8">

<span class="page-header">Buy & Sell</span>

<section class="popular">
	<span class="section-header">Popular</span>
	<ul>
		<c:forEach items="${popular }" var="item" varStatus="index">
		<li class="trade-item">
			<a href="${urlTrade }${item.id }"><img class="trade-img thumbnail" src="${item.imgur.smallSquare }" /></a>
			<ul class="trade-item-details">
				<li>
					<strong><a href="${urlTrade}${item.id}">${item.name }
					<c:if test="${item.buyAndSellMode eq 'auction' }">(Auction)</c:if>
					<c:if test="${item.buyAndSellMode eq 'trade' }">(Trade)</c:if>
					</a></strong>
				</li>
			
				<!-- bidding -->
				<c:if test="${item.buyAndSellMode eq 'auction' }">
				<li>Winning: ${item.start }</li>
				<li>Buyout: ${item.buyout }</li>
				<li>Ends: <span class="time">${item.biddingEnds.time }</span></li>
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
		</li>
		<c:if test="${(index.index+1)%3 == 0 && index.index != 0}"><div class="clear"></div></c:if>
		</c:forEach>
	</ul>
</section>

<div style="clear: both;">&nbsp;</div>

<section class="recent">
	<span class="section-header">Recent</span>
	<ul>
		<c:forEach items="${recent }" var="item" varStatus="index">
		<li class="trade-item">
			<a href="${urlTrade }${item.id }"><img class="trade-img thumbnail" src="${item.imgur.smallSquare }" /></a>
			<ul class="trade-item-details">
				<li>
					<strong><a href="${urlTrade}${item.id}">${item.name }
					<c:if test="${item.buyAndSellMode eq 'auction' }">(Auction)</c:if>
					<c:if test="${item.buyAndSellMode eq 'trade' }">(Trade)</c:if>
					</a></strong>
				</li>
			
				<!-- bidding -->
				<c:if test="${item.buyAndSellMode eq 'auction' }">
				<li>Winning: ${item.start }</li>
				<li>Buyout: ${item.buyout }</li>
				<li>Ends: <span class="time">${item.biddingEnds.time }</span></li>
				</c:if>
			
				<!-- trade -->
				<c:if test="${item.buyAndSellMode eq 'trade' }">
				<li>trade for: ${item.tradefor }</li>
				</c:if>
			
				<!-- fixed -->
				<c:if test="${item.buyAndSellMode eq 'fixed' }">
				<li>&#8369; ${item.price } <c:if test="${item.negotiable }">(negotiable)</c:if></li>
				</c:if>
				
				<li class="subtitle"><span class="time">${item.time.time }</span> by <a href="${urlUserProfile }${item.owner.username}">${item.owner.username }</a></li>
				<li class="subtitle">${item.views } views</li>
			</ul>
		</li>
		<c:if test="${(index.index+1)%3 == 0 && index.index != 0}"><div class="clear"></div></c:if>
		</c:forEach>
	</ul>
</section>

</div>

<div class="newitem sidebar-section grid_4">
	<button class="btn-newitem">Sell your possessions</button>
	<div class="newitem-form-container hide" title="Sell your possessions">
		<div class="newitem-img-container noimage"><div class="newitem-img-message">Drop picture here (or click)</div></div>
		<form id="newitem-form" method="post">
			<ul class="newitem-formfields">
				<li>Name</li>
				<li><input type="text" class="newitem-name" name="name" /></li>
				<li>Description</li> 
				<li><textarea class="newitem-description" rows="5" name="description"></textarea></li>
				<li>Tags (maximum of 5)</li>
				<li>
					<span class="active-tags"></span><input type="text" name="tags" placeholder="space-delimited e.g. 'shoes mens-sports-shoes kalenji'"/>
				</li>
				<li>Sell mode</li>
				<li> 
					<select class="sellmode" name="sellMode">
						<option value="fixed">Normal</option>
						<option value="auction">Auction</option>
						<option value="trade">Trade</option>
					</select>
				</li>
				<li class="li-sellmode fixedprice">Price<br/><input type="number" value="0" class="newitem-fixedprice" name="fixedprice"/></li>
				<li class="li-sellmode fixedprice">
					<input type="checkbox" name="negotiable" id="fixedprice-negotiable" class="newitem-fixedprice-negotiable" />
					<label for="fixedprice-negotiable">Negotiable?</label>
				</li>
				
				<li class="li-sellmode auction">Starting Price<br/><input type="number" value="0" class="newitem-bidding-startingprice" name="startingprice"/></li>
				<li class="li-sellmode auction">Buyout<br/><input type="number" value="0" class="newitem-bidding-buyout" name="buyout"/></li>
				<li class="li-sellmode auction">End date<br/><input type="text" class="newitem-bidding-enddate" readonly="readonly" name="enddate"/></li>
				
				<li class="li-sellmode trade">Will trade for</li>
				<li class="li-sellmode trade"><input type="text" class="newitem-trade-tradefor" name="tradefor" /></li>
			</ul>
			<input type="hidden" class="newitem-hash" name="hash" />
			<input type="hidden" class="newitem-deletehash" name="deletehash" />
			<input type="file" style="visibility: collapse; width: 0px;" class="newitem-img-file" />
		</form>
		<div class="newitem-form-errors"></div>
	</div>
	
	<div class="buyandsell-search">
		<form id="buysellsearchform">
			<input type="text" class="ipt-search" placeholder="<spring:message code='buyandsell.search' />"/>
			<button class="btn-search">Search</button>
		</form>
		<div class="buyandsell-search-results-container">
			<ul class="buyandsell-search-results"></ul>
			<a href="javascript:;" class="showmore hide">Show more...</a>
		</div>
	</div>
	<div class="sidebar-divider"></div>
</div>

<script>
window.urls = {
	buysellsearch: '<spring:url value="/s/buysell/" />',
	buysellitem: '<spring:url value="/t/" />',
	//grids
	tagweights: '<spring:url value="/s/tags.json" />',
	tag: '<spring:url value="/t/tags/" />', //also used by page js here
	watchedtags: '<spring:url value="/t/watchedtags" />'
}

$(function(){
	var $btnNewItem = $('.btn-newitem'),
		$formNewItemContainer = $('.newitem-form-container'),
		$formNewItem = $('#newitem-form'),
		$formError = $('.newitem-form-errors'),
		$iptSellmode = $('.sellmode'),
		$iptBiddingEndDate = $('.newitem-bidding-enddate').datepicker({minDate: '+3d' }),
		$newItemImgContainer = $('.newitem-img-container'),
		$iptImageFile = $('.newitem-img-file'),
		$iptHash = $('.newitem-hash'),
		$iptDeletehash = $('.newitem-deletehash');
	
	//new item
	//open dialog
	$btnNewItem.click(function(){
		$formNewItemContainer.dialog({
			buttons: {
				'Cancel' : function() {
					$(this).dialog('close');
				},
				'Sell!' : function() {
					$formNewItem.submit();
					$formNewItemContainer.find('button').button('disable');
					//$(this).dialog('close');
				}
			}
		});
		$iptSellmode.change();
	});
	
	//upload image
	$newItemImgContainer.on({
		click: function() {
			$iptImageFile.click();
		},
		dragover: function(event) {
	    	event.preventDefault(); 
			$(this).addClass('hungry');
		},
		dragleave: function(event) {
	    	event.preventDefault(); 
			$(this).removeClass('hungry');
		},
		drop: function(event) {
	    	event.preventDefault(); 
	    	$(this).removeClass('hungry');
			$('<div class="overlay">').appendTo($newItemImgContainer);
    		dgte.upload(event.originalEvent.dataTransfer.files[0], uploadCallback);
		}
	});
	
	$iptImageFile.change(function(){
		$('<div class="overlay">').appendTo($newItemImgContainer);
		dgte.upload(this.files[0], uploadCallback);
	});
	
	function uploadCallback(imgurResponse) {
		$newItemImgContainer.removeClass('noimage').find('*:not(.overlay)').remove();
		$iptHash.val(imgurResponse.upload.image.hash);
		$iptDeletehash.val(imgurResponse.upload.image.deletehash);
		$('<img>').attr('src', imgurResponse.upload.links.small_square).appendTo($newItemImgContainer);
		$newItemImgContainer.find('.overlay').delay(800).fadeOut(200, function() { $(this).remove(); });
		$('.error-noimage').remove();
	}
	
	//sell mode
	$iptSellmode.change(function(){
		var value = $iptSellmode.val();
		$('.li-sellmode').hide();

		switch(value) {
		case 'fixed':
			$('.li-sellmode.fixedprice').show();
			break;
		case 'auction':
			$('.li-sellmode.auction').show();
			break;
		case 'trade':
			$('.li-sellmode.trade').show();
			break;
		default:
			debug('Invalid value: ' + value);
		}
	});
	
	//validate and submit the form
	$.validator.setDefaults({ignore: []}); //allow validation of hidden fields
	$formNewItem.validate({
		rules : {
			name: {
				required: true
			},
			description: {
				required: true
			}
		},
		messages : {
			name: 'Item name is required',
			description: 'Please provide a brief description of the item'
		},
		errorPlacement: showError
	});
	
	function showError(error, element) {
		$formError.html('').append(error);
	}
	
	function validateConditionalReqts() {
		//validate conditional requirements
		var value = $iptSellmode.val();

		switch(value) {
		case 'fixedprice':
			$('.li-sellmode.fixedprice').show();
			break;
		case 'bidding':
			$('.li-sellmode.bidding').show();
			break;
		case 'trade':
			$('.li-sellmode.trade').show();
			break;
		default:
			debug('Invalid value: ' + value);
		}
	}
	
	$formNewItem.submit(function(){
		if(!$formNewItem.valid()) return false;
		
		//validate image
		if(!$iptHash.val()) {
			showError($('<span class="error-noimage">').text('You need to provide a picture of the item.'));
			return false;
		}
	});
	
	//popular, recent, owned
	$('.trade-item').on({
		mouseenter: function(){
			$(this).addClass('ui-state-highlight');
		},
		mouseleave: function() {
			$(this).removeClass('ui-state-highlight');
		}
	});
	
	$('.time').each(function(i, div) {
		var fromnow = moment(parseInt($(div).text())).fromNow();
		$(div).text(fromnow);
	});
	
	
	//search 
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
		$.get(urls.buysellsearch + term + '/' + start + '/' + perload + '.json', function(response) {
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
					dgte.addBuySellItem($results, response.items[i], urls.buysellitem)
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