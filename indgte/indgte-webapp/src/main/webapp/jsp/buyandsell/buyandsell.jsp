<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../tiles/links.jsp" %>

<title>Buy & Sell In Dumaguete</title>
<link rel="stylesheet" href="<spring:url value='/resources/css/buyandsell.css' />" />
<script type="text/javascript" src="${jsApplication }"></script>
<script src="http://ajax.microsoft.com/ajax/jQuery.Validate/1.6/jQuery.Validate.min.js"></script>

<div class="grid_12">

<section class="newitem">
	<button class="btn-newitem">Sell your possessions</button>
	<div class="newitem-form-container hide" title="Sell your possessions">
		<div class="newitem-img-container noimage"><div class="newitem-img-message">Drop picture here (or click)</div></div>
		<form id="newitem-form" method="post">
			<ul class="newitem-formfields">
				<li>Name</li>
				<li><input type="text" class="newitem-name" name="name" /></li>
				<li>Description</li> 
				<li><textarea class="newitem-description" rows="5" name="description"></textarea></li>
				<li>Sell mode</li>
				<li> 
					<select class="sellmode" name="sellMode">
						<option value="fixed">Normal</option>
						<option value="auction">Auction</option>
						<option value="trade">Trade</option>
					</select>
				</li>
				
				<li class="li-sellmode fixedprice">Price<br/><input type="number" class="newitem-fixedprice" name="fixedprice"/></li>
				<li class="li-sellmode fixedprice">
					<input type="checkbox" name="negotiable" id="fixedprice-negotiable" class="newitem-fixedprice-negotiable" />
					<label for="fixedprice-negotiable">Negotiable?</label>
				</li>
				
				<li class="li-sellmode auction">Starting Price<br/><input type="number" class="newitem-bidding-startingprice" name="startingprice"/></li>
				<li class="li-sellmode auction">Buyout<br/><input type="number" class="newitem-bidding-buyout" name="buyout"/></li>
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
</section>

<section class="owned">
</section>

<section class="popular">
	Popular
	<ul>
		<c:forEach items="${popular }" var="item">
		<li class="trade-item">
			<a href="${urlTrade }${item.id }"><img class="trade-img" src="${item.imgur.smallSquare }" /></a>
			<ul class="trade-item-details">
				<li>
					<strong><a href="${urlTrade}${item.id}">${item.name }
					<c:if test="${item.buyAndSellMode eq 'auction' }">(Auction)</c:if>
					<c:if test="${item.buyAndSellMode eq 'trade' }">(Trade)</c:if>
					</a></strong>
				</li>
			
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
		</li>
		</c:forEach>
	</ul>
</section>

<div style="clear: both;">&nbsp;</div>

<section class="recent">
	Recent
	<ul>
		<c:forEach items="${recent }" var="item">
		<li class="trade-item">
			<a href="${urlTrade }${item.id }"><img class="trade-img" src="${item.imgur.smallSquare }" /></a>
			<ul class="trade-item-details">
				<li>
					<strong><a href="${urlTrade}${item.id}">${item.name }
					<c:if test="${item.buyAndSellMode eq 'auction' }">(Auction)</c:if>
					<c:if test="${item.buyAndSellMode eq 'trade' }">(Trade)</c:if>
					</a></strong>
				</li>
			
				<!-- bidding -->
				<c:if test="${item.buyAndSellMode eq 'auction' }">
				<li>current: ${item.start }</li>
				<li>buyout: ${item.buyout }</li>
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
				
				<li class="subtitle"><span class="time">${item.time.time }</span> by <a href="${urlUserProfile }${item.owner.username}">${item.owner.username }</a></li>
				<li class="subtitle">${item.views } views</li>
			</ul>
		</li>
		</c:forEach>
	</ul>
</section>

</div>

<script>
window.urls = {

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
});
</script>