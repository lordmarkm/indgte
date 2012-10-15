<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@include file="../tiles/links.jsp" %>

<link rel="stylesheet" href="<c:url value='/resources/css/businessops-view.css' />" />
<link rel="stylesheet" href="<c:url value='/resources/galleria/themes/classic/galleria.classic.css' />" />

<!-- WARN: Galleria is heavy! -->
<script src="http://ajax.cdnjs.com/ajax/libs/galleria/1.2.8/galleria.min.js"></script>

<title><spring:message code="product.title" arguments="${product.name },${business.domain }"/></title>

<div class="viewproduct grid_12">
	<section class="product-welcome">
		<div class="product-welcome-product">
			<div class="product-welcome-image-container">
				<img class="product-welcome-image" />
			</div>
			<h3>${product.name }</h3>
			<div class="product-welcome-description italic">${product.description }</div>
		</div>
		<c:if test="${owner }">
		<div class="product-owner-operations">
			<button class="btn-edit-product">Edit</button>
			<button class="btn-product-add-photo">Add Photo</button>
			<button class="btn-product-promote">Promote</button>
		</div>
		</c:if>
		<div class="product-welcome-divider">&nbsp;</div>
		<div class="product-welcome-category">
			<a href="${urlCategories }${business.domain}/${product.category.id}"><img class="product-category-image" /></a>
			<div class="product-category-info">
				<h4><a href="${urlCategories }${business.domain}/${product.category.id}">${product.category.name }</a></h4>
				<div>Category</div>
			</div>
		</div>
		<div class="product-welcome-divider">&nbsp;</div>
		<div class="product-welcome-provider">
			<a href="${urlProfile }${business.domain}"><img class="product-provider-image" /></a>
			<div class="product-provider-info">
				<h4><a href="${urlProfile }${business.domain}">${business.fullName }</a></h4>
				<div>Provider</div>
			</div>
		</div>
	</section>
	
	<section>
		<div id="galleria"></div>
	</section>
</div>

<style>
	#galleria{height:320px;}
</style>

<c:if test="${not empty product.mainpic}">
	<c:set var="productPic" value="${product.mainpic.smallSquare }" />
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
	noImage50 : '${noimage50}'
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

$(function(){
	var	productPic = '${productPic}',
		productPicImgur = '${productPicImgur}',
		categoryPic = '${categoryPic}',
		categoryPicImgur = '${categoryPicImgur}',
		businessPic = '${businessPic}',
		businessPicImgur = '${businessPicImgur}';
	
	var $productPic = $('.product-welcome-image'),
		$categoryPic = $('.product-category-image'),
		$businessPic = $('.product-provider-image');
	
	$productPic.attr('src', productPic ? productPic : urls.noImage);
	$categoryPic.attr('src', categoryPic ? categoryPic : urls.noImage50);
	$businessPic.attr('src', businessPic ? businessPic : urls.noImage50);
	
	$.get(urls.products + constants.domain + '/' + constants.productId + '/pics.json', function(response) {
		switch(response.status){
		case '200':
			var images = [];
			for(var i = 0, length = response.imgurs.length; i < length; i++) {
				var imgur = response.imgurs[i];
				images.push({
					image: imgur.largeThumbnail,
					thumb: imgur.smallSquare,
					big: imgur.original,
					title: constants.productName,
					description: 'Category: ' + constants.categoryName,
					link: imgur.imgurPage
				});
			}
			Galleria.loadTheme("<c:url value='/resources/galleria/themes/classic/galleria.classic.min.js' />");
			$('#galleria').galleria({
			    dataSource: images,
	            transition: 'slide',
	            transitionSpeed: 750,
	            autoplay: 2500,
	            imageCrop: true,
	            maxScaleRatio: 1,
	            overlayBackground: '#39561D',
	            height: 500,
	            width: 500
			});
			//Galleria.run('#galleria', {dataSource: images});
			break;
		default:
			debug('Error. response: ' + response);
		}
	});
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

<style>
div[aria-labelledby="ui-dialog-title-image-upload"] a.ui-dialog-titlebar-close {
	display:none;	
}
.ui-button .ui-button-text {
	font-size: 10pt;
	line-height: 1.0;
}

.product-owner-operations {
	margin: 10px 0 5px 178px;	
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
	border-color: red;
}

.dropbox-message {
	display: table-cell;
    vertical-align: middle;
}
</style>

<script>
$(function(){
	//WARNING: Owner-only script!
	var $addPhoto = $('#image-upload'),
		$dropbox = $('.image-upload-dropbox'),
		$btnAddPhoto = $('.btn-product-add-photo'),
		$uploadPreview = $('.image-upload-preview'),
		$fieldImageUpload = $('.image-upload-file');
	
	var waitingFiles = 0;
	
	//override jquery ui dialog defaults
	$.extend($.ui.dialog.prototype.options, {
	    modal: true,
	    resizable: false,
	    maxHeight: 250,
	    width:500,
		closeOnEscape: false,
		hide: {effect: "fade", duration: 500}
	});
	
	$btnAddPhoto.click(function(){
		$addPhoto.dialog({
			title: '<spring:message code="product.addphoto.title" arguments="${product.name}"/>',
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
	    			$('<img class="upload-preview-img">')
	    				.attr('src', dgteResponse.imgur.smallSquare)
	    				.appendTo($uploadPreview);
	    			break;
	    		default:
	    			debug('Error: ' + dgteResponse);
    		}
    		
    		//remove overlay if all files are done processing
    		console.debug('upload done. waiting files: ' + waitingFiles);
    		if(--waitingFiles === 0) {
				$dropbox.find('.overlay').remove();
			} else {
				debug('still ' + waitingFiles + ' waiting. overlay stays');
			}
    	});
    }
	
	function upload(file, onComplete) {
		++waitingFiles;
		if(!$dropbox.find('.overlay').length) {
			debug('appending overlay');
			$('<div class="overlay">').appendTo($dropbox);
		}
		
	    if (!file || !file.type.match(/image.*/)) return;

	    var fd = new FormData();
	    fd.append("image", file);
	    fd.append("key", constants.imgurKey);
	    
	    var xhr = new XMLHttpRequest();
	    xhr.open("POST", "http://api.imgur.com/2/upload.json");
	    xhr.onload = function() {
	    	var response = JSON.parse(xhr.responseText);
	    	onComplete(response);
	    }
		xhr.send(fd);		
	}
});
</script>
</c:if>

<spring:url var="jsApplication" value="/resources/javascript/application.js" />
<script type="text/javascript" src="${jsApplication }"></script>