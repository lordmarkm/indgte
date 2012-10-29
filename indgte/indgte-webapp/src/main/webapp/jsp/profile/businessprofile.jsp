<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="noimage" value="/resources/images/noimage.jpg" />
<spring:url var="noimage50" value="/resources/images/noimage50.png" />
<spring:url var="spinner" value="/resources/images/spinner.gif" />
<spring:url var="paperclip" value="/resources/images/icons/paperclip.png" />
<spring:url var="urlProfileRoot" value="/p/" />
<spring:url var="urlEdit" value="/r/edit/" />
<spring:url var="urlPosts" value="/i/posts/" />
<spring:url var="urlBusinessPost" value="/i/posts/business/" />
<spring:url var="urlCategories" value="/b/categories/" />
<spring:url var="urlCreateCategory" value="/b/newcategory/" />

<link rel="stylesheet" href="<spring:url value='/resources/css/businessprofile.css' />" />
<link rel="stylesheet" href="<spring:url value='/resources/css/lists.css' />" />
<link rel="stylesheet" href="<spring:url value='/resources/css/review.css' />" />

<script src="http://ajax.microsoft.com/ajax/jQuery.Validate/1.6/jQuery.Validate.min.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>

<spring:url var="jsApplication" value="/resources/javascript/application.js" />
<script type="text/javascript" src="${jsApplication }"></script>
<spring:url var="jsFeed" value="/resources/javascript/businessprofile/feed.js" />
<script type="text/javascript" src="${jsFeed }"></script>

<c:choose>
<c:when test="${not empty business.profilepic }">
<spring:url var="urlSmallProfilepic" value="http://i.imgur.com/${business.profilepic.hash }s.jpg" />
</c:when>
<c:otherwise>
<spring:url var="urlSmallProfilepic" value="/resources/images/noimage.jpg" />
</c:otherwise>
</c:choose>

<title>${business.fullName }</title>

<div class="cover grid_9">
	<c:if test="${not empty business.coverpic }">
	<a href="http://imgur.com/${business.coverpic.hash }"><img class="coverpic" src="http://i.imgur.com/${business.coverpic.hash }l.jpg" /></a>
	</c:if>
</div>

<div class="rightbar grid_3">&nbsp;
</div>

<div class="content grid_9">
<div class="profilepic-container grid_2">
	<div class="profilepic-inner-container">
		<c:choose>
		<c:when test="${not empty business.profilepic }">
		<a href="http://imgur.com/${business.profilepic.hash }"><img class="profilepic" src="${urlSmallProfilepic }" /></a>
		</c:when>
		<c:otherwise>
		<img class="profilepic" src="${noimage }" />
		</c:otherwise>
		</c:choose>
	</div>
	<div class="button btn-subscribe-toggle">Subscribe</div>
</div>

<div class="content-info">
	<h2 class="ui-widget-header fullname">${business.fullName }</h2>
	<div class="content-info-indented grid_6">
		<div style="text-transform: capitalize;">${business.category.name }</div>
		<div class="description">${business.description }</div>
		<div class="biz-url">www.indumaguete.com/p/${business.domain }</div>
		<div class="biz-owner"><a href="${urlProfileRoot}user/${business.owner.username}">${business.owner.username }</a></div>
		<c:if test="${owner }">
		<div class="business-owner-operations">
			<a class="button" href="${urlEdit }${business.domain}">Edit</a>
			<button class="btn-promote">Promote</button>
		</div>
		</c:if>
	</div>
</div>

<div class="tabs grid_9">
	<ul>
		<li><a href="#feed"><spring:message code="business.profile.tabs.feed" /></a></li>
		<li><a href="#catalog"><spring:message code="business.profile.tabs.products" /></a></li>
		<li><a href="#map">Map</a>
		<li><a href="#reviews"><spring:message code="business.profile.tabs.reviews" /></a></li>
		<li><a href="#comments"><spring:message code="business.profile.tabs.comments" /></a>
	</ul>
	
	<!-- Business's feed - shows posts by the business -->
	<div id="feed">
		<c:if test="${owner }">
		<div class="newpost">
			<form id="form-newpost">
				<div class="border-provider ui-state-active inline-block">
					<div class="status-title-container">
						<input name="title" class="status-title ui-state-active hide" maxlength="45" type="text" placeholder="title" />
					</div>
					<textarea name="text" class="status-textarea noattachment" maxlength="140" rows="1" placeholder="<spring:message code="home.status.textarea" />"></textarea>	
					<div class="attach-input-container">
						<!-- Hidden -->
						<input class="posterId" type="hidden" value="${business.id }" />
						<input class="posterType" type="hidden" value="business" />
						<input class="iptType" type="hidden" value="none"/>
						
						<!-- Image -->
						<input class="iptFile" type="file" />
						
						<!-- Video and link -->
						<input class="iptUrl" type="text" placeholder="Paste URL"/>
						
						<!-- Entity -->
						<input class="iptEntity" type="text" placeholder="Enter Entity name" />
						<div class="entity-preview hide"></div> 
						<div class="entity-suggestions ui-state-default"></div>
					</div>
				</div>
				<div class="newpost-errors"></div>
			</form>
			<div class="status-options hide">
				<sec:authorize access="hasRole('ROLE_USER_FACEBOOK')">
				<input type="checkbox" name="toFacebook" id="toFacebook" value="true"><label for="toFacebook"><spring:message code="home.status.postfb" /></label>
				</sec:authorize>
				<div class="floatright">
					<span class="status-counter"></span>
					<div class="post-as">
						<div class="menubutton post-as-visibles" title="Posting as ${business.fullName }">
							<c:if test="${not empty business.profilepic }">
							<img src="${business.profilepic.smallSquare }" />
							</c:if>
						</div>
					</div>
					<div class="attach">
						<div class="menubutton attach-visibles" title="Attach a product or image">
							<img src="${paperclip }" style="max-width: 15px; max-height: 15px;" />
							<div class="ui-icon ui-icon-triangle-1-s attach-triangle"></div>
						</div>
						<div class="attach-menu">
							<div class="attach-option image">
								<div class="name"><span class="ui-icon ui-icon-image inline-block mr5"></span>Image</div>
							</div>
							<div class="attach-option video">
								<div class="name"><span class="ui-icon ui-icon-video inline-block mr5"></span>Video</div>
							</div>
							<div class="attach-option link">
								<div class="name"><span class="ui-icon ui-icon-link inline-block mr5"></span>Link</div>
							</div>
							<div class="attach-option entity">
								<div class="name"><span class="ui-icon ui-icon-cart inline-block mr5"></span> Indgte Entity</div>
							</div>
							<div class="attach-option none">
								<div class="name"><span class="ui-icon ui-icon-cancel inline-block mr5"></span>None</div>
							</div>
						</div>			
					</div>
					<div class="button btn-post">Post</div>
				</div>
			</div>
		</div>
		</c:if>
	
		<div class="feed-container">
			<ul class="posts"></ul>
			<div class="loadmoreContainer" style="text-align: center; height: 100px; position: relative;">
				<button class="loadmore" style="width: 50%; margin-top: 50px;">Load 10 more</button>
			</div>
		</div>
	</div>
	
	<div id="catalog">
		<div class="catalog-container">
			<c:if test="${owner }">
			<div class="catalog-owner-container">
				<div class="catalog-new">
					<a class="dialog" href="${urlCreateCategory}${business.domain}"><spring:message code="business.newcategory.link" /></a>
				</div>
			</div>
			</c:if>	
			<div class="catalog-categories-container">
				<ul class="catalog-categories"></ul>
			</div>
		</div>
	</div>
	
	<div id="map">
		<div class="map"></div>
	</div>
	
	<div id="reviews">
	<div class="reviews-container">
		<div class="review-header">
			<div class="review-average"></div>
			<div class="review-count"></div>
		</div>
		
		<c:if test="${not owner }">
		<div class="review-container user-review ui-state-active">
			<div class="reviewer-container">
				<img class="reviewer-img" src="${user.imageUrl }" />
				<span class="reviewer-name">${user.username }</span>
			</div>
			<div class="review-star-container"></div>
			<div class="review-justification"></div>
			
			<div class="review-form-container">
				<form id="review-form">
					<textarea name="justification" class="justification" rows="3" cols="50" placeholder="Say something about ${business.fullName }"></textarea>
					<input class="review-score" type="hidden" name="score" />
					<div>
						<a class="review-submit button" href="javascript:;">Submit</a>
					</div>
				</form>
			</div>
		</div>
		</c:if>
		
		<div class="reviews-list"></div>
	</div>
	</div>
	
	<div id="comments">Comments</div>
</div>

</div>

<script>
window.constants = {
	owner : '${owner}' === 'true',
	imgurKey : '${imgurKey}'
}

window.urls = {
	subscribe : '<spring:url value="/i/subscribe/business/${business.id}.json" />',
	unsubscribe : '<spring:url value="/i/unsubscribe/business/${business.id}.json" />',
	getProducts : '<spring:url value="/b/products/" />',
	product : '<spring:url value="/b/products/" />',
	review: '<spring:url value="/i/review/" />',
	allReviews: '<spring:url value="/i/allreviews/" />',
	status : '<spring:url value="/i/newstatus.json" />',
	subposts : '<spring:url value="/i/subposts.json" />',
	postdetails : '<spring:url value="/i/posts/" />',
	linkpreview : '<spring:url value="/i/linkpreview/" />',
	user : '<spring:url value="/p/user/" />',
	business : '<spring:url value="/p/" />',
	category: '<spring:url value="/b/categories/" />',
	categoryWithProducts: '<spring:url value="/b/categories/" />',
	getproducts: '<spring:url value="/b/products/" />',
	productwithpics: '<spring:url value="/b/products/withpics/" />',
	imgur : 'http://i.imgur.com/',
	imgurPage : 'http://imgur.com/',
	searchOwn: '<spring:url value="/s/own/" />',
	
	//posts
	businessPosts: '<spring:url value="/i/posts/" />' //get the last posts by this business
}

window.user = {
	username : '${user.username}'
}

window.business = {
	id : '${business.id}',
	latitude: '${business.latitude}',
	longitude: '${business.longitude}',
	domain: '${business.domain}',
	fullName: '${business.fullName}',
	address: '${business.address}'
}

$(function(){
	var domain = '${business.domain}',
		fullName = '${business.fullName}',
		businessId = '${business.id}';
	
	var hasPic = '${not empty business.profilepic }' === 'true';
	
	var urlSubscribe = 
		urlPosts = '${urlPosts}',
		urlNoImage = '${noimage}',
		urlNoImage50 = '${noimage50}',
		urlSmallProfilepic = '${urlSmallProfilepic}',
		urlCategories = '${urlCategories}';

	//subscribe
	var subscribed = '${subscribed}' === 'true';
	var $btnSubscribe = $('.btn-subscribe-toggle');
	
	function refreshSubsButton() {
		if(subscribed) {
			$btnSubscribe
				.button({label: 'Subscribed', icons: {secondary: 'ui-icon-circle-check'}})
				.unbind('hover')
				.hover(function(){
					$btnSubscribe.button({label: 'Unsubscribe', icons: {}});
				}, function(){
					$btnSubscribe.button({label: 'Subscribed', icons:{secondary: 'ui-icon-circle-check'}});
				});
		} else {
			$btnSubscribe
				.button({label: 'Not subscribed', icons: {secondary: 'ui-icon-circle-close'}})				
				.unbind('hover')
				.hover(function(){
					$btnSubscribe.button({label: 'Subscribe', icons: {}});
				}, function(){
					$btnSubscribe.button({label: 'Not subscribed', icons:{secondary: 'ui-icon-circle-close'}});
				});
		}
	}
	refreshSubsButton();

	$btnSubscribe.click(function(){
		var url = subscribed ? urls.unsubscribe : urls.subscribe;
		$.post(url, function(response) {
			switch(response.status) {
			case '200':
				subscribed = !subscribed;
				refreshSubsButton();
				break;
			default:
				debug(response);
			}
		});
	});
	
	//feed
	var $feedContainer = $('.feed-container'),
		$posts = $('.feed-posts');
	
	//catalog
	var $catalogContainer = $('.catalog-container'),
		$categories = $('.catalog-categories');
	
	//tabs lol
	$('.tabs').tabs({
		select: function(event, ui){
			onTab(event, ui)
		},
		create: function(event, ui) {
			if(window.location.hash[1]) {
				setTimeout(function(){
					$('.tabs').tabs('select', parseInt(window.location.hash[1]));	
				}, 1000);
			}
		}
	});
	
	function onTab(event, ui) {
		debug('ontab. id: ' + ui.panel.id);
		switch(ui.panel.id) {
		case 'feed':
			//reloadPosts();
			window.location.hash = 0;
			break;
		case 'catalog':
			onCatalog();
			window.location.hash = 1;
			break;
		case 'map':
			onMap();
			window.location.hash = 2;
			break;
		case 'reviews':
			onReview();
			window.location.hash = 3;
			break;
		case 'comments':
			window.location.hash = 4;
			break;
		}
	}

	//handle the catalog tab
	var initialized = false;
	function onCatalog() {
		if(initialized) return;
		var $overlay = $('<div class="overlay">').appendTo($catalogContainer);
		$categories.html('');
		$.get(urlCategories + domain + '.json', function(response) {
			switch(response.status) {
			case '200':
				if(response.categories.length === 0) {
					$categories.append($('<span class="removeme">').text('No products or services yet for ' + fullName));
				} else {
					$.each(response.categories, function(i, category) {
						addCategory(category);
					});
				}
				break;
			default:
				debug(response);
			}
			
			$overlay.remove();
		});
		initialized = true;
	}
	
	function addCategory(category) {
		var $category = $('<li class="category">').appendTo($categories);

		//picture
		var $picContainer = $('<div class="category-pic-container">').appendTo($category);
		$('<img class="category-pic">').attr('src', category.mainpic ? category.mainpic.smallSquare : urlNoImage50).appendTo($picContainer);

		var $dataContainer = $('<div class="category-data-container">').appendTo($category);
		
		//title
		var $name = $('<strong class="category-name">').appendTo($dataContainer);
		$('<a>').attr('href', urlCategories + domain + '/' + category.id).html(category.name).appendTo($name);
		$('<div class="category-description italic">').text(category.description).appendTo($dataContainer);
		
		var $productsContainer = $('<div class="products-container">').appendTo($category);
		$.get(urls.getProducts + business.domain + '/' + category.id + '.json', function(response) {
			switch(response.status) {
			case '200':
				for(var i = 0, length = response.products.length; i < length; ++i) {
					var product = response.products[i];
					var $productContainer = $('<div class="product-container">').appendTo($productsContainer);
					var $a = $('<a>').attr('href', urls.product + product.id).appendTo($productContainer);
					$('<img class="product-preview-img shadow">').attr('src', product.mainpic ? product.mainpic.smallSquare : dgte.urls.blackSquareSmall)
						.attr('title', product.name)
						.appendTo($a);
					$('<div class="product-preview-name">').attr('title', product.name).text(product.name).appendTo($productContainer);
				}
				break;
			default:
				debug(response);
			}
		})
	}
	
	//map
	var $map = $('.map');
	var map,
		location = new google.maps.LatLng(business.latitude, business.longitude);
	
	function onMap() {
		if(~~business.latitude === 0.0) {
			$map.html('');
			$('<h5>').text('<spring:message code="business.nolatlng" arguments="' + business.fullName + '"/>').appendTo($map);
			$('<p>').html(business.address).appendTo($map);
			return;
		}
		
		if(!map) {
			initializeMap();
		} else {
			map.panTo(location);
		}
	}
	
	function initializeMap() {
		map = new google.maps.Map($map[0], {
			zoom: 15,
			scrollwheel: false,
			panControl: false,
			streetViewControl: false,
			center: location,
			mapTypeId: google.maps.MapTypeId.ROADMAP
		});
		
		var marker = new google.maps.Marker({
			map: map,
			position: location
		});
		
		var infowindow = new google.maps.InfoWindow({
			content: '<p><strong style="font-size: 1.1em;">' + business.fullName + '</strong></p>' + business.address
		});
		infowindow.open(map, marker);
	}
	
	
	//review
	var $reviews = $('.reviews-container'),
		$starContainer = $('.user-review .review-star-container'),
		$reviewJustification = $('.user-review .review-justification'),
		$reviewFormContainer = $('.review-form-container').hide(),
		$reviewForm = $('#review-form'),
		$iptJustification = $('textarea.justification'),
		$iptScore = $('.review-score'),
		$linkSubmit = $('.review-submit'),
		$reviewsList = $('.reviews-list');
	
	function makestars($container, score, preview) {
		$container.html('');
		if(typeof(score) == 'undefined') score = $container.attr('score');
		
		var stars =  Math.floor(score);
		var halfstar = Math.ceil(score - stars);
		var nullstars = dgte.review.max - (stars + halfstar);
		debug(stars + ',' + halfstar + ',' + nullstars);

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
	
	//all reviews of this business
	function addReview(review) {
		var $container = $('<div class="review-container user">').appendTo($reviewsList);
		//reviewer
		var $reviewerContainer = $('<div class="reviewer-container">').appendTo($container);
		$('<img class="reviewer-img">').attr('src', review.reviewer.imageUrl).appendTo($reviewerContainer);
		$('<span class="reviewer-name">').text(review.reviewer.username).appendTo($reviewerContainer);
		//data
		$detailsContainer = $('<div class="review-details-container">').appendTo($container)
		//stars
		var $stars = $('<div class="review-star-container">').appendTo($detailsContainer);
		makestars($stars, review.score);
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
			if(reviews[i].reviewer.username != user.username) {
				addReview(reviews[i]);
			} else {
				addUserReview(reviews[i]);
				userReviewed = true;
			}
		}
		if(!userReviewed) {
			makestars($starContainer, 0);
			$reviewJustification.text('Click on the stars to review ' + business.fullName);
		}
		
		$reviews.find('.overlay').delay(800).fadeOut(200, function() { $(this).remove(); });
	}
	
	var reviewsLoaded = false;
	function getAllRatings() {
		if(reviewsLoaded) return;
		var $overlay = $('<div class="overlay">').appendTo($reviews);

		$.get(urls.allReviews + business.id + '.json', function(response) {
			switch(response.status) {
			case '200':
				reviewsLoaded = true;
				processReviewSuccess(response);
				break;
			default:
				debug(response);
				$overlay.delay(800).fadeOut(200, function() { $(this).remove(); });
			}
		});
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
		$.post(urls.review + business.id + '.json', 
			
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
	
	function onReview() {
		getAllRatings();
	}
});
</script>

<c:if test="${owner }">
<!-- Image upload -->
<div id="profilepic-image-upload" style="display: none;">
	<div class="form-profilepic">
		<input class="profilepic-file" type="file" />
	</div>
	<div class="loading-profilepic hide">
		<img src="${spinner }" />
	</div>
</div>

<style>
div[aria-labelledby="ui-dialog-title-profilepic-image-upload"] a.ui-dialog-titlebar-close {
	display:none;	
}
</style>

<script>
$(function(){
	//WARNING: owner-only script!
	var domain = '${business.domain}',
		businessId = '${business.id}';

	var hasPic = '${not empty business.profilepic }' === 'true';

	var urlProfileRoot = '${urlProfileRoot}',
		urlSpinner = '${spinner}',
		urlPosts = '${urlPosts}',
		urlBusinessPost = '${urlBusinessPost}',
		urlSmallProfilepic = '${urlSmallProfilepic}';
	
	//image-upload vars
	var $upload = $('#profilepic-image-upload'),
		$uploadBtnUpload = $('.btn-profilepic-upload'),
		$uploadBtnCancel = $('.btn-profilepic-cancel'),
		$file = $('.profilepic-file'),
		$profilepic = $('.profilepic'),
		$cover = $('.cover'),
		$coverpic = $('.coverpic');
	
	//update cover
	$cover.unbind('click').click(function(){
		$upload.dialog({
			title: "<spring:message code='business.profile.coverpic.upload.title' /> ${business.fullName }",
			buttons: {
				"<spring:message code='business.profile.coverpic.upload.button' />" : function(){
					$upload.find('div').toggle();
					$coverpic.attr('src', urlSpinner);
					$upload.dialog('close');
					upload($file[0].files[0], urlProfileRoot + domain + '/coverpic');
				},
				
				"<spring:message code='business.profile.coverpic.upload.cancel' />" : function(){
					$upload.dialog('close');
				}
			}
		});
		return false;
	});
	
	//update profile pic
	$profilepic.unbind('click').click(function(){
		$upload.dialog({
			title: "<spring:message code='business.profile.profilepic.upload.title' /> ${business.fullName }",
			buttons: {
				"<spring:message code='business.profile.profilepic.upload.button' />" : function(){
					$upload.find('div').toggle();
					$profilepic.attr('src', urlSpinner);
					$upload.dialog('close');
					upload($file[0].files[0], urlProfileRoot + domain + '/profilepic');
				},
				
				"<spring:message code='business.profile.profilepic.upload.cancel' />" : function(){
					$upload.dialog('close');
				}
			}
		});
		return false;
	});
});
</script>
</c:if>