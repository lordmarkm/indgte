<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@include file="../tiles/links.jsp" %>

<link rel="stylesheet" href="<c:url value='/resources/css/businessops.css' />" />
<link rel="stylesheet" href="<c:url value='/resources/galleria/themes/classic/galleria.classic.css' />" />

<!-- WARN: Galleria is heavy! -->
<script src="http://ajax.cdnjs.com/ajax/libs/galleria/1.2.8/galleria.min.js"></script>

<title><spring:message code="product.title" arguments="${product.name },${business.domain }"/></title>

<div class="viewproduct grid_8">
	<section class="product-welcome">
		<div class="product-welcome-product">
			<div class="product-welcome-image-container">
				<img class="product-welcome-image" />
			</div>
			<h3>${product.name }</h3>
			<div class="product-welcome-description italic">${product.description }</div>
		</div>
		<div class="product-welcome-divider">&nbsp;</div>
		<div class="product-welcome-category">
			<a href="${urlCategories }${business.domain}/${product.category.id}"><img class="product-category-image" /></a>
			<div class="product-category-info">
				<h4><a href="${urlCategories }${business.domain}/${product.category.id}">${product.category.name }</a></h4>
				<div><spring:message code="generics.category" /></div>
			</div>
		</div>
		<div class="product-welcome-divider">&nbsp;</div>
		<div class="product-welcome-provider">
			<a href="${urlProfile }${business.domain}"><img class="product-provider-image" /></a>
			<div class="product-provider-info">
				<h4><a href="${urlProfile }${business.domain}">${business.fullName }</a></h4>
				<div><spring:message code="generics.provider" /></div>
			</div>
		</div>
	</section>
	
	<section>
		<div id="galleria"></div>
		<div class="centercontent">
			<button class="galleria-fullscreen">Fullscreen</button>
		</div>
	</section>
</div>

<!-- product owner controls -->
<c:if test="${owner }">
<div class="viewproduct-controls grid_4 ui-widget sidebar-section">
	<div class="sidebar-section-header">Product Owner Controls</div>
	<div class="product-owner-operations">
		<a href="${urlEditProduct}${business.domain}/${product.id}" class="button btn-edit-product"><spring:message code="generics.edit" /></a>
		<button class="btn-product-add-photo"><spring:message code="product.addphotos.button" /></button>
		<button class="btn-product-promote"><spring:message code="generics.promote" /></button>
	</div>
	<div class="sidebar-divider"></div>
</div>
<style>
.product-owner-operations .button, .product-owner-operations button {
	margin-top: 5px;
	width: 100%;
}
</style>
</c:if>
<!-- end product owner controls -->

<!-- product generic controls -->
<div class="viewproduct-controls grid_4 ui-widget sidebar-section">
	<div class="sidebar-section-header">Product Actions</div>
	<div class="product-actions">
		<button class="btn-wishlist-add">Add to Wishlist</button>
		<div class="fb-like" data-send="true" data-width="450" data-show-faces="true"></div>
	</div>
	<div class="sidebar-divider"></div>
</div>

<style>
#galleria {
	height: 500px;
	width: 500px;
	margin: 20px auto 20px;
}

.product-actions .button, .product-actions button {
	margin-top: 5px;
	width: 100%;
}

.fb-like {
	margin-top: 5px;
}
</style>

<c:if test="${not empty product.mainpic}">
	<c:set var="productPic" value="${product.mainpic.largeThumbnail }" />
	<c:set var="productPicImgur" value="${product.mainpic.imgurPage }" />
</c:if>
<c:if test="${not empty product.category.mainpic }">
	<c:set var="categoryPic" value="${product.category.mainpic.smallSquare }" />
	<c:set var="categoryPicImgur" value="${product.category.mainpic.imgurPage }" />
</c:if>
<c:if test="${not empty business.profilepic }">
	<c:set var="businessPic" value="${business.profilepic.smallSquare }" />
	<c:set var="businessPicImgur" value="${business.profilepic.imgurPage }" />
</c:if>
<script>
window.urls = {
	products : '${urlProducts}',
	profile : '${urlProfile}',
	noImage : '${noimage}',
	noImage50 : '${noimage50}',
	wishlist: '<spring:url value="/i/wishlist/product/" />'
}

window.constants = {
	domain: '${business.domain}',
	categoryId: '${product.category.id}',
	categoryName: '${product.category.name}',
	productId: '${product.id}',
	productName: '${product.name}',
	owner : '${owner}' === 'true',
	imgurKey : '${imgurKey}'
}

window.product = {
	id: '${product.id}',
	name: '${product.name}',
	categoryId: '${product.category.id}',
	imgur: {
		hash: '${product.mainpic.hash}',
		image: '${product.mainpic.largeThumbnail}',
		thumb: '${product.mainpic.smallSquare}',
		big: '${product.mainpic.original}',
		title: '${product.name}',
		link: '${product.mainpic.imgurPage}'
	}
}

$(function(){
	var	productPic = '${productPic}',
		productPicImgur = '${productPicImgur}',
		categoryPic = '${categoryPic}',
		categoryPicImgur = '${categoryPicImgur}',
		businessPic = '${businessPic}',
		businessPicImgur = '${businessPicImgur}';
	
	var $productPic = $('.product-welcome-image'),
		$categoryPic = $('.product-category-image'),
		$businessPic = $('.product-provider-image'),
		$btnWishlistAdd = $('.btn-wishlist-add');
	
	$productPic.attr('src', productPic ? productPic : urls.noImage);
	$categoryPic.attr('src', categoryPic ? categoryPic : urls.noImage50);
	$businessPic.attr('src', businessPic ? businessPic : urls.noImage50);
	
	$.get(urls.products + constants.domain + '/' + constants.productId + '/pics.json', function(response) {
		switch(response.status){
		case '200':
			var images = [];
			images.push(product.imgur);
			for(var i = 0, length = response.imgurs.length; i < length; i++) {
				var imgur = response.imgurs[i];
				images.push({
					image: imgur.largeThumbnail,
					thumb: imgur.smallSquare,
					big: imgur.original,
					title: imgur.title ? imgur.title : constants.productName,
					description: imgur.description ? imgur.description : 'Category: ' + constants.categoryName,
					link: imgur.imgurPage
				});
			}
			Galleria.loadTheme("<c:url value='/resources/galleria/themes/classic/galleria.classic.min.js' />");
			$('#galleria').galleria({
			    dataSource: images,
	            transition: 'slide',
	            transitionSpeed: 750,
	            imageCrop: true,
	            maxScaleRatio: 1,
	            overlayBackground: '#39561D',
	            height: 500,
	            width: 500,
	            extend: function(options) {
	                Galleria.get(0).$('info-link').click();
	            }
			});
			//Galleria.run('#galleria', {dataSource: images});
			break;
		default:
			debug('Error. response: ' + response);
		}
	});
	
	$('.galleria-fullscreen').click(function(){
		Galleria.get(0).enterFullscreen();
	});
	
	//wishlist
	$btnWishlistAdd.click(function(){
		$.post(urls.wishlist + product.id + '.json', function(response) {
			debug(response);
		});
	})
});
</script>

<c:if test="${owner }">
<!-- Image upload -->
<div id="image-upload" style="display: none;">
	<div class="form-pic">
		<div class="image-upload-dropbox">
			<span class="dropbox-message"><spring:message code="product.dropbox.message" /></span>
		</div>
		<div style="text-align: center;">
			<button class="dropbox-orclick"><spring:message code="product.dropbox.orclick" /></button>
		</div>
		<div class="image-upload-preview"></div>
		<input type="file" style="visibility: collapse; width: 0px;"class="image-upload-file" />
	</div>
	<div class="loading hide">
		<img src="${spinner }" />
	</div>
</div>

<div class="image-upload-error" style="display:none;">
	<span class="message"></span>
</div>

<style>
div[aria-labelledby="ui-dialog-title-image-upload"] a.ui-dialog-titlebar-close {
	display:none;	
}
.ui-button .ui-button-text {
	font-size: 10pt;
	line-height: 1.0;
}

.image-upload-dropbox {
	width: 150px;
	height: 150px;
	text-align: center;
	border: 1px dashed grey;
	display: table;
	margin: auto;
	margin-bottom: 5px;
	position: relative;
}
.image-upload-dropbox.hungry {
	border-color: green !important;
}

.dropbox-message {
	display: table-cell;
    vertical-align: middle;
}

.upload-preview-img {
	max-width: 50px;
	max-height: 50px;
	margin: 2px;
}

.galleria-info-text {
	background-color: grey;
	opacity: 0.8;
	filter:alpha(opacity=80); /* For IE8 and earlier */
}
.galleria-info-description {
	color: white !important;
}
</style>

<script>
$(function(){
	//WARNING: Owner-only script!
	var $addPhoto = $('#image-upload'),
		$dropbox = $('.image-upload-dropbox'),
		$btnAddPhoto = $('.btn-product-add-photo'),
		$uploadPreview = $('.image-upload-preview'),
		$fieldImageUpload = $('.image-upload-file'),
		$errorDialog = $('.image-upload-error');
	
	var waitingFiles = 0;
	
	//override jquery ui dialog defaults
	$.extend($.ui.dialog.prototype.options, {
	    modal: true,
	    resizable: false,
	    maxHeight: 250,
	    width:500,
		closeOnEscape: false,
		hide: {effect: "fade", duration: 200}
	});
	
	$btnAddPhoto.click(function(){
		$uploadPreview.html('');
		$addPhoto.find('.overlay').remove().end()
			.dialog({
				title: '<spring:message code="product.addphotos.title" arguments="${product.name}"/>',
				buttons: {
					'<spring:message code="generics.done" />' : function(){
						$addPhoto.dialog('close');
					}
				}
		});
	});
	
	$('.dropbox-orclick').click(function(){
		$fieldImageUpload.click();
	});
	
	$fieldImageUpload.change(function(){
		upload(this.files[0], uploadCallback);
	});
	
    $dropbox[0].ondragover = function(e) {
    	e.preventDefault();
    	$dropbox.addClass('hungry');
    }
    $dropbox[0].ondragleave = function(e) {
    	e.preventDefault();
    	$dropbox.removeClass('hungry');
    }
    $dropbox[0].ondrop = function(e) {
    	e.preventDefault(); 
    	$dropbox.removeClass('hungry');
    	for(var i = 0, length = e.dataTransfer.files.length; i < length; ++i) {
    		upload(e.dataTransfer.files[i], uploadCallback);
    	}
    }
    
    function uploadCallback(imgurResponse) {
    	$.post(urls.products + constants.domain + '/' + constants.productId + '/pics.json', 
    		{
    			hash: imgurResponse.upload.image.hash,
    			deletehash: imgurResponse.upload.image.deletehash
    		},
    		function(dgteResponse){
	    		switch(dgteResponse.status) {
	    		case '200':
	    			var imgur = dgteResponse.imgur;
	    			$('<img class="upload-preview-img">')
	    				.attr('src', imgur.smallSquare)
	    				.appendTo($uploadPreview);

	    			//push to gallery
	    			var galleria = Galleria.get(0).push({
						image: imgur.largeThumbnail,
						thumb: imgur.smallSquare,
						big: imgur.original,
						title: constants.productName,
						description: 'Category: ' + constants.categoryName,
						link: imgur.imgurPage
	    			});
	    			
	    			//show last image but give gallery some time to init
	    			setTimeout(function(){$('.galleria-image:last').click()}, 250);
	    			break;
	    		default:
	    			debug('Error: ' + dgteResponse);
    		}
    		
    		//remove overlay if all files are done processing
    		if(--waitingFiles === 0) {
				$dropbox.find('.overlay').remove();
			} else {
				debug('still ' + waitingFiles + ' files waiting to upload. overlay stays');
			}
    	});
    }
	
	function upload(file, onComplete) {
		++waitingFiles;
		if(!$dropbox.find('.overlay').length) {
			$('<div class="overlay">').appendTo($dropbox);
		}
		
	    if (!file || !file.type.match(/image.*/)) return;

	    var fd = new FormData();
	    fd.append("image", file);
	    fd.append("key", constants.imgurKey);
	    
	    var xhr = new XMLHttpRequest();
	    xhr.open("POST", "http://api.imgur.com/2/upload.json");
	    xhr.onload = function() {
	    	if(xhr.status != 200) {
	    		return xhr.onerror();
	    	}
	    	
	    	var response = JSON.parse(xhr.responseText);
	    	onComplete(response);
	    }
	    xhr.onerror = function() {
	    	var response = JSON.parse(xhr.responseText);
	    	if(response) {
	    		debug('Error ' + xhr.status + ' - ' + JSON.stringify(response));
	    		
	    		var message;
	    		if(response.error && response.error.message) {
	    			message = response.error.message + ' Please try again after 1 hour.';
	    		} else {
	    			message = 'Unspecified error. Please try again after 1 hour.';
	    		}
	    		
	    		if($errorDialog.is(':visible') === false) {
	    			$errorDialog.find('.message').text(message).end()
		    			.dialog({
		    				title: 'Does not compute !@#$)@',
		    				buttons: {
		    					'OK' : function(){
		    						$(this).dialog('close');
		    						$addPhoto.dialog('close');
		    					}
		    				}
		    			});
		    		
	    			$.get('http://api.imgur.com/2/credits.json', function(response){
	    				var resetMessage = '';
	    				if(response.credits) {
	    					resetMessage = 'Credits reset in ' + response.credits.refresh_in_secs + ' seconds.';
	    				} else {
	    					resetMessage = 'Credits reset in ' + $.parseJSON(response).credits.refresh_in_secs + ' seconds.';
	    				}
	    				$errorDialog.append('Imgur credits reset in ' + response.credits + ' seconds.');
	    			});
	    		}
	    	} else {
	    		debug('Error ' + xhr.status);
	    	}
	    }
		xhr.send(fd);		
	}
});
</script>
</c:if>

<!-- Top Tens -->
<div class="toptens-container grid_4 ui-widget sidebar-section">
<div class="sidebar-section-header">Top Tens</div>
<div class="toptens">
	Popular:
	<ul class="popular"></ul>
	Recent:
	<ul class="recent"></ul>
	
	<a href="<spring:url value='/i/toptens/' />">View all...</a>
</div>
</div>
<link rel="stylesheet" href="<spring:url value='/resources/css/grids/toptens.css' />" />
<script>
window.urls.topTens = '<spring:url value="/i/toptens.json" />',
window.urls.topTenLeader = '<spring:url value="/i/toptens/leader/" />',
window.urls.topTensPage = '<spring:url value="/i/toptens/" />'
</script>
<script src="${jsTopTens }"></script>

<spring:url var="jsApplication" value="/resources/javascript/application.js" />
<script type="text/javascript" src="${jsApplication }"></script>

<div id="fb-root"></div>
<script>(function(d, s, id) {
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "//connect.facebook.net/en_GB/all.js#xfbml=1&appId=270450549726411";
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));</script>