$(function(){
	var 
		$agreeDisagree = $('.react-agree-disagree'),
		$react = $('.react')
		$btnAgree = $('.btn-agree'),
		$btnDisagree = $('.btn-disagree'),
		$agreeCount = $('.agreeCount'),
		$disagreeCount = $('.disagreeCount'),
		initial = true;
	
	function updateCounts(review) {
		$agreeCount.text(review.agreeCount).dgteFadeIn();
		$disagreeCount.text(review.disagreeCount).dgteFadeIn();
	}
	
	function showAgree(review) {
		$react.hide();
		$agreeDisagree.hide().text('You agree with this review.');
		var $flip = $('<div class="link-disagree">').appendTo($agreeDisagree);
		$('<a>').attr('href', 'javascript:;').text('Disagree instead.').appendTo($flip);
		if(initial) {
			$agreeDisagree.show();
			initial = false;
		} else {
			$agreeDisagree.dgteFadeIn();
		}
		updateCounts(review);
	}
	
	function showDisagree(review) {
		$react.hide();
		$agreeDisagree.hide().text('You disagree with this review.');
		var $flip = $('<div class="link-agree">').appendTo($agreeDisagree);
		$('<a>').attr('href', 'javascript:;').text('Agree instead.').appendTo($flip);
		if(initial) {
			$agreeDisagree.show();
			initial = false;
		} else {
			$agreeDisagree.dgteFadeIn();
		}
		updateCounts(review);
	}
	
	function agreeDisagree(agree) {
		$.post(agree ? urls.reviewAgree : urls.reviewDisagree, function(response) {
			switch(response.status) {
			case '200':
				if(agree) {
					showAgree(response.review);
				} else {
					showDisagree(response.review);
				}
				break;
			default:
				debug('error reacting to review');
				debug(response);
			}
		});
	}
	
	$('.btn-react').click(function(){
		var agree = $(this).hasClass('agree');
		agreeDisagree(agree);
	});
	
	if(review.agree || review.disagree) {
		$react.hide();
		if(review.agree) {
			showAgree(review);
		} else {
			showDisagree(review);
		}
	}
	
	$(document).on({
		'click' : function(){
			agreeDisagree(true);
			$(this).hide();
		}
	}, '.link-agree')
	
	.on({
		'click' : function(){
			agreeDisagree(false);
			$(this).hide();
		}
	}, '.link-disagree');
	
	//delete
	var $btnDelete = $('.btn-delete-review');
	$btnDelete.click(function(){
		var $reviewDeleteDialog = $('<div>')
			.attr('title', 'Delete this review?')
			.html('Are you sure you want to delete this review? This cannot be undone.')
			.dialog({
				buttons: {
					'Yup': function(){
						$reviewDeleteDialog.parent().spinner();
						$.post(urls.deleteReview + review.id + '/json', function(response) {
							switch(response.status) {
							case '200':
								$reviewDeleteDialog.dialog('close');
								switch(review.type) {
								case 'business':
									window.location.replace(urls.business + review.revieweeIdentifier);
									break;
								case 'user':
									window.location.replace(urls.user + review.revieweeIdentifier);
									break;
								default:
									window.location.replace('/');
								}
								break;
							case '500':
								$reviewDeleteDialog.dialog('close');
								dgte.operationFailed();
								break;
							}
						}).error(function(){
							dgte.operationFailed()
						}).complete(function(){
							$reviewDeleteDialog.dialog('close');
						});
					},
					
					'Not really, no': function(){
						$(this).dialog('close');
					}
				}
			});
	});
});