$(function(){
		//review
	var $reviews = $('.reviews-container'),
		$starContainer = $('.user-review .review-star-container'),
		$reviewJustification = $('.user-review .review-justification'),
		$reviewFormContainer = $('.review-form-container').hide(),
		$reviewForm = $('#review-form'),
		$iptJustification = $('textarea.justification'),
		$iptScore = $('.review-score'),
		$linkSubmit = $('.review-submit'),
		$reviewsList = $('.reviews-list'),
		$reviewHeaderMessage = $('.review-header-message'),
		$averageScore = $('.review-average-score'),
		$reviewCount = $('.review-count');
	
	var reviewsLoaded = false;
	function loadReviews() {
		if(!valid()) return;
		if(reviewsLoaded) return;
		var $overlay = $('<div class="overlay">').appendTo($reviews);

		$.get(urls.allReviews + reviewconstants.target.id + '.json', function(response) {
			switch(response.status) {
			case '200':
				reviewsLoaded = true;
				if(!response.averageScore || response.reviewCount == 0) {
					$reviewHeaderMessage.text('No reviews yet for ' + reviewconstants.target.name);
					$averageScore.hide();
					$reviewCount.hide();
				} else {
					$reviewHeaderMessage.hide();
					$averageScore.text('Average score: ' + response.averageScore);
					$reviewCount.text('Reviews: ' + response.reviewCount);
				}
				processReviewSuccess(response);
				break;
			default:
				debug(response);
				$overlay.delay(800).fadeOut(200, function() { $(this).remove(); });
			}
		});
	}
	loadReviews();
	
	function valid() {
		if($reviews.length == 0) {
			debug('Reviews container not available');
			return false;
		}
		if(!urls || !urls.review || !urls.allReviews) {
			debug('One or more review URLs not available.');
			return false;
		}
		if(!reviewconstants) {
			debug('Reviews object not available.');
			return false;
		}
		if(typeof user == 'undefined' || !user) {
			debug('User info not available.');
			return false;
		}
		return true;
	}
	
	function makestars($container, score, preview) {
		$container.html('');
		if(typeof(score) == 'undefined') score = $container.attr('score');
		
		var stars =  Math.floor(score);
		var halfstar = Math.ceil(score - stars);
		var nullstars = dgte.review.max - (stars + halfstar);

		for(var i = 0; i < stars; ++i) {
			$('<div class="review-star dgte-icon dgte-icon-star">').text(' ').appendTo($container);
		}
		
		if(halfstar) {
			$('<div class="review-star dgte-icon dgte-icon-halfstar">').text(' ').appendTo($container);
		}
	
		for(var i = 0; i < nullstars; ++i) {
			$('<div class="review-star dgte-icon dgte-icon-nullstar">').text(' ').appendTo($container);
		}
		
		if(!preview) {
			$container.attr('score', score);
		}
	}
	
	//all reviews of the entity
	function addReview(review) {
		var $container = $('<div class="review-container user">').appendTo($reviewsList);
		//reviewer
		var $reviewerContainer = $('<div class="reviewer-container">').appendTo($container);
		$('<img class="reviewer-img">').attr('src', review.reviewerSummary.thumbnailHash).appendTo($reviewerContainer);
		var $reviewername = $('<div class="reviewer-name">').appendTo($reviewerContainer);
		$('<a>').attr('href', urls.user + review.reviewerSummary.identifier).text(review.reviewerSummary.identifier).appendTo($reviewername);
		$('<div class="reviewer-rank">').text(review.reviewerSummary.rank).appendTo($reviewerContainer);
		//data
		$detailsContainer = $('<div class="review-details-container">').appendTo($container)
		//stars and +/-
		var $stars = $('<div class="review-star-container">').appendTo($detailsContainer);
		makestars($stars, review.score);
		var $agreeDisagree = $('<div class="agree-disagree-preview-container">').appendTo($stars);
		if(review.agreeCount) $('<span class="agree-disagree-preview greentext">').text(review.agreeCount + ' agree').appendTo($agreeDisagree);
		if(review.disagreeCount) $('<span class="agree-disagree-preview redtext">').text(review.disagreeCount + ' disagree').appendTo($agreeDisagree);
		
		//justification
		$('<div class="review-justification">')
			.html(review.justification.length > dgte.review.previewChars ? 
					review.justification.substring(0, dgte.review.previewChars) + '...' 
					: review.justification).appendTo($detailsContainer);
		
		var $footer = $('<div class="review-footer">').appendTo($container)
		$('<a>').attr('href', urls.review + review.id).text('Full review and comments').appendTo($footer);
		$('<div class="review-date">').text(moment(review.time).format('LL')).appendTo($footer);
	}
	
	var userReviewed = false;
	function addUserReview(review) {
		makestars($starContainer, review.score);
		$reviewJustification.html(review.justification);
		$iptJustification.val(removeBrs(review.justification));
	}
	
	function processReviewSuccess(response) {
		$reviewsList.html('');
		var reviews = response.reviews;
		for(var i = 0, len = reviews.length; i < len; ++i) {
			if(reviews[i].reviewerSummary.identifier != user.username) {
				addReview(reviews[i]);
			} else {
				addUserReview(reviews[i]);
				userReviewed = true;
			}
		}
		if(!userReviewed) {
			makestars($starContainer, 0);
			$reviewJustification.text('Click on the stars to review ' + reviewconstants.target.name);
		}
		
		$reviews.find('.overlay').delay(800).fadeOut(200, function() { $(this).remove(); });
	}
	
	function reviewMode() {
		$reviewJustification.hide();
		$reviewFormContainer.show();
		$iptJustification.focus();
	}
	
	function reviewDone() {
		$reviewJustification.show();
		$reviewFormContainer.hide();
	}
	
	$('.user-review').on({
		mouseenter: function(){
			var $this = $(this);
			$this.removeClass('dgte-icon-nullstar').removeClass('dgte-icon-halfstar').addClass('dgte-icon-star');
			$this.prevAll().removeClass('dgte-icon-nullstar').removeClass('dgte-icon-halfstar').addClass('dgte-icon-star');
			$this.nextAll().removeClass('dgte-icon-star').addClass('dgte-icon-nullstar');
		},
		mouseleave: function(){
			makestars($starContainer);
		},
		click: function() {
			var score = $(this).index() + 1;
			makestars($starContainer, score);
			$iptScore.val(score);
			reviewMode();
		}
	}, '.review-star');
	
	$reviewForm.validate({
		rules: {
			justification : {
				required: true
			},
			score : {
				required: true
			}
		}
	});
	
	$linkSubmit.click(function(){
		if(!$reviewForm.valid()) return;
		$.post(urls.review + reviewconstants.target.id + '.json', 
			
			{score: $iptScore.val(), justification: $iptJustification.val()}, 
			
			function(response) {
				switch(response.status) {
				case '200':
					$reviewJustification.html(response.review.justification);
					reviewDone();
					break;
				default:
					debug(response);
				}
			}		
		);
	});
});