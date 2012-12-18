<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@include file="../tiles/links.jsp" %>

<link rel="stylesheet" href="<c:url value='/resources/css/businessops.css' />" />

<title><spring:message code="category.title" arguments="${category.name },${business.domain }"/></title>

<div class="category-container grid_8 maingrid">
	<section class="category-welcome">
		<div class="category-welcome-category">
			<div class="category-welcome-image-container">
				<img class="category-welcome-image" />
			</div>
			<h3>${category.name }</h3>
			<div class="category-welcome-description italic">${category.description }</div>
		</div>
		<c:if test="${owner }">
		<div class="category-owner-operations">
			<button class="btn-edit-category">Edit</button>
			<button class="btn-create-product">New product</button>
			<button class="btn-promote">Promote</button>
		</div>
		</c:if>
		<div class="category-welcome-provider">
			<img class="category-provider-image" />
			<div class="category-provider-info">
				<h4><a href="${urlProfile }${business.domain}">${business.fullName }</a></h4>
				<div>Provider</div>
			</div>
		</div>
	</section>
	
	<section class="summary-container">
		<h5>Products - Summary</h5>
		<ul class="products-summary"></ul>
	</section>
	
	<div class="products-container">
		<ol class="products"></ol>
	</div>
</div>

<c:if test="${not empty category.mainpic }">
	<c:set var="categoryPic" value="${category.mainpic.largeThumbnail }" />
	<c:set var="categoryPicImgur" value="${category.mainpic.imgurPage }" />
</c:if>
<c:if test="${not empty business.profilepic }">
	<c:set var="businessPic" value="${business.profilepic.smallSquare }" />
	<c:set var="businessPicImgur" value="${business.profilepic.imgurPage }" />
</c:if>
<script>
window.urls = {
	urlProducts : '${urlProducts}'	
}

window.constants = {
	domain : '${business.domain}',
	previewPicsCount : 5
}

window.products = {
	domain : '${business.domain}',
	$summary : $('.products-summary'),
	$products : $('.products-container'),
	addProduct : function (product, $parent, prepend) {
		if(!$parent) {
			$parent = this.$products;
		}
		var $summaryItem = $('<li>').appendTo(this.$summary);
		$('<a href="#' + product.id + '">').text(product.name).appendTo($summaryItem);
		
		var $actualItem = $('<li class="product">')
			.on('mouseover', function(){$(this).addClass('ui-state-highlight');})
			.on('mouseout', function(){$(this).removeClass('ui-state-highlight');})
			.appendTo(this.$products);
		var $container = $('<div class="product-container">').appendTo($actualItem);

		if(product.mainpic) {
			var $picContainer = $('<div class="product-pic-container">').appendTo($container);
			$('<img class="product-pic">').attr('src', product.mainpic.smallSquare).appendTo($picContainer);
		}
		
		var $title = $('<a id="' + product.id + '">').attr('href', urls.urlProducts + this.domain + '/' + product.id).appendTo($container);
		var $h3 = $('<h3 class="product-name">').text(product.name).appendTo($title)
		
		$.get(urls.urlProducts + constants.domain + '/' + product.id + '/pics/' + constants.previewPicsCount + '.json', function(response){
			switch(response.status){
			case '200':
				for(var i = 0; i < response.imgurs.length; ++i) {
					$('<img class="product-otherpics-preview">').attr('src', response.imgurs[i].smallSquare).appendTo($h3);
				}
				break;
			default:
				debug('Error: ' + response);
			}
		});

		var maxdesc = 240;
		var desc = product.description.length > maxdesc ? product.description.substring(0, maxdesc) + '...' : product.description;
		$('<div>').html(desc).appendTo($container);
	}
}

$(function(){
	var categoryId = '${category.id}',
		categoryPic = '${categoryPic}',
		categoryPicImgur = '${categoryPicImgur}',
		domain = '${business.domain}',
		businessPic = '${businessPic}',
		businessPicImgur = '${businessPicImgur}',
		owner = '${owner}' === 'true';
	
	var urlProfile = '${urlProfile}',
		urlProducts = '${urlProducts}';
		
	var noImage = '${noimage}',
		noImage50 = '${noimage50}';
	
	var $categoryPic = $('.category-welcome-image'),
		$businessPic = $('.category-provider-image'),
		$summaryContainer = $('.summary-container'),
		$summary = $('.products-summary'),
		$products = $('.products');
	
	$categoryPic.attr('src', categoryPic ? categoryPic : noImage);
	$businessPic.attr('src', businessPic ? businessPic : noImage50);
	
	if(!owner) {
		if(categoryPic) {
			$categoryPic.click(function(){
				window.location.href = categoryPicImgur;
			});
		} else {
			$categoryPic.css('cursor', 'default');
		}
	}
	
	$businessPic.click(function(){
		window.location.href = urlProfile + domain;
	});
	
	$.get(urlProducts + domain + '/' + categoryId + '/json', function(response){
		switch(response.status){
		case '200':
			if(response.products.length < 2) {
				$summaryContainer.hide();
			}
			for(var i = 0, length = response.products.length; i < length; i++) {
				products.addProduct(response.products[i]);
			}
			break;
		default:
			debug(response);
		}
	});
});
</script>

<c:if test="${owner }">
<!-- Image upload -->
<div id="image-upload" style="display: none;">
	<div class="form-pic">
		<input class="image-upload-file" type="file" />
	</div>
	<div class="loading hide">
		<img src="${spinner }" />
	</div>
</div>

<div class="newproduct" style="display: none;">
	<div class="newproduct-pic-container">
		<img src="${noimage }" class="newproduct-pic-display" />
		<input type="file" class="newproduct-pic" />
	</div>
	<div class="newproduct-form">
		<div class="newproduct-field">Name</div>
		<input type="text" class="newproduct-name" />
		<div class="newproduct-field">Description</div>
		<textarea class="newproduct-description" rows="3"></textarea>
		<div class="newproduct-field">Main pic</div>
		<button class="btn-newproduct-pic">Choose</button>
		<input type="hidden" class="newproduct-pic-hash" />
		<input type="hidden" class="newproduct-pic-deletehash" />
	</div>
</div>

<style>
div[aria-labelledby="ui-dialog-title-image-upload"] a.ui-dialog-titlebar-close {
	display:none;	
}

.category-owner-operations {
	margin: 10px 0 5px 178px;	
}

.newproduct-pic-container {
	float: left;
	width: 250px;
	height: 250px;
	border: 1px dotted grey;
	text-align: center;
	position: relative;
}

.newproduct-pic-display {
	vertical-align: middle;
	max-width: 230px;
	max-height: 230px;
	margin: 10px;
	cursor: pointer;
}

.newproduct-form {
	margin-left: 270px;
}

.newproduct-form button {
	margin-top: 5px;
}

.newproduct-field {
	font-size: 0.8em;
	margin-top: 5px;
}

.newproduct-pic {
	left: 0;
	top: 0;
	position: absolute;
    opacity:0;
    -ms-filter:"progid:DXImageTransform.Microsoft.Alpha(Opacity=0)";
    filter: alpha(opacity=0);
}
</style>

<script>
$(function(){
	//WARNING: Owner-only script!
	var businessDomain = '${business.domain}',
		categoryName = '${category.name}',
		categoryId = '${category.id}';
	
	var categoryPic = '${categoryPic}',
		categoryPicImgur = '${categoryPicImgur}',
		owner = '${owner}' === 'true';

	var urlSpinner = '${spinner}',
		urlCategories = '${urlCategories}',
		urlNewProduct = '${urlNewProduct}';	
		
	var $categoryPicContainer = $('.category-welcome-image-container'),
		$categoryPic = $('.category-welcome-image'),
		$btnCreateProduct = $('.btn-create-product'),
		$createProduct = $('.newproduct');
	
	var $upload = $('#image-upload'),
		$file = $('.image-upload-file'),
		$newProductName = $('.newproduct-name'),
		$newProductDescription = $('.newproduct-description'),
		$btnNewproductPic = $('.btn-newproduct-pic'),
		$newproductPicContainer = $('.newproduct-pic-container'),
		$newproductPicDisplay = $('.newproduct-pic-display'),
		$newproductPicField = $('.newproduct-pic'),
		$newproductHashField = $('.newproduct-pic-hash'),
		$newproductDeletehashField = $('.newproduct-pic-deletehash');
	
	//override jquery ui dialog defaults
	$.extend($.ui.dialog.prototype.options, {
	    modal: true,
	    resizable: false,
	    maxHeight: 250,
	    width:500,
		closeOnEscape: false,
		hide: {effect: "fade", duration: 200}
	});
	
	//update category main pic
	$categoryPic.unbind('click').click(function(){
		$upload.dialog({
			title: "<spring:message code='category.mainpic.upload.title' />" + categoryName,
			buttons: {
				"<spring:message code='category.mainpic.upload.button' />" : function(){
					$upload.find('div').toggle();
					$categoryPicContainer.append($('<div class="overlay">'));
					$upload.dialog('close');
					//upload($file[0].files[0], urlCategories + businessDomain + '/' + categoryId + '/mainpic/', $categoryPic);
					
					var updatePhoto = function(response) {
						$.post(urlCategories + businessDomain + '/' + categoryId + '/mainpic/',
								{
									hash: response.upload.image.hash,
		        					deletehash: response.upload.image.deletehash
		        				},
								function(){
									$categoryPic.attr('src', response.upload.links.large_thumbnail);
									$categoryPicContainer.find('.overlay').remove();
									$upload.find('div').toggle();
								}
						);
					}
					upload($file[0].files[0], updatePhoto, $categoryPic);
				},
				
				"<spring:message code='category.mainpic.upload.cancel' />" : function(){
					$upload.dialog('close');
				}
			}
		});
		return false;
	});
	
	//create new product
	$btnCreateProduct.click(function(){
		$createProduct.dialog({
			title: '<spring:message code="category.newproduct.title" arguments="${category.name}" />',
			width: 600,
			buttons: {
				"<spring:message code='category.newproduct.save' />" : function(){
					var hash = $newproductHashField.val();
					var data = JSON.stringify({
						name: $newProductName.val(),
						description: $newProductDescription.val(),
						mainpic: hash ? {
							hash: hash,
							deletehash: $newproductDeletehashField.val(),
							uploaded: new Date()
						} : null
					});
					debug('data: ' + data);
					$.ajax({
							url: urlNewProduct + businessDomain + '/' + categoryId + '.json', 
							type: 'post',
							data: data, 
							success: function(response){
								switch(response.status) {
								case '200':
									products.addProduct(response.product, true);
									$createProduct.dialog('close');
									break;
								default:
									debug(response);
								}
							},
							dataType: 'json',
							contentType: 'application/json'
					});
				},
				"<spring:message code='category.newproduct.cancel' />" : function() {
					$createProduct.dialog('close');
				}
			}
		});
	});
	
	$btnNewproductPic.click(function(){
		$newproductPicField.focus().trigger('click');
	});
	$newproductPicDisplay.click(function(){
		$newproductPicField.focus().trigger('click');
	})
	
	$newproductPicField.change(function(){
		$newproductPicContainer.append($('<div class="overlay">'));
		var productFieldOnComplete = function(response){
			$newproductHashField.val(response.upload.image.hash);
			$newproductDeletehashField.val(response.upload.image.deletehash);
			$newproductPicDisplay.attr('src', response.upload.links.large_thumbnail);
			$newproductPicContainer.find('.overlay').remove();
		}
		upload($newproductPicField[0].files[0], productFieldOnComplete, $newproductPicDisplay);
	});
	
	function upload(file, onComplete, $target) {
	    if (!file || !file.type.match(/image.*/)) return;

	    var fd = new FormData();
	    fd.append("image", file);
	    fd.append("key", "${imgurKey}");
	    
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