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
			<h3>${product.name }</h3>
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
		<h4><spring:message code="product.edit.photos.title" /></h4>
		<div class="editable-pics"></div>
	</section>
</div>

<div class="info-overlay ui-corner-all ui-state-highlight hide">
	<img class="overlay-active-pic" />
	<div class="info-overlay-tabs">
		<ul>
			<li><a href="#info-overlay-options">Options</a></li>
			<li><a href="#info-overlay-edit">Edit</a></li>
		</ul>
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
		<div id="info-overlay-options">
			<ul>
				<li>
					<spring:message code="generics.title" />: <span class="img-title">THIS HAS THIRTY CHARACTERS IN</span>
				</li>
				<li>
					<spring:message code="generics.description" />: <span class="img-description">@LeslieMcLellan: Everything booked for #Smalltown2012! Can't wait to see everyone, enjoy the #140conf and #Hutch!!</span>
				</li>
				<li>
					<a href="javascript:;" class="a-edit-pic"><spring:message code="product.edit.photos.edit" /></a>
					- <spring:message code="product.edit.photos.edithelp" />
				</li>
				<li>
					<a href="javascript:;" class="a-hide-pic"><spring:message code="product.edit.photos.hide" /></a>
					- <spring:message code="product.edit.photos.hidehelp" />
				</li>
				<li>
					<a href="javascript:;" class="a-delete-pic"><spring:message code="product.edit.photos.delete" /></a>
					- <spring:message code="product.edit.photos.deletehelp" />
				</li>
			</ul>
		</div>
	</div> <!-- info-overlay-tabs -->
</div>

<style>
.editproduct-form,
.editproduct-photos {
	margin: 20px 0 0 175px;
}
.editproduct-form-container {
	padding: 20px 20px 30px 20px;
}
.editproduct-form h4,		$editFieldName.val($div.attr('name'));
		$editFieldDesc.val($div.attr('description'));
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
.editable-pic-container {
	display: inline-block;
	width: 80px;
	height: 80px;
	text-align: center;
}
.editable-pic {
	margin: 5px 5px 5px 15px;
	max-width: 60px;
	max-height: 60px;
	cursor: pointer;
}

.info-overlay {
	width: 300px;
	height:  240px;
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
	height: 2.5em;		$editFieldName.val($div.attr('name'));
		$editFieldDesc.val($div.attr('description'));
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
	editpics : '${urlEditPics}'
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

	$.get(urls.products + constants.domain + '/' + constants.productId + '/pics.json', function(response) {
		switch(response.status) {
		case '200':
			for(var i = 0, length = response.imgurs.length; i < length; ++i) {
				appendEditablePicContainer(response.imgurs[i], $picContainer);
			}
			break;
		default:
			debug('Error: ' + response);
		}
	});
	
	
	//picture
	var $infotabs = $('.info-overlay-tabs'),
		$editlink = $('.a-edit-pic'),
		$editFieldTitle = $('#info-overlay-edit .title'),
		$editFieldDesc = $('#info-overlayy-edit .description'),
		$editBtnSave = $('#info-overlay-edit .save'),
		$editBtnCancel = $('#info-overlay-edit .cancel');
	
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
		$editFieldTitle.val($div.imgur.title);
		$editFieldDesc.val($div.imgur.description);
	});
	$editBtnCancel.click(function(){
		$infotabs.tabs('option', 'selected', 0);
		$editFieldTitle.val($div.imgur.title);
		$editFieldDesc.val($div.imgur.description);
	});
	$editBtnSave.click(function(){
		$.post(urls.editpics + constants.domain + '/' + $div.imgur.id + '.json',
			{
				title : $editFieldTitle.val(),
				description : $editFieldDesc.val()
			},
			function(response){
				switch(response.status) {
				case '200':
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
		
		var $img = $('<img class="editable-pic">')
			.attr('src', imgur.smallSquare)
			.attr('alt', imgur.title)
			.attr('title', imgur.title)
			.appendTo($container);
		
		$img.attr('imgur-hash', imgur.hash)
			.attr('imgur-description', imgur.description)
			.attr('imgur-title', imgur.title)
			.attr('imgur-id', imgur.imageId);
		
		$img.click(function(event){
			var $that = $(this);
			event.stopPropagation();
			
			if($div.master && $div.master.attr('src') === $that.attr('src')) {
				overlay($that, true);
			} else {
				overlay($that);
			}
		});
		$container.append('<br>');
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
			$div.overlay($target, 'left');
		} else {
			$div.overlay($target, 'right');
		}
		
		if(positionOnly) return;
		
		$div.find('.overlay').remove();
		$div.find('img').attr('src', $target.attr('src'));
		$div.find('.img-title').text($target.attr('title') ? $target.attr('title') : constants.notitle);
		$div.find('.img-description').text($target.attr('description') ? $target.attr('description') : constants.nodescription);
		$div.master = $target;
		$div.imgur = {
			id : $target.attr('imgur-id'),
			title: $target.attr('imgur-title'),
			description : $target.attr('imgur-description')
		}
		debug($div.imgur);
		debug('$div.imgur: ' + JSON.stringify($div.imgur));
	}
	
	$(window).resize(function() {
		if($div.master) {
			overlay($div.master, true);
		}
	});
	
	$(document).click(function(){
		hideOverlay();
	});
	
	function hideOverlay() {
		$div.hide();
		$div.master = null;
	}
});
</script>

<spring:url var="jsApplication" value="/resources/javascript/application.js" />
<script type="text/javascript" src="${jsApplication }"></script>