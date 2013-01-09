<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@include file="../tiles/links.jsp" %>

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

<script src="http://ajax.aspnetcdn.com/ajax/jQuery.Validate/1.6/jQuery.Validate.min.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>

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

<div class="businessprofile grid_9 maingrid">
	<div class="cover">
		<c:if test="${not empty business.coverpic }">
		<a href="http://imgur.com/${business.coverpic.hash }"><img class="coverpic" src="http://i.imgur.com/${business.coverpic.hash }l.jpg" /></a>
		</c:if>
	</div>
	
	<div class="content">
		<div class="profilepic-container">
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
		</div>
		
		<div class="content-info">
			<h2 class="ui-widget-header fullname">${business.fullName }</h2>
		</div>		
		
		<div class="tabs">
			<ul>
				<li><a href="#business-info">${business.fullName }</a>
				<li><a href="#feed"><spring:message code="business.profile.tabs.feed" /></a></li>
				<li><a href="#catalog"><spring:message code="business.profile.tabs.products" /></a></li>
				<li><a href="#map">Map</a>
				<li><a href="#reviews"><spring:message code="business.profile.tabs.reviews" /></a></li>
				<li><a href="#comments"><spring:message code="business.profile.tabs.comments" /></a>
			</ul>
			
			<div id="business-info">
				<%@include file="./businessinfo.jsp"  %>
			</div>
			
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
								<div class="link-preview-container ui-state-default">
									<div class="link-preview-images"></div>
									<div class="inline-block">
										<div class="link-preview-title"></div>
										<div class="link-preview-description"></div>
										<div class="link-preview-url"></div>
									</div>
								</div>
																
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
					<div class="review-header-message"></div>
					<span class="review-count"></span>
					<span class="review-average-score"></span>
				</div>
				
				<sec:authorize access="hasRole('ROLE_USER')">
				<c:if test="${not owner }">
				<div class="review-container user-review ui-state-highlight">
					<div class="reviewer-container">
						<img class="reviewer-img" src="${user.imageUrl }" />
						<span class="reviewer-name">${user.username }</span>
						<div class="review-star-container"></div>
					</div>
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
				</sec:authorize>
				
				<div class="reviews-list"></div>
			</div>
			</div>
			
			<div id="comments">
				<div class="fb-comments" data-href="${baseURL}/${business.domain}" data-width="660"></div>
			</div>
		</div>
		
		</div>
</div>

<c:if test="${owner }">
	<div class="grid_3 sidebar-section owner-operations">
		<div class="sidebar-container">
			<div class="sidebar-section-header">Page Owner Operations</div>
			<a class="button" href="${urlEdit }${business.domain}">Edit</a>
			<a class="button btn-promote" href="javascript:;">Promote</a>
		</div>
	</div>
	<div class="dialog-promote hide" title="Promote this business">
		<span><spring:message code="entity.promote.dialog" arguments="${user.billingInfo.coconuts },${user.billingInfo.coconuts / 10 },${business.fullName }" /></span>
		<form class="form-promote" method="post" action="<c:url value='/o/sidebar/business/${business.id }' />" >
			<table>
				<tr>
					<td><label for="start-date">Promote from</label></td>
					<td><input type="date" id="start-date" name="start" readonly="readonly" placeholder="Click to choose" /></td>
				</tr>
				<tr>
					<td><label for="end-date">Promote until</label></td>
					<td><input type="date" id="end-date" name="end" readonly="readonly" placeholder="Click to choose"/></td>
				</tr>
			</table>
		</form>
		<span class="coconut-cost"><spring:message code="promote.dialog.comp" /></span>
	</div>
	<script src="<c:url value='/resources/javascript/promote.js' />" ></script>
</c:if>

<div class="grid_3 sidebar-section">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Interact</div>
		<sec:authorize access="hasRole('ROLE_USER')">
			<div class="button btn-subscribe-toggle">Subscribe</div>
		</sec:authorize>
		<div class="fb-like" layout="button_count" href="${baseURL }/${business.domain}" data-send="true" data-width="450" data-show-faces="true"></div>
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
	review: '<spring:url value="/i/review/business/" />',
	allReviews: '<spring:url value="/i/allreviews/business/" />',
	status : '<spring:url value="/i/newstatus.json" />',
	subposts : '<spring:url value="/i/subposts.json" />',
	postdetails : '<spring:url value="/i/posts/" />',
	linkpreview : '<spring:url value="/i/linkpreview/" />',
	user : '<spring:url value="/p/user/" />',
	business : '<spring:url value="/" />',
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
	id: '${user.id}',
	username: '${user.username}',
	rank: '${user.rank}',
	coconuts: '${user.billingInfo.coconuts}'
}

window.business = {
	id : '${business.id}',
	latitude: '${business.latitude}',
	longitude: '${business.longitude}',
	domain: '${business.domain}',
	fullName: '${business.fullName}',
	address: '${business.address}'
}

window.reviewconstants = {
	target: {
		id: '${business.id}',
		name: '${business.fullName}'
	}
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
			break;
		case 'catalog':
			onCatalog();
			break;
		case 'map':
			onMap();
			break;
		case 'reviews':
			break;
		case 'comments':
			break;
		}
	}

	//handle the catalog tab
	var initialized = false;
	function onCatalog() {
		if(initialized) return;
		var $overlay = $('<div class="overlay">').appendTo($catalogContainer);
		$categories.html('');
		$.get(urlCategories + domain + '/json', function(response) {
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
		$.get(urls.getProducts + business.domain + '/' + category.id + '/json', function(response) {
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

	var urlProfileRoot = '${urlProfileRoot}', //no /p/
		urlProfile = '${urlProfile}', //has /p/
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
	
	function upload(file, notifyUrl) {
	    if (!file || !file.type.match(/image.*/)) return;

	    var fd = new FormData();
	    fd.append("image", file);
	    fd.append("key", "${imgurKey}");
	    
	    var xhr = new XMLHttpRequest();
	    xhr.open("POST", "http://api.imgur.com/2/upload.json");
	    xhr.onload = function() {
	    	var response = JSON.parse(xhr.responseText);
	        
	        //notify indgte that profilepic has changed
	        $.get(response.upload.links.small_square);
	        setTimeout(function(){
	        	$.post(notifyUrl, 
		        		{
		        			hash: response.upload.image.hash,
		        			deletehash: response.upload.image.deletehash
		        		}
		        		, function(){
		        	//only now do we update the user's profile pic
	        		window.location.replace(urlProfile + domain);
		        });
	        }, 2000);
	    }
		xhr.send(fd);
	}
});
</script>
</c:if>

<script src="${jsReviews }"></script>

<!-- Notifications -->
<%@include file="../grids/notifications3.jsp"  %>

<!-- Simillar Businesses -->
<%@include file="../grids/suggested3.jsp" %>