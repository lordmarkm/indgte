$(function(){
	var $reviewqueue = $('.reviewqueue'),
		$reviewqueuelist = $('.reviewqueue ul.reviewlist');
	
	function hideReviewQueue() {
		$reviewqueue.hide();
	}
	
	function valid() {
		if($reviewqueue.length === 0) {
			debug('Review queue container not available.');
			return false;
		}
		if(!urls) {
			debug('URLs not available in containing page');
			return false;
		}
		if(!urls.reviewqueue) {
			debug('Review queue url not available');
			return false;
		}
		if(!urls.business) {
			debug('Business Profile url not available');
			return false;
		}
		if(!dgte.urls.imgur) {
			debug('Imgur url (dgte.urls.imgur) not available');
			return false;
		}
		return true;
	}
	if(!valid()) {
		hideReviewQueue();
		return false;
	}
	
	$.get(urls.reviewqueue + '?time=' + new Date().getTime(), function(response) {
		switch(response.status) {
		case '200':
			var len = response.reviewqueue.length;
			
			if(len == 0) {
				hideReviewQueue();
			}
			
			for(var i = 0; i < len; ++i) {
				addReviewRequest($reviewqueuelist, response.reviewqueue[i]);
			}
			
			break;
		default:
			debug('Error getting review queue');
			debug(response);
		}
	}).error(function(){
		debug('Error getting review queue');
		debug(response);
		hideReviewQueue();
	});
	
	function addReviewRequest($container, summary) { //Summary, not BusinessProfile
		var $li = $('<li class="review-request">').attr('businessId', summary.id).appendTo($container);
		
		//img
		var $imgLink = $('<a>').attr('href', urls.business + summary.identifier).appendTo($li);
		var imageUrl = summary.thumbnailHash ? dgte.urls.imgur + summary.thumbnailHash + 's.jpg' : dgte.urls.blackSquareSmall;
		$('<img class="review-request-img">').attr('src', imageUrl).appendTo($imgLink);

		//infocontainer
		var $infocontainer = $('<div class="review-request-info">').appendTo($li);
		
		//fullname
		var $name = $('<div class="review-request-name">').appendTo($infocontainer);
		$('<a class="dgte-previewlink fatlink" previewtype="business">').attr('href', urls.business + summary.identifier).text(summary.title).appendTo($name);
		//description
		var description = summary.description.length > dgte.review.previewChars ?
				summary.description.substring(0, dgte.review.previewChars) + '...' :
				summary.description;
		$('<div class="subtitle">').text(description).appendTo($infocontainer);
		
		var $decline = $('<div class="review-decline-container">').appendTo($infocontainer);
		$('<a class="review-accede">').attr('href', urls.business + summary.identifier).text('Review').appendTo($decline);
		$('<a href="javascript:;" class="review-decline noreview">').text('Not now').appendTo($decline);
		$('<a href="javascript:;" class="review-decline neverreview">').text('Don\'t show again').appendTo($decline);
	}
	
	$reviewqueue.on({
		mouseenter: function(){
			$(this).addClass('ui-state-highlight').find('.review-decline-container a').css('color', 'black');
		},
		mouseleave: function(){
			$(this).removeClass('ui-state-highlight').find('.review-decline-container a').css('color', 'transparent');
		}
	}, '.review-request');
	
	$reviewqueue.on({
		click: function(event) {
			event.stopPropagation();
			var $li = $(this).closest('li.review-request');
			var isNeverReview = $(this).hasClass('neverreview');
			var urlRoot = isNeverReview ? urls.neverreview : urls.noreview;
			
			$.post(urlRoot + $li.attr('businessId') + '.json', function(response) {
				switch(response.status) {
				case '200':
					phaseOut($li, removeRequestCallback);
					break;
				default:
					debug('Error in review decline operation');
					debug(response);
				}
			});
		}
	}, '.review-decline');
	
	function removeRequestCallback() {
		var reviewRequestCount = $('li.review-request').length;
		if(reviewRequestCount == 0) {
			phaseOut($reviewqueue);
		}
	}
	
	function phaseOut(element, callback) {
		element.animate({
			opacity: 0
		}, 500, function(){
			element.hide('blind', {direction: 'vertical'}, 500, function(){
				element.remove();
				if(typeof callback == 'function') callback();
			});
		})
	}
});