<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="../tiles/links.jsp" %>

<link rel="stylesheet" href="<c:url value='/resources/css/businessops.css' />" />

<title><spring:message code="product.edit.title" arguments="${product.name }"/></title>

<div class="editproduct grid_12">
	<section class="product-welcome">
		<div class="product-welcome-product">
			<div class="product-welcome-image-container">
				<img class="product-welcome-image" />
			</div>
			<h3><a href="${urlProducts }${business.domain}/${product.id}">${product.name }</a></h3>
			<div class="product-welcome-description italic">
				<c:choose>
				<c:when test="${fn:length(product.description) > 100 }">
					${fn:substring(product.description, 0, 100) }... <spring:message code="generics.truncated.seebelow" />
				</c:when>
				<c:otherwise>
					${product.description }
				</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="product-welcome-provider">
			<a href="${urlProfile }${business.domain}"><img class="product-provider-image" /></a>
			<div class="product-provider-info">
				<h4><a href="${urlProfile }${business.domain}">${business.fullName }</a></h4>
				<div><spring:message code="generics.provider" /></div>
			</div>
		</div>
		<div class="product-welcome-divider">&nbsp;</div>
		<div class="product-welcome-category">
			<a href="${urlCategories }${business.domain}/${product.category.id}"><img class="product-category-image" /></a>
			<div class="product-category-info">
				<h4><a href="${urlCategories }${business.domain}/${product.category.id}">${product.category.name }</a></h4>
				<div><spring:message code="generics.category" /></div>
			</div>
		</div>
	</section>
	
	<section class="editproduct-form ui-widget-content">
		<div class="editproduct-form-container">
			<form:form modelAttribute="product" method="post">
			<h4><spring:message code="product.edit.formtitle" arguments="${product.name }"/></h4>
			<ul>
				<li>
					<form:label path="name"><spring:message code="generics.forms.name" /></form:label> <br/>
					<form:input path="name" />
				</li>
				<li>
					<form:label path="description"><spring:message code="generics.forms.description" /></form:label> <br/>
					<form:textarea path="description" rows="6" style="width: 100%;"/>
				</li>
				<li>
					<button class="floatright"><spring:message code="generics.forms.submit" /></button>
				</li>
			</ul>
			</form:form>
		</div>
	</section>
	
	<section class="editproduct-photos ui-widget-content">
		<div class="editproduct-photos-container" style="position: relative;">
			<h4><spring:message code="product.edit.photos.title" /></h4>
			<div class="editable-pics"></div>
			<div class="editproduct-photos-selectors" style="text-align: right;">
				<a class="selectall" href="javascript:;">Select All</a>
				<a class="selectnone" href="javascript:;">Select None</a>
			</div>
			<div class="editproduct-photos-buttons" style="text-align: right;">
				<button class="hideall">Hide Selected</button>
				<button class="showall">Show Selected</button>
				<button class="deleteall">Delete Selected</button>
			</div>
		</div>
	</section>
</div>

<div class="info-overlay ui-corner-all ui-state-highlight hide">
	<img class="overlay-active-pic" />
	<div class="info-overlay-tabs">
		<ul>
			<li><a href="#info-overlay-options">Options</a></li>
			<li><a href="#info-overlay-edit">Edit</a></li>
		</ul>
		<div id="info-overlay-options">
			<ul>
				<li>
					<spring:message code="generics.title" />: <span class="title">THIS HAS THIRTY CHARACTERS IN</span>
				</li>
				<li>
					<spring:message code="generics.description" />: <span class="description">@LeslieMcLellan: Everything booked for #Smalltown2012! Can't wait to see everyone, enjoy the #140conf and #Hutch!!</span>
				</li>
				<li>
					<a href="javascript:;" class="a-edit-pic"><spring:message code="product.edit.photos.edit" /></a>
					- <spring:message code="product.edit.photos.edithelp" />
				</li>
				<li class="li-hide-pic">
					<a href="javascript:;" class="a-hide-pic"><spring:message code="product.edit.photos.hide" /></a>
					- <spring:message code="product.edit.photos.hidehelp" />
				</li>
				<li class="li-show-pic">
					<a href="javascript:;" class="a-show-pic"><spring:message code="product.edit.photos.show" /></a>
					- <spring:message code="product.edit.photos.showhelp" />
				</li>
				<li>
					<a href="javascript:;" class="a-delete-pic"><spring:message code="product.edit.photos.delete" /></a>
					- <spring:message code="product.edit.photos.deletehelp" />
				</li>
			</ul>
		</div>
		<div id="info-overlay-edit">
			<ul>
				<li>
					Title <br>
					<input class="title" type="text" maxlength="25" />
				</li>
				<li>
					Description <br>
					<textarea class="description" maxlength="140" rows="5" style="width: 100%; resize: none;"></textarea>
				</li>
				<li style="padding-top: 10px; text-align: right;">
					<a class="save" href="javascript:;">Save</a>
					<a class="cancel" href="javascript:;">Cancel</a>
				</li>
			</ul>
		</div>
	</div> <!-- info-overlay-tabs -->
</div>

<div class="delete-confirm" title="Really delete these pics?" style="display: none;">
	<span>This action can't be undone! Are you sure you want to delete these pics?</span>
	<div class="preview"></div>
</div>

<div class="dialog-error" title="Does not compute !@#$@!!!">
	<span class="message"></span>
</div>

<style>
.product-welcome-product h3 a {
	color: black;
	text-decoration: none;
}

.editproduct-form,
.editproduct-photos {
	margin: 20px 0 0 175px;
}
.editproduct-form-container {
	padding: 20px 20px 30px 20px;
}
.editproduct-form h4,
.editproduct-photos h4 {
	margin: 0;
}
.editproduct-form ul {
	list-style-type: none;
	padding-left: 0;
}
.editproduct-form li {
	margin: 5px 0;
}

.editproduct-photos {
	padding: 20px;
}

.editproduct-photos-selectors {
	font-size: 0.8em;
	font-weight: bold;
	color: black;
	margin: 10px 0;
}

.editable-pic-container {
	display: inline-block;
	width: 85px;
	height: 70px;
	text-align: center;
	border: 1px solid transparent;
	margin: 1px;
	position: relative;
}
.editable-pic {
	margin: 5px 5px 5px 20px;
	max-width: 60px;
	max-height: 60px;
	cursor: pointer;
}

.hidden-overlay {
	position: absolute;
	opacity: 0.7;
	color: white;
	background-color: black;
	text-align: center;
}

.info-overlay {
	width: 300px;
	min-height:  240px;
	padding: 5px;
	z-index: 1;
	font-size: 7pt !important;
	position: absolute;
}
.info-overlay-tabs {
	margin-left: 100px;
	height: 99%;
}
.info-overlay-tabs .ui-tabs-panel {
	padding: 5px 1.4em;
	font-size: 9pt;
}
.info-overlay .ui-tabs-nav {
	height: 2.5em;		
}
.info-overlay .ui-button {
	font-size: 0.9em;
}
.info-overlay ul {
	list-style-type: none;
	padding-left: 0;
}
.info-overlay li {
	margin-top: 2px;
}
.info-overlay img {
	float:left;
	margin: 3px;
}
.info-overlay a {
	font-weight: bold;
}

.triangle {
	height: 0; 
	width: 0; 
	border-top: 30px solid transparent; 
	border-bottom: 30px solid transparent;
}

.ui-tabs-panel {
	height: 100%;	
}
</style>

<script>
window.constants = {
	domain: '${business.domain}',
	productId : '${product.id}',
	nodescription : '<spring:message code="generics.nodescription" />',
	notitle: '<spring:message code="generics.notitle" />'
}

window.urls = {
	noimage : '${noimage}',
	noimage50 : '${noimage50}',
	products : '${urlProducts}',
	editpics : '${urlEditPics}',
	hidepics : '${urlHidePics}',
	showpics : '${urlShowPics}',
	deletepics : '${urlDeletePics}'
}

$(function(){
	var $productPic = $('.product-welcome-image'),
		$providerPic = $('.product-provider-image'),
		$categoryPic = $('.product-category-image'),
		$picContainer = $('.editable-pics');
	
	var $description = $('#description');
	unescapeBrs($description);
	
	//handle welcome pics
	$productPic.attr('src', '${not empty product.mainpic}' === 'true' ? '${product.mainpic.largeThumbnail}' : urls.noimage);
	$providerPic.attr('src', '${not empty business.profilepic}' === 'true' ? '${business.profilepic.smallSquare}' : urls.noimage50);
	$categoryPic.attr('src', '${not empty product.category.mainpic}' === 'true' ? '${product.category.mainpic.smallSquare}' : urls.noimage50);

	//picture
	var $sectionPhotos = $('.editproduct-photos'),
		$containerPhotos = $('.editproduct-photos-container'),
		$infooptions = $('#info-overlay-options'),
		$infotabs = $('.info-overlay-tabs'),
		$overlayPic = $('.overlay-active-pic'),
		$optionsTitle = $('#info-overlay-options .title'),
		$optionsDesc = $('#info-overlay-options .description'),
		$liHidePic = $('.li-hide-pic'),
		$liShowPic = $('.li-show-pic'),
		$editlink = $('.a-edit-pic'),
		$showlink = $('.a-show-pic'),
		$hidelink = $('.a-hide-pic'),
		$deletelink = $('.a-delete-pic'),
		$editFieldTitle = $('#info-overlay-edit .title'),
		$editFieldDesc = $('#info-overlay-edit .description'),
		$editBtnSave = $('#info-overlay-edit .save'),
		$editBtnCancel = $('#info-overlay-edit .cancel')
		$triangle = $('<div class="triangle">').appendTo('body');
	
	$.get(urls.products + constants.domain + '/' + constants.productId + '/pics.json', function(response) {
		switch(response.status) {
		case '200':
			if(response.imgurs.length === 0) {
				$sectionPhotos.hide();
				break;
			}
			for(var i = 0, length = response.imgurs.length; i < length; ++i) {
				appendEditablePicContainer(response.imgurs[i], $picContainer);
			}
			setTimeout(refreshHiddenOverlays, 200);
			break;
		default:
			debug('Error: ' + response);
		}
	});
	
	$infotabs.tabs().removeClass('ui-corner-all');
	
	var $div = $('.info-overlay').click(function(event){
		event.stopPropagation();
	});
	
	//edit
	/*
		$div.imgur = {
			id: ,
			name: ,
			description: ,
		}
	*/
	$editlink.click(function(){
		$infotabs.tabs('option', 'selected', 1);
		debug('setting to ' + $div.imgur.title);
		$editFieldTitle.val($div.imgur.title);
		$editFieldDesc.val($div.imgur.description);
	});
	$editBtnCancel.click(function(){
		$infotabs.tabs('option', 'selected', 0);
		$editFieldTitle.val($div.imgur.title);
		$editFieldDesc.val($div.imgur.description);
	});
	$editBtnSave.click(function(){
		$('<div class="overlay">').text('Updating picture...').appendTo($div);
		var imgur = {
			title : $editFieldTitle.val(),
			description : $editFieldDesc.val()
		};
		console.debug('submitting edit ' + JSON.stringify(imgur));
		$.post(urls.editpics + constants.domain + '/' + $div.imgur.id + '.json',
			imgur,
			function(response){
				switch(response.status) {
				case '200':
					var newtitle = unescapeBrs(response.imgur.title);
					var newdesc = unescapeBrs(response.imgur.description);
					
					debug('before: ' + response.imgur.description + ' after: ' + newdesc);
					
					if($div.imgur && $div.imgur.id === response.imgur.imageId) {
						//because user may have clicked another imgur while .post was in progress
						$optionsTitle.text(newtitle);
						$optionsDesc.text(newdesc);
						$div.imgur.title = newtitle;
						$div.imgur.description = newdesc;
						$infotabs.tabs('option', 'selected', 0);
					} else {
						debug('Mismatch! Updated id: ' + response.imgur.imageId + ' New imgur: ' + ($div.imgur ? $div.imgur.id : 'undefined'));
					}
					
					var $updatedImg = $('img[imgur-id="' + response.imgur.imageId + '"]');
					if($updatedImg.length) {
						$updatedImg.attr('title', newtitle)
							.attr('alt', newtitle)
							.attr('imgur-title', newtitle)
							.attr('imgur-description', newdesc);
					} else {
						debug('No image found for id ' + response.imgur.imageId);
					}
					
					$div.find('.overlay').delay(800).fadeOut(200, function() { $(this).remove(); });

					break;
				default:
					debug('Error: ' + response.message);
					debug(response);
				}
			}
		);
	});
	
	//init
	function appendEditablePicContainer(imgur, $parent) {
		var $container = $('<div class="editable-pic-container">').appendTo($parent);
		
		var title = unescapeBrs(imgur.title);
		var desc = unescapeBrs(imgur.description);
		
		var $checkbox = $('<input type="checkbox" class="pic-selector" style="float: left; position: absolute;">').attr('imgur-id', imgur.imageId).appendTo($container);
		
		var $img = $('<img class="editable-pic">')
			.attr('src', imgur.smallSquare)
			.attr('alt', title)
			.attr('title', title)
			.addClass(imgur.hidden ? 'hidden-pic' : 'visible-pic')
			.appendTo($container);
		
		$img.attr('imgur-hash', imgur.hash)
			.attr('imgur-deletehash', imgur.deletehash)
			.attr('imgur-description', desc)
			.attr('imgur-title', title)
			.attr('imgur-id', imgur.imageId);
		if(imgur.hidden) {
			$img.addClass('hidden-pic');
		}
		
		$img.click(function(event){
			var $that = $(this);
			event.stopPropagation();
			
			if($div.master && $div.master.attr('src') === $that.attr('src')) {
				overlay($that, true);
			} else {
				overlay($that);
			}
		});
	}
	
	//overlay
	function overlay($target, positionOnly) {
		//if rightmost part of div would exceed 960 relative to .container_12, overlay left
		//else overlay right
		
		var containerOffset = $target.closest('.container_12').offset();
		var thisOffset = $target.offset();
		var ifGt960Left = thisOffset.left - containerOffset.left + $div.width();
		
		$div.show();
		if(ifGt960Left > 960) {
			$div.overlay($target, 'left', $triangle);
		} else {
			$div.overlay($target, 'right', $triangle);
		}
		
		if(positionOnly) return;
		
		$infotabs.tabs('option', 'selected', 0);
		$div.find('.overlay').delay(800).fadeOut(200, function() { $(this).remove(); });
		
		$div.find('img').attr('src', $target.attr('src')).attr('imgur-id', $target.attr('imgur-id'));
		
		$div.find('.hidden-overlay').remove();
		if($target.hasClass('hidden-pic')) {
			markAsHidden($div.find('img'), true);
			$liShowPic.show();
			$liHidePic.hide();
		} else {
			$liHidePic.show();
			$liShowPic.hide();
		}
		
		$optionsTitle.text($target.attr('imgur-title') ? $target.attr('imgur-title') : constants.notitle);
		$optionsDesc.text($target.attr('imgur-description') ? $target.attr('imgur-description') : constants.nodescription);
		$div.master = $target;
		$div.imgur = {
			id : parseInt($target.attr('imgur-id')),
			title: $target.attr('imgur-title'),
			description : $target.attr('imgur-description')
		}
		$editFieldTitle.val($div.imgur.title);
		$editFieldDesc.val($div.imgur.description);
	}
	
	$(document).click(function(){
		hideOverlay();
	});
	
	function hideOverlay() {
		$triangle.fadeOut(200, function(){
			$triangle.hide();
		});
		$div.fadeOut(200, function(){
			$div.hide()
		});
		$div.master = null;
	}
	
	//batch updates
	var $btnHideAll = $('.editproduct-photos-buttons .hideall').prop('disabled', true),
		$btnShowAll = $('.editproduct-photos-buttons .showall').prop('disabled', true),
		$btnDeleteAll = $('.editproduct-photos-buttons .deleteall').prop('disabled', true),
		$linkSelectAll = $('.editproduct-photos-selectors .selectall'),
		$linkSelectNone = $('.editproduct-photos-selectors .selectnone');
	
	$linkSelectAll.click(function(){
		$('.editproduct-photos .pic-selector').prop('checked', true).parent().addClass('ui-state-active');
		$('.editproduct-photos-buttons button').button('enable');
	});
	$linkSelectNone.click(function(){
		$('.editproduct-photos .pic-selector').prop('checked', false).parent().removeClass('ui-state-active');
		$('.editproduct-photos-buttons button').button('disable');
	});
	
	$sectionPhotos.on({
		change : function(){
			var $that = $(this);
			if($that.is(':checked')) {
				$that.parent().addClass('ui-state-active');
				$('.editproduct-photos-buttons button').prop('disabled', false).button('enable');
			} else {
				$that.parent().removeClass('ui-state-active');
				if(getCheckedImages().length === 0) {
					$('.editproduct-photos-buttons button').prop('disabled', true).button('disable');
				}
			}
		}
	}, 'input[type="checkbox"].pic-selector');
	
	function getCheckedImages() {
		return $('.editproduct-photos .pic-selector:checked').siblings('img');
	}
	
	$hidelink.click(function(){
		$('<div class="overlay">').appendTo($infooptions);
		var toHideId = $overlayPic.attr('imgur-id');
		var toHideIds = [toHideId];
		$.post(urls.hidepics + constants.domain + '/' + constants.productId + '.json', 
			{'imgurIds': toHideIds},
			function(response){
				switch(response.status){
				case '200':
					markAsHidden($overlayPic);
					markAsHidden($('.editable-pic[imgur-id="' + toHideId + '"]'));
					$liHidePic.hide();
					$liShowPic.show();
					break;
				default:
					debug(response);
				}
				$div.find('.overlay').delay(800).fadeOut(200, function() { $(this).remove(); });
			}
		);
	});
	
	$showlink.click(function(){
		$('<div class="overlay">').appendTo($infooptions);
		var toShowId = $overlayPic.attr('imgur-id');
		var toShowIds = [toShowId];
		$.post(urls.showpics + constants.domain + '/' + constants.productId + '.json', 
			{'imgurIds': toShowIds},
			function(response){
				switch(response.status){
				case '200':
					markAsVisible($overlayPic);
					markAsVisible($('.editable-pic[imgur-id="' + toShowId + '"]'));
					$liShowPic.hide();
					$liHidePic.show();
					break;
				default:
					debug(response);
				}
				$infooptions.find('.overlay').delay(800).fadeOut(200, function() { $(this).remove(); });
			}
		);
	});
	
	$deletelink.click(function(){
		var toDeleteId = $overlayPic.attr('imgur-id');
		deleteConfirm([toDeleteId]);
	});
	
	$btnHideAll.click(function(){
		hideOverlay(); //don't want to have to deal with overlay functions in case active photo is hidden this way
		$('<div class="overlay">').appendTo($containerPhotos);
		var toHideIds = [];
		getCheckedImages().each(function(i, img){
			toHideIds.push($(img).attr('imgur-id'));
		});
		$.post(urls.hidepics + constants.domain + '/' + constants.productId + '.json', 
			{'imgurIds': toHideIds},
			function(response){
				switch(response.status){
				case '200':
					$.each(toHideIds, function(i, id){
						markAsHidden($('.editable-pic[imgur-id="' + id + '"]'));
					});
					break;
				default:
					debug(response);
				}
			}
		);
		$containerPhotos.find('.overlay').delay(800).fadeOut(200, function() { $(this).remove(); });
	});
	
	$btnShowAll.click(function(){
		$('<div class="overlay">').appendTo($containerPhotos);
		hideOverlay();
		var toShowIds = [];
		getCheckedImages().each(function(i, img){
			toShowIds.push($(img).attr('imgur-id'));
		});
		$.post(urls.showpics + constants.domain + '/' + constants.productId + '.json', 
			{'imgurIds': toShowIds},
			function(response){
				switch(response.status){
				case '200':
					$.each(toShowIds, function(i, id){
						markAsVisible($('.editable-pic[imgur-id="' + id + '"]'));
					});
					break;
				default:
					debug(response);
				}
			}
		);
		$containerPhotos.find('.overlay').delay(800).fadeOut(200, function() { $(this).remove(); });
	});
	
	function markAsHidden($target, unclickable) {
		var $container = $target.parent();
		$container.find('.hidden-overlay').remove();

		var $overlay = $('<div class="hidden-overlay">')
							.text('Hidden')
							.appendTo($container)
							.width($target.width())
							.height($target.height())
							.offset($target.offset())
							.attr('imgur-id', $target.attr('imgur-id'))
							.css('font-size', $target.height()/4 + 'px')
							.css('line-height', '4em')
							.css('cursor', 'default');
		
		if(!unclickable) {
			$overlay.css('cursor', 'pointer')
				.click(function(event){
					event.stopPropagation();
					$target.click();
				});
		}
							
		$target.addClass('hidden-pic')
	}
	
	function markAsVisible($target) {
		$target.parent().find('.hidden-overlay').remove();
		$target.removeClass('hidden-pic');
	}
	
	//delete pics
	//override jquery ui dialog defaults
	$.extend($.ui.dialog.prototype.options, {
	    modal: true,
	    resizable: false,
	    maxHeight: 250,
	    width:500,
		closeOnEscape: false,
		hide: {effect: "fade", duration: 500}
	});
	
	$btnDeleteAll.click(function(){
		hideOverlay();
		var toDeleteIds = [];
		var checkedImages = getCheckedImages();
		debug('checked images: ' + checkedImages.length);
		getCheckedImages().each(function(i, img){
			toDeleteIds.push($(img).attr('imgur-id'));
		});
		deleteConfirm(toDeleteIds);
	});
	
	function deleteConfirm(toDeleteIds) {
		debug('todelete: ' + toDeleteIds);
		
		var $confirm = $('.delete-confirm');
		var $preview = $('.preview');
		$preview.children().remove();
		
		for(var i = 0, length = toDeleteIds.length; i < length; ++i) {
			$('.editable-pic[imgur-id="' + toDeleteIds[i] + '"]').clone()
				.css('cursor', 'default')
				.appendTo($preview);
		}
		
		$confirm.dialog({
			buttons: {
				'Delete forever' : function(){
					$('<div class="overlay">').appendTo($containerPhotos);
					$.post(urls.deletepics + constants.domain + '/' + constants.productId + '.json', 
						{'imgurIds': toDeleteIds},
						function(response){
							switch(response.status){
							case '200':
								var waiting = toDeleteIds.length;
								
								function imgurdelete($picToDelete, $parentToDelete) {
									$.get('http://api.imgur.com/2/delete/' + $picToDelete.attr('imgur-deletehash') + '.json')
									.complete(function(response){ //use complete because imgur errors might cause trouble
										$parentToDelete.remove();
										if(--waiting === 0) {
											$containerPhotos.find('.overlay').remove();
										} else {
											debug('not done yet. ' + waiting + ' pics waiting for imgur resp');
										}
									});
								}
								
								for(var i = 0, length = toDeleteIds.length; i < length; ++i) {
									var $picToDelete = $('.editable-pic[imgur-id="' + toDeleteIds[i] + '"]');
									var $parentToDelete = $picToDelete.parent('.editable-pic-container').append('<div class="overlay">');
									imgurdelete($picToDelete, $parentToDelete);
								}
								break;
							default:
								debug(response);
							}
							refreshHiddenOverlays();
							$infooptions.find('.overlay').delay(800).fadeOut(200, function() { $(this).remove(); });
						}
					);					
					$confirm.dialog('close');
				},
				'Cancel' : function() {
					$confirm.dialog('close');
				}
			}
		});
	}
	
	function refreshHiddenOverlays() {
		//"reposition" hidden-overlays
		$('.hidden-pic').each(function(i, pic){
			markAsHidden($(pic));
		});
	}
	
	//keep this at bottom, might need it for a bunch of stuff
	$(window).resize(function() {
		if($div.master) {
			overlay($div.master, true);
		}
		refreshHiddenOverlays();
	});
});
</script>

<spring:url var="jsApplication" value="/resources/javascript/application.js" />
<script type="text/javascript" src="${jsApplication }"></script>