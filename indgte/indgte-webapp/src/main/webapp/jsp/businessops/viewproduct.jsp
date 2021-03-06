<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@include file="../tiles/links.jsp" %>

<link rel="stylesheet" href="<c:url value='/resources/css/businessops.css' />" />
<link rel="stylesheet" href="<c:url value='/resources/galleria/themes/classic/galleria.classic.css' />" />

<!-- WARN: Galleria is heavy! -->
<script src="http://ajax.cdnjs.com/ajax/libs/galleria/1.2.8/galleria.min.js"></script>

<title><spring:message code="product.title" arguments="${product.name },${business.domain }"/></title>

<div class="viewproduct grid_8 maingrid">
	<section class="product-welcome">
		<div class="product-welcome-product">
			<div class="product-welcome-image-container">
				<img class="product-welcome-image" />
			</div>
			<h3>
				${product.name }
				<c:if test="${product.soldout }"><span class="redtext">(Sold out)</span></c:if>
			</h3>
			<div class="product-welcome-description italic">${product.description }</div>
		</div>
		<div class="product-welcome-category">
			<a href="${urlCategories }${business.domain}/${product.category.id}"><img class="product-category-image" /></a>
			<div class="product-category-info">
				<h4><a href="${urlCategories }${business.domain}/${product.category.id}">${product.category.name }</a></h4>
				<div><spring:message code="generics.category" /></div>
			</div>
		</div>
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
	
	<section>
		<div class="section-header">Comments</div>
		<div class="mt5">
			<div class="fb-like" data-send="true" data-width="450" data-show-faces="true"></div>
		</div>
		<div class="fb-comments" data-href="${baseURL}/b/products/${product.category.business.domain}/${product.id}" data-width="620"></div>
	</section>
</div>

<!-- product owner controls -->
<c:if test="${owner }">
<div class="viewproduct-controls grid_4 ui-widget sidebar-section">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Product Owner Controls</div>
		<div class="product-owner-operations">
			<a href="${urlEditProduct}${business.domain}/${product.id}" class="button btn-edit-product"><spring:message code="generics.edit" /></a>
			<button class="btn-product-add-photo"><spring:message code="product.addphotos.button" /></button>
			<button class="btn-promote"><spring:message code="generics.promote" /></button>
			<div class="dialog-promote hide" title="Promote this Product">
				<span><spring:message code="entity.promote.dialog" arguments="${user.billingInfo.coconuts },${user.billingInfo.coconuts / 10 },${category.name }" /></span>
				<form class="form-promote" method="post" action="<c:url value='/o/sidebar/product/${product.id }' />" >
					<table>
						<tr>
							<td><label for="start-date">Promote from</label></td>
							<td><input type="text" id="start-date" name="start" readonly="readonly" placeholder="Click to choose" /></td>
						</tr>
						<tr>
							<td><label for="end-date">Promote until</label></td>
							<td><input type="text" id="end-date" name="end" readonly="readonly" placeholder="Click to choose"/></td>
						</tr>
					</table>
				</form>
				<span class="coconut-cost"><spring:message code="promote.dialog.comp" /></span>
			</div>
			<c:if test="${product.soldout }">
				<button class="btn-available">Mark as Available</button>
			</c:if>
			<c:if test="${!product.soldout }">
				<button class="btn-soldout">Mark as Sold out</button>
			</c:if>
			<button class="btn-delete">Delete product</button>
		</div>
	</div>
</div>
<script src="<spring:url value='/resources/javascript/promote.js' />" ></script>
<script src="http://ajax.aspnetcdn.com/ajax/jQuery.Validate/1.6/jQuery.Validate.min.js"></script>
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
	<div class="sidebar-container">
		<div class="sidebar-section-header">Product Actions</div>
		<div class="product-actions">
			<sec:authorize access="hasRole('ROLE_USER')">
			<c:if test="${!inwishlist }">
			<button class="btn-wishlist-add">Add to Wishlist</button>
			</c:if>
			</sec:authorize>
			<div class="fb-like" layout="button_count" data-send="true" data-width="450" data-show-faces="true"></div>
		</div>
	</div>
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
window.user = {
	coconuts : '${user.billingInfo.coconuts}'		
}

window.urls = {
	products : '${urlProducts}',
	profile : '${urlProfile}',
	noImage : '${noimage}',
	noImage50 : '${noimage50}',
	wishlist: '<spring:url value="/i/wishlist/product/" />',
	soldout: '<spring:url value="/b/products/soldout/" />',
	deleteProduct: '<spring:url value="/b/products/delete/" />',
	parentcategory: '${urlCategories }${business.domain}/${product.category.id}'
}

window.constants = {
	domain: '${business.domain}',
	categoryId: '${product.category.id}',
	categoryName: "<c:out value='${product.category.name}' />",
	productId: '${product.id}',
	productName: '<c:out value="${product.name}" />',
	owner : '${owner}' === 'true',
	imgurKey : '${imgurKey}'
}

window.product = {
	id: '${product.id}',
	name: '<c:out value="${product.name}" />',
	categoryId: '${product.category.id}',
	imgur: {
		hash: '${product.mainpic.hash}',
		image: '${product.mainpic.largeThumbnail}',
		thumb: '${product.mainpic.smallSquare}',
		big: '${product.mainpic.largeThumbnail}',
		title: '<c:out value="${product.name}" />',
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
		debug('add photo button clicked');
		$uploadPreview.html('');
		$addPhoto.find('.overlay').remove().end()
			.dialog({
				title: "<spring:message code='product.addphotos.title' arguments='${product.name}'/>",
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
	
	//sold out
	var 
		$btnSoldout = $('.btn-soldout'),
		$btnAvailable = $('.btn-available');
	
	function alertOperationFailed() {
		$('<div title="Operation failed">')
			.text('Operation failed. Please try again')
			.dialog({
				buttons: {
					'OK': function(){
						$(this).dialog('close');
					}	
				}
			});
	}
	
	function alertOperationSuccess(message, refresh) {
		$('<div title="Operation successful">')
			.text(message)
			.dialog({
				buttons: {
					'OK': function(){
						$(this).dialog('close');
						if(refresh) {
							window.location.reload();
						}
					}	
				}
			});
	}
	
	$btnSoldout.click(function(){
		
		$.post(urls.soldout + product.id + '/true/json', function(response){
			switch(response.status) {
			case '200':
				alertOperationSuccess('Item marked as sold out.', true);
				break;
			default:
				alertOperationFailed();
			}
		}).error(alertOperationFailed);

	});
	
	$btnAvailable.click(function(){
		
		$.post(urls.soldout + product.id + '/false/json', function(response){
			switch(response.status) {
			case '200':
				alertOperationSuccess('Item marked as available.', true);
				break;
			default:
				alertOperationFailed();
			}
		}).error(alertOperationFailed);
		
	});
	
	//delete
	var $btnDelete = $('.btn-delete');
	
	$btnDelete.click(function(){
		
		$('<div>')
			.attr('title', 'Really delete this product?')
			.text('Are you sure you want to delete this product? This cannot be undone.')
			.dialog({
				buttons: {
					'Yep': function(){
						$.post(urls.deleteProduct + product.id + '/json', function(response) {
							switch(response.status) {
							case '200':
								$('<div>')
									.attr('title', 'Product deleted')
									.text('Product has been deleted')
									.dialog({
										buttons: {
											'Ok': function(){
												window.location.replace(urls.parentcategory);
											}
										}
									});
								break;
							case '500':
								alertOperationFailed();
								break;
							default:
								debug(response);
							}
						}).error(alertOperationFailed);
					},
					
					'Not really, no': function(){
						$(this).dialog('close');
					}
				}
			})
		
	});
});
</script>
</c:if>

<!-- Notifications -->
<%@include file="../grids/notifications4.jsp"  %>

<!-- Similar Businesses -->
<%@include file="../grids/suggested4.jsp" %>

<!-- Top Tens -->
<%@include file="../grids/toptens.jsp" %>