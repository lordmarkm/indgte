<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../tiles/links.jsp" %>

<title>Review for ${review.revieweeSummary.title }</title>
<script type="text/javascript" src="${jsApplication }"></script>

<div class="grid_8 review">
	<div class="page-header">Review for ${review.revieweeSummary.title }</div>
	<section class="reviewee-summary">
		<img class="reviewee-img floatleft" src="${urlImgur }${review.revieweeSummary.thumbnailHash }s.jpg" />
		<div class="reviewee-info">
			<div><strong>${review.revieweeSummary.title }</strong></div>
			<div>${review.revieweeSummary.description }</div>
		</div>
	</section>
	
	<div class="clear"></div>
	
	<section class="review-proper">
		<div class="section-header">
			The Review (
				<span class="greentext"><span class="agreeCount">${review.agreeCount }</span> agree</span>, 
				<span class="redtext"><span class="disagreeCount">${review.disagreeCount }</span> disagree</span>
			)
		</div>
		<div class="review-text">${review.justification }</div>
	</section>
	
	<section class="reviewer-summary">
		<div class="reviewer-container">
			<div class="reviewer-img-container">
				<img class="reviewer-img" src="${review.reviewerSummary.thumbnailHash}" />
			</div>
			<div class="reviewer-info">
				<div>${review.reviewerSummary.title }</div>
				<div>${review.reviewerSummary.rank }</div>
			</div>
		</div>
	</section>	
	
	<div class="clear"></div>
	
	<section>
		<div class="section-header">React</div>
		<div class="react-agree-disagree hide"></div>
		<div class="react">
			<div>How do you feel about this review?</div>
			<div class="centercontent mt10">
				<button class="btn-react agree">I agree with this review</button>
				<button class="btn-react disagree">I disagree with this review</button>
			</div>
		</div>
	</section>
	
	<section>
		<div class="section-header">Comment</div>
		<div class="fb-comments" data-href="${domain}${urlReview }${review.reviewType }/${review.id}" data-width="600"></div>
	</section>
</div>

<style>
.review section:not(:first-child) {
	margin-top: 20px;
}

.reviewee-img {
	padding: 5px;
}

.reviewer-summary {
	text-align: right;
}
.reviewer-container {
	display: inline-block;
	text-align: left;
}
.reviewer-container > div {
	display: inline-block;
	vertical-align: text-top;
}
</style>

<script>
window.review = {
	agree: '${agree}' === 'true',
	disagree: '${disagree}' === 'true',
	agreeCount: '${review.agreeCount}',
	disagreeCount: '${review.disagreeCount}'
}

window.urls = {
	reviewAgree: '<spring:url value="/i/reviewreact/${review.reviewType}/agree/${review.id}.json" />',
	reviewDisagree: '<spring:url value="/i/reviewreact/${review.reviewType}/disagree/${review.id}.json" />'
}

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
});
</script>

<div id="fb-root"></div>
<script>(function(d, s, id) {
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "//connect.facebook.net/en_GB/all.js#xfbml=1&appId=270450549726411";
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));</script>