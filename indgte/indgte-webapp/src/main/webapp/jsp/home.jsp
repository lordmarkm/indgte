<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@include file="tiles/links.jsp" %>

<spring:url var="paperclip" value="/resources/images/icons/paperclip.png" />

<title>Dumaguete</title>
<link rel="stylesheet" href="<spring:url value='/resources/css/lists.css' />" />
<link rel="stylesheet" href="<spring:url value='/resources/css/feed.css' />" />
<link rel="stylesheet" href="<spring:url value='/resources/css/home.css' />" />

<script src="${jsValidator }"></script>

<div class="grid_8 maingrid">

<sec:authorize access="hasRole('ROLE_ANONYMOUS')">
<section class="newpost">
	<div><spring:message code="anon.newpost" /></div>
</section>
</sec:authorize>

<sec:authorize access="hasRole('ROLE_USER')">
<section class="newpost">
	<form id="form-newpost">
		<div class="border-provider ui-state-active inline-block">
			<div class="status-title-container">
				<input name="title" class="status-title ui-state-active hide" maxlength="45" type="text" placeholder="title" />
			</div>
			<textarea name="text" class="status-textarea noattachment" maxlength="1000" rows="1" placeholder="<spring:message code="home.status.textarea" />"></textarea>	
			<div class="attach-input-container">
				<!-- Hidden -->
				<input class="posterId" type="hidden" value="${user.id }" />
				<input class="posterType" type="hidden" value="user" />
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
		
		<input type="text" name="tags" class="ipt-tags ui-state-active" placeholder="Tags 'news buglasan pics-2012' (space-delimited, 3 max)" />
		<div class="newpost-errors"></div>
	</form>
	<div class="status-options hide">
		<div class="floatright">
			<span class="status-counter"></span>
			<div class="post-as">
				<div class="menubutton post-as-visibles" title="Posting as ${user.username }">
					<img src="${user.imageUrl }" />
					<div class="ui-icon ui-icon-triangle-1-s"></div>
				</div>
				<div class="post-as-menu">
					<div class="post-as-option" posterId="${user.id }" posterType="user">
						<img src="${user.imageUrl }"> 
						<div class="name">${user.username }</div>
						<div class="post-as-category">Personal</div>
					</div>
					<c:forEach items="${user.businesses }" var="business">
					<div class="post-as-option" posterId="${business.id }" posterType="business">
						<c:choose>
						<c:when test="${not empty business.profilepic }">
							<img src="${business.profilepic.smallSquare }" />
						</c:when>
						<c:otherwise>
							<img src="http://i.imgur.com/Y0NTes.jpg" />
						</c:otherwise>
						</c:choose>
						<div class="name">${business.fullName }</div>
						<div class="post-as-category">${business.category.name }</div>
					</div>
					</c:forEach>
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
		<div class="clear"></div>
	</div>
</section>
</sec:authorize>

<section class="feedcontainer">
	<div class="posts-container relative">
		<ul class="posts"></ul>
	</div>
	<div class="loadmoreContainer" style="text-align: center; height: 100px; position: relative;">
		<button class="loadmore" style="width: 50%; margin-top: 50px;">Load 10 more</button>
	</div>
</section>
</div>

<div class="dgte-preview"></div>

<script>
window.constants = {
	imgurKey : '${imgurKey}',
	auth : '<sec:authorize access="hasRole('ROLE_USER')">true</sec:authorize>' === 'true'
}
window.urls = {
	status : '<spring:url value="/i/newstatus.json" />',
	subposts : '<spring:url value="/i/subposts/json" />',
	postdetails : '<spring:url value="/i/posts/" />',
	linkpreview : '<spring:url value="/i/linkpreview/" />',
	user : '<spring:url value="/p/user/" />',
	business : '<spring:url value="/" />',
	category: '<spring:url value="/b/categories/" />',
	categoryWithProducts: '<spring:url value="/b/categories/" />',
	getproducts: '<spring:url value="/b/products/" />',
	product: '<spring:url value="/b/products/" />',
	productwithpics: '<spring:url value="/b/products/withpics/" />',
	buyandsellitem: '<spring:url value="/t/" />',
	imgur : 'http://i.imgur.com/',
	imgurPage : 'http://imgur.com/',
	searchOwn: '<spring:url value="/s/own/" />'
}

$(function(){
	//newpost
	var $newpost = $('.newpost'),
		$form = $('#form-newpost'),
		$errors = $('.newpost-errors'),
		$title = $('.status-title'),
		$status = $('.status-textarea'),
		$attachInputContainer = $('.attach-input-container').addClass('ui-state-active').hide(),
		$attachType = $('.iptType'),
		$posterId = $('.posterId'),
		$posterType = $('.posterType'),
		$iptFile = $('.iptFile').hide(),
		$iptUrl = $('.iptUrl').hide(),
		$iptEntity = $('.iptEntity').hide(),
		$entityPreview = $('.entity-preview'),
		$entitySuggestions = $('.entity-suggestions').hide(),
		$statusCounter = $('.status-counter'),
		$statusOptions = $('.status-options'),
		$menubutton = $('.menubutton'),
		$postas = $('.post-as'),
		$postasVisibles = $('.post-as-visibles'),
		$postasMenu = $('.post-as-menu').hide(),
		$attach = $('.attach'),
		$attachVisibles = $('.attach-visibles'),
		$attachMenu = $('.attach-menu').hide(),
		$btnPost = $('.btn-post'),
		$loadmoreContainer = $('.loadmoreContainer'),
		$loadmore = $('.loadmore'),
		$rdoSortPosts = $('#rdo-post-sort').buttonset(),
		$linkpreview = $('.link-preview-container'),
		$linkpreviewImg = $('.link-preview-images'),
		$iptTags = $('.ipt-tags');
	
	//posts
	var $feedcontainer = $('.feedcontainer'),
		$posts = $('.posts');
	
	//newpost
	function shrinkStatus() {
		$status.attr('rows', '1')
			.attr('cols', dgte.home.statusColsCollapsed)
			.removeClass('ui-state-active')
			.addClass('ui-state-default');	
		$newpost.removeClass('active');
		
		//hide stuff
		$title.hide();
		$statusOptions.hide();
		$errors.hide();
		hideAttachInputs();
		$attachInputContainer.hide();
		
		//clear inputs
		$attachType.val('none');
		$newpost.find('input:not(.posterId):not(.posterType),textarea').val('');
		
		//entity
		$iptEntity.removeAttr('entitytype').removeAttr('entityid');
		$entityPreview.html('');
		
		//tags
		$iptTags.hide();
	}
	
	function matchWidths() {
		$title.css('width', $status.width());
		$errors.css('width', $status.width() + 4);
		$attachInputContainer.css('width', $status.width() + 4);
		$iptTags.css('width', $status.width() + 8);
	}
	
	function expandStatus() {
		$status.attr('cols', dgte.home.statusColsExpanded)
			.removeClass('ui-state-default')
			.addClass('ui-state-active');
		resizeVertical();
		$newpost.addClass('active');
		$statusOptions.show();
		$title.show();
		$errors.show();
		$iptTags.show();
		checkActiveAttachment();
		checkPoster();
		matchWidths();
	}
	
	function checkStatus() {
		var status = $status.val();
		if(!status.trim()) {
			shrinkStatus();
		} else {
			expandStatus();
		}
	}
	if(constants.auth) checkStatus();
	
	$status.focus(function(){
		expandStatus();
	});
	
	function resizeVertical() {
		var content = $status.val();
		$statusCounter.text(dgte.home.postMaxlength - content.length);
		
		var cols = $status[0].cols;
		var linecount = 0;
		var splitcontent = content.split('\n');
		for(var i = 0, length = splitcontent.length; i < length; ++i) {
			linecount += 1 + Math.floor(splitcontent[i].length / cols);
		}
		$status.attr('rows', linecount);
	}
	
	$status.keyup(function(){
		resizeVertical();
	});
	$status.bind('paste',function(){
		resizeVertical();
	});
	
	//post-as
	$menubutton.mouseenter(function(){
		var $this = $(this);
		if(!$this.hasClass('ui-state-active')) $this.addClass('ui-state-highlight');
	});
	$menubutton.mouseleave(function(){
		var $this = $(this);
		$this.removeClass('ui-state-highlight');
	});
	
	$postasVisibles.click(function(event){
		event.stopPropagation();
		hideAttachMenu();
		if($postasVisibles.hasClass('ui-state-active')) hidePostasMenu();
		else showPostasMenu();
	});
	
	function showPostasMenu() {
		$postasVisibles.removeClass('ui-state-highlight');
		$postasVisibles.addClass('ui-state-active');
		$postasMenu.show().addClass('ui-state-active');
	}
	
	function hidePostasMenu() {
		$postasVisibles.removeClass('ui-state-active');
		$postasMenu.hide();
	}
	
	$postasMenu.on({
		mouseenter: function(event){
			event.stopPropagation();
			$(this).addClass('ui-state-highlight');
		},
		mouseleave: function(event) {
			event.stopPropagation();
			$(this).removeClass('ui-state-highlight');
		},
		click: function() {
			var $this = $(this);
			var imgSrc = $this.find('img').attr('src');
			var name = $this.find('.name').text();
			$posterId.val($this.attr('posterId'));
			$posterType.val($this.attr('posterType'));
			$postasVisibles.attr('title', 'Posting as ' + name).children('img').attr('src', imgSrc ? imgSrc : dgte.urls.blackSquareSmall);
			hidePostasMenu();
		},
	}, '.post-as-option');
	
	//attach
	$attachVisibles.click(function(event){
		event.stopPropagation();
		hidePostasMenu();
		if($attachVisibles.hasClass('ui-state-active')) hideAttachMenu();
		else showAttachMenu();
	});
	
	function showAttachMenu() {
		$attachVisibles.removeClass('ui-state-highlight');
		$attachVisibles.addClass('ui-state-active');
		$attachMenu.show().addClass('ui-state-active');
	}
	
	function hideAttachMenu() {
		$attachVisibles.removeClass('ui-state-active');
		$attachMenu.hide();
	}
	
	function hideAttachInputs() {
		$attachInputContainer.find('input').hide();
		$entityPreview.hide();
		$linkpreview.hide();
	}
	
	$('.attach-option').click(function(){
		$('<div class="overlay">').appendTo($newpost).delay(800).fadeOut(200, function() { $(this).remove(); });
	});
	
	$('.attach-option.image').click(function(){
		$attachInputContainer.show();
		hideAttachInputs();
		$attachType.val('imgur');
		$status.removeClass('noattachment');
		$iptFile.show();
		matchWidths();
	});
	
	$('.attach-option.video').click(function(){
		$attachInputContainer.show();
		hideAttachInputs();
		$attachType.val('video');
		$status.removeClass('noattachment');
		$iptUrl.attr('placeholder', 'Paste video embed code').show();
		matchWidths();
	});

	$('.attach-option.link').click(function(){
		$attachInputContainer.show();
		hideAttachInputs();
		$attachType.val('link');
		$status.removeClass('noattachment');
		$iptUrl.attr('placeholder', 'Paste link URL').show();
		$linkpreview.show();
		matchWidths();
	});
	
	$('.attach-option.entity').click(function(){
		$attachInputContainer.show();
		hideAttachInputs();
		$attachType.val('entity');
		$status.removeClass('noattachment');
		$iptEntity.show().blur();
		matchWidths();
	});
	
	$('.attach-option.none').click(function(){
		$attachType.val('none');
		$status.addClass('noattachment');
		$attachInputContainer.hide();
	});
	
	//onload and on status expand, check if attachments are active (happens on refresh)
	//however, if onload, status box must be expanded, otherwise we see collapsed status box with input fields
	function checkActiveAttachment() {
		var attachType = $attachType.val();
		if(attachType && attachType != 'none' && $status.hasClass('ui-state-active')) {
			$attachInputContainer.show();
			$status.removeClass('noattachment');
			hideAttachInputs();
			switch($attachType.val()) {
			case 'imgur':
				$iptFile.show();
				break;
			case 'video':
				$iptUrl.attr('placeholder', 'Paste video embed code').show();
				break;
			case 'link':
				$iptUrl.attr('placeholder', 'Paste link URL').show();
				$linkpreview.show();
				break;
			case 'entity':
				$iptEntity.show().blur();
				break;
			}
		}
	}
	checkActiveAttachment();
	
	//same thing for poster if poster has been changed
	function checkPoster() {
		var posterId = $posterId.val();
		var posterType = $posterType.val();
		if(posterType === 'user') return; //default
		$('.post-as-option[posterid="' + posterId + '"]').click();
	}
	checkPoster();
	
	$attachMenu.on({
		mouseenter: function(event){
			event.stopPropagation();
			$(this).addClass('ui-state-highlight');
		},
		mouseleave: function(event) {
			event.stopPropagation();
			$(this).removeClass('ui-state-highlight');
		},
		click: function() {
			hideAttachMenu();
		},
	}, '.attach-option');
	
	//link
	$iptUrl.change(function(){
		if($attachType.val() != 'link') return;
		$linkpreview.find('.inline-block div,.link-preview-images').text('');
		var url = $iptUrl.val();
		$.get(urls.linkpreview, {uri: url}, function(response) {
			debug(response);
			
			switch(response.status) {
			case '200':
				$linkpreview
					.find('.link-preview-title').text(response.title).end()
					.find('.link-preview-description').text(response.description).end()
					.find('.link-preview-url').text(response.url);
			
				if(response.ogImage) {
					$('<img>').attr('src', response.ogImage).appendTo($linkpreviewImg);
				} else {
					if(response.images.length > 0) {
						$('<img>').attr('src', response.images[0]).appendTo($linkpreviewImg);
					}
					
					//for(var i = 0, len = response.images.length; i < len; ++i) {
					//	$('<img>').attr('src', response.images[i]).appendTo($linkpreviewImg);
					//}
				}
				break;
			case '500':
				$linkpreview
					.find('.link-preview-title').text(url).end()
					.find('.link-preview-description').text('').end()
					.find('.link-preview-url').text(url);
				$('<img>').attr('src', '${noimage50	}').appendTo($linkpreviewImg);
				break;
			default:
				debug('wat');
				debug(response);
			}
		});
	});
	
	//entity
	$iptEntity.bind({
		keyup: startTimeout,
		paste: startTimeout,
		focus: startTimeout
	});
	
	var searchtimeout;
	function startTimeout() {
		if(searchtimeout) {
			clearTimeout(searchtimeout);
		}
		searchtimeout = setTimeout(autocompleteOwn, 500);
	}
	
	function autocompleteOwn() {
		if(!$iptEntity.is(':visible')) return;
		var term = $iptEntity.val();
		if(term.length < dgte.search.autocompleteMinlength) return;
		$.get(urls.searchOwn + term + '.json', function(response){
			switch(response.status) {
			case '200':
				$entitySuggestions.html('');
				var visibleResults = 0;
				function makeAutocompleteResult(result, urlRoot) {
					var $auto = $('<div class="autocomplete-container entity-suggestion">')
						.attr('entitytype', result.type)
						.attr('entityid', result.id)
						.appendTo($entitySuggestions);
					
					$('<img class="autocomplete-img">').attr('src', result.thumbnailHash ? urls.imgur + result.thumbnailHash + 's.jpg' : dgte.urls.blackSquareSmall).appendTo($auto);
					$('<div class="autocomplete-title">').text(result.title).appendTo($auto);
					
					if(result.description && result.description.length < 80) {
						$('<div class="autocomplete-description">').text(result.description).appendTo($auto);
					} else if(result.description) {
						$('<div class="autocomplete-description">').text(result.description.substring(0, 80) + '... ').appendTo($auto);
					}
					++visibleResults;
				}
				
				if(response.business && response.business.length > 0) {
					$('<div class="ui-widget-header">').text('Businesses').appendTo($entitySuggestions);
					for(var i = 0, length = response.business.length; i < length; i++) {
						makeAutocompleteResult(response.business[i], urls.business);
					}
				}
				
				if(response.category && response.category.length > 0) {
					$('<div class="ui-widget-header">').text('Categories').appendTo($entitySuggestions);
					for(var i = 0, length = response.category.length; i < length; i++) {
						makeAutocompleteResult(response.category[i], urls.category);
					}
				}
				
				if(response.product && response.product.length > 0) {
					$('<div class="ui-widget-header">').text('Products').appendTo($entitySuggestions);
					for(var i = 0, length = response.product.length; i < length; i++) {
						makeAutocompleteResult(response.product[i], urls.product);
					}
				}
				
				if(response.buyandsellitem && response.buyandsellitem.length > 0) {
					$('<div class="ui-widget-header">').text('Buy and Sell').appendTo($entitySuggestions);
					for(var i = 0, len = response.buyandsellitem.length; i < len; ++i) {
						makeAutocompleteResult(response.buyandsellitem[i], urls.buyandsellitem);
					}
				}
				
				if(visibleResults) {
					$entitySuggestions.show();
				} else {
					$entitySuggestions.hide();
				}
				break;
			default:
				debug('Error' + JSON.stringify(response));
			}
		});
	}
	
	$entitySuggestions.on({
		mouseover: function(){$(this).addClass('ui-state-highlight')}, 
		mouseout:  function(){$(this).removeClass('ui-state-highlight')}
	}, '.autocomplete-container');
	
	$(document).on({
		click: function(){
			var $this = $(this);
			
			$iptEntity.attr('entityid', $this.attr('entityid'))
				.attr('entitytype', $this.attr('entitytype'))
				.unbind('blur')
				.blur(function(){
					if($iptEntity.attr('entityid')) {
						$iptEntity.hide();
						$entityPreview.show();
					}
				})
				.hide();
			
			$entityPreview.html('');
			$this.clone().appendTo($entityPreview).removeClass('entity-suggestion').addClass('entity-suggestion-clone');
			$entityPreview.fadeIn(500).click(function(){
				$entityPreview.hide();
				$iptEntity.show().focus();
			});
		}
	}, '.entity-suggestion');
	
	//submit post
	$form.validate({
		rules: {
			title: {
				required: true,
				rangelength: [1, 45]
			},
			text: {
				required: true,
				maxlength: 1000
			}
		},
		messages: {
			title: {
				required: 'Title can\'t be blank',
				rangelength: 'Title allowed length range: 1-45 characters'
			},
			text: {
				required: 'Post text can\'t be blank',
				maxlength: 'Post text length too long'
			}
		},
		errorPlacement: function(error, element) {
			$errors.html('').show().append(error);
		}
	});
	
	$btnPost.click(function(){
		if(!$form.valid()) return;
		
		$('<div class="overlay">').appendTo($newpost);
		
		var attachType = $attachType.val();
		debug('posting. attachment type: ' + attachType);
		
		var data = {
			posterId: $posterId.val(), //user.id or business.id
			posterType : $posterType.val(), //user or business
			title: $title.val(),
			text: $status.val()
		}
		
		switch(attachType) {
		case 'imgur':
			data.attachmentType = 'imgur';
			if(!$iptFile[0] || !$iptFile[0].files[0]) {
				dgte.error('You haven\'t attached an image', 'You should attach an image, or select "none" from the attachment type menu');
				$newpost.find('.overlay').remove();
				return;
			}
			
			upload($iptFile[0].files[0], function(imgurResponse) {
				data.hash = imgurResponse.upload.image.hash;
				postStatus(data);
			});
			return;
		case 'video':
			data.attachmentType = 'video';
			data.embed = $iptUrl.val();
			break;
		case 'link':
			data.attachmentType = 'link';
			data.link = encodeURI($iptUrl.val());
			data.attachmentImgurHash = $linkpreview.find('.link-preview-images img:first').attr('src');
			data.attachmentTitle = $linkpreview.find('.link-preview-title').text();
			data.attachmentDescription = $linkpreview.find('.link-preview-description').text();
			data.attachmentIdentifier = $linkpreview.find('.link-preview-url').text();
			break;
		case 'entity':
			data.attachmentType = $iptEntity.attr('entitytype');
			data.entityId = $iptEntity.attr('entityid');
			break;
		case '':
		default:
			data.attachmentType = 'none';
		}
		
		postStatus(data);
	});
	
	function upload(file, onComplete) {
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
	
	function postStatus(data) {
		debug(JSON.stringify(data));
		$.post(urls.status, data, function(response) {
			switch(response.status) {
			case '200':
				addPost(response.post, true, true);
				shrinkStatus();
				break;
			case '500':
				dgte.error('Error creating post', 'There was an error creating this post. Please check your text and attachment (if any) and try again.');
				break;
			default:
				debug(response);
				dgte.error('Error creating post', 'There was an error creating this post. Please check your text and attachment (if any) and try again.');
			}
		});
		
		$newpost.find('.overlay').delay(800).fadeOut(200, function() { $(this).remove(); });
	}
	
	//posts
	var startPostIndex = 0;
	function getPosts() {
		var sort = $rdoSortPosts.length ? $rdoSortPosts.find(':checked').val() : 'popularity';
		var hasSticky = $('.post.sticky').length != 0;
		debug('Gonna be sorting by: ' + sort + ' has sticky? ' + hasSticky);
		
		$.get(urls.subposts, 
			{
				start: startPostIndex, 
				howmany: dgte.constants.postsPerPage, 
				sort: sort,
				hasSticky: hasSticky
			}, 
			
			function(response){
				switch(response.status) {
				case '200':
					debug('has featured? ' + JSON.stringify(response.featured));
					if(response.featured) {
						addPost(response.featured, true, startPostIndex != 0, true);
					}
					for(var i = 0, length = response.posts.length; i < length; ++i) {
						addPost(response.posts[i], false, startPostIndex != 0);
					}
					startPostIndex += response.posts.length;
					
					//reparse fb:comment-count
					FB.XFBML.parse();
					
					break;
				default:
					debug(response);
				}
		}).complete(function(){
			$posts.parent().fadeSpinner();
		});
		$loadmoreContainer.find('.overlay').delay(800).fadeOut(200, function() { $(this).remove(); });
	}
	getPosts();
	
	function clearPosts() {
		$posts.html('');
	}
	
	$rdoSortPosts.change(function(){
		startPostIndex = 0;
		$posts.parent().spinner(true);
		clearPosts();
		getPosts();
	});
	
	function addPost(post, prepend, fadein, sticky) {
		var posterImgSrc;
		var link;
		switch(post.type) {
		case 'user':
			posterImgSrc = post.posterImgurHash; //probably something like http://graph.facebook.com/123/picture
			link = urls.user + post.posterIdentifier;
			break;
		case 'business':
			posterImgSrc = post.posterImgurHash ? urls.imgur + post.posterImgurHash  + 's.jpg' : dgte.urls.noImage50; //something like H4qu1
			link = urls.business + post.posterIdentifier;
			break;
		default:
			debug('Illegal post type: ' + post.type);
			return;
		}
		
		var $post = $('<li class="post">').attr('postId', post.id);
		if(sticky) {
			$post.addClass('sticky');
			$('<div class="sticky-pin">')
				.attr('title', 'This is a featured post :)')
				.appendTo($post);
		}
		
		if(prepend || sticky) {
			$post.prependTo($posts);
		} else {
			$post.appendTo($posts);
		}
		
		if(fadein) {
			$post.hide().fadeIn(1500);
		}
		
		if(posterImgSrc) {
			var $picContainer = $('<div class="post-pic-container">').appendTo($post);
			var $aPostPic = $('<a class="dgte-previewlink">')
				.attr('previewtype', post.type)
				.attr('href', link)
				.appendTo($picContainer);
			$('<img class="post-pic">').attr('src', posterImgSrc).appendTo($aPostPic);	
		}
		
		//title and text
		var $dataContainer = $('<div class="post-data-container">').appendTo($post);
		var $title = $('<strong class="post-title">').appendTo($dataContainer);
		$('<a>').attr('href', urls.postdetails + post.id).html(post.title).appendTo($title);
		
		var $text = $('<div class="post-text">').appendTo($dataContainer);
		if(post.text.length < 140) {
			$text.html(post.text);
		} else {
			$text.html(post.text)
				.addClass('post-text-compressed');
			
			var $showMore = $('<div class="mt5">').insertAfter($text);
			$('<a class="showmore">')
				.text('Show full text...')
				.attr('href', urls.postdetails + post.id)
				.appendTo($showMore);
		}
		
		//attachment, if any
		switch(post.attachmentType) {
		case 'imgur':
			var $container = $('<div class="post-attachment">').appendTo($dataContainer);
			var $attachmentImgA = $('<a>').attr('href', urls.imgurPage + post.attachmentImgurHash).appendTo($container);
			$('<img class="attachment-img">').attr('src', urls.imgur + post.attachmentImgurHash + 'l.jpg').appendTo($attachmentImgA);
			break;
		case 'video':
			var $container = $('<div class="post-attachment">').appendTo($dataContainer);
			var $playbutton = $('<div class="playbutton">').text('Show Video')
				.button({icons: {secondary: 'ui-icon-play'}})
				.appendTo($container);
			$playbutton.click(function(){
				var $this = $(this);
				$this.toggleClass('showing');
				
				if($this.hasClass('showing')) {
					var $player = $('<div class="player">').appendTo($container);
					$player.html(post.attachmentIdentifier);
					$player.find('iframe').attr('width', '400').attr('height', '300');
					$this.button('option', 'label', 'Hide video').button({icons: {secondary: 'ui-icon-stop'}});
				} else {
					$container.find('.player').remove();
					$this.button('option', 'label', 'Show video').button({icons: {secondary: 'ui-icon-play'}});
				}
			});
			break;
		case 'business':
			var $container = $('<div class="post-attachment">').appendTo($dataContainer);
			var $attachmentA = $('<a class="fatlink dgte-previewlink">')
				.attr('previewtype', 'business')
				.attr('href', urls.business + post.attachmentIdentifier)
				.appendTo($container);
			
			$('<strong>').text(post.attachmentTitle).appendTo($attachmentA);
			$('<p class="attachment-description">').text(post.attachmentDescription).appendTo($container);
			//main category pic
			var $a2 = $('<a>').attr('href', urls.business + post.attachmentIdentifier).appendTo($container);
			$('<img class="category-attachment-img">').attr('src', urls.imgur + post.attachmentImgurHash + 'l.jpg').appendTo($a2);
			break;
		case 'category':
			var $container = $('<div class="post-attachment">').appendTo($dataContainer);
			//title
			var $attachmentA = $('<a class="fatlink dgte-previewlink">')
				.attr('previewtype', 'category')
				.attr('href', urls.category + post.attachmentIdentifier).appendTo($container);
			$('<strong>').text(post.attachmentTitle).appendTo($attachmentA);
			//main category pic
			var $a2 = $('<a>').attr('href', urls.category + post.attachmentIdentifier).appendTo($container);
			$('<img class="category-attachment-img">').attr('src', urls.imgur + post.attachmentImgurHash + 'l.jpg').appendTo($a2);
			$.get(urls.categoryWithProducts + post.attachmentIdentifier + '/' + dgte.home.productPreviews + '.json', function(response) {
				switch(response.status) {
				case '200':
					var description = response.category.description.length < dgte.home.attchDescLength ? response.category.description : response.category.description.substring(0, dgte.home.attchDescLength) + '...';
					$('<p class="attachment-description">')
						.text(description)
						.insertAfter($attachmentA);
					
					if(response.products && response.products.length > 0) {
						var $products = $('<div>').appendTo($container);
						var visibleProducts = 0;
						for(var prodIterator = 0, prodLength = response.products.length; prodIterator < prodLength; ++prodIterator) {
							var attachedProduct = response.products[prodIterator];
							if(attachedProduct.mainpic) {
								var $productA = $('<a class="dgte-previewlink">')
									.attr('previewtype', 'product')
									.attr('href', urls.product + attachedProduct.id).appendTo($products);
								$('<img class="category-attachment-product-img">')
									.attr('src', attachedProduct.mainpic.smallSquare)
									.attr('title', attachedProduct.name)
									.appendTo($productA);
								if(++visibleProducts >= dgte.home.productPreviews) {
									break;
								}								
							}
						}
						
						var notshown = response.moreproducts > 0 ? response.moreproducts : 0;
						notshown = notshown + response.products.length - visibleProducts;
						if(notshown > 0) {
							var $moreproducts = $('<div class="category-attachment-moreproducts">').appendTo($products);
							$('<a>').attr('href', urls.category + post.attachmentIdentifier).text(notshown + ' more...').appendTo($moreproducts);
						}
					}
					break;
				default:
					debug(response);
				}
			});
			break;
		case 'product':
			var $container = $('<div class="post-attachment">').appendTo($dataContainer);
			
			//title
			var $attachmentA = $('<a class="fatlink dgte-previewlink">')
				.attr('previewtype', 'product')
				.attr('href', urls.product + post.attachmentIdentifier).appendTo($container);
			$('<strong>').text(post.attachmentTitle).appendTo($attachmentA);
			//mainpicproduct
			if(post.attachmentImgurHash) {
				var $attachmentImgA = $('<a>')
					.attr('href', urls.product + post.attachmentIdentifier).appendTo($container);
				$('<img class="attachment-img">').attr('src', urls.imgur + post.attachmentImgurHash + 'l.jpg').appendTo($attachmentImgA);
			}
				
			$.get(urls.productwithpics + post.attachmentIdentifier + '/' + dgte.home.productPreviews + '.json', function(response) {
				switch(response.status) {
				case '200':
					if(response.pics.length > 0) {
						var description = response.product.description.length < dgte.home.attchDescLength ? response.product.description : response.product.description.substring(0, dgte.home.attchDescLength) + '...';
						$('<p class="attachment-description">')
							.text(description)
							.insertAfter($attachmentA);
						var $morepics = $('<div>').appendTo($container);
						var max = response.pics.length > dgte.home.productPreviews ? dgte.home.productPreviews : response.pics.length;
						for(var i = 0; i < max; ++i) {
							$('<a>').attr('href', response.pics[i].imgurPage).appendTo($morepics).append(
							$('<img class="product-attachment-img">')
								.attr('src', response.pics[i].smallSquare)
								.attr('title', response.pics[i].title ? response.pics[i].title : response.product.name)
							);
						}
						
						if(response.morepics > 0) {
							var $morelink = $('<div class="product-attachment-morepics">').appendTo($morepics);
							$('<a>').attr('href', urls.product + post.attachmentIdentifier).text(response.morepics + ' more...').appendTo($morelink);
						}
					}
					break;
				default:
					debug(response);
				}
			});
			break;
		case 'buyandsellitem':
			var $container = $('<div class="post-attachment">').appendTo($dataContainer);
			var $attachmentA = $('<a class="fatlink dgte-previewlink">')
				.attr('previewtype', 'buyandsellitem')
				.attr('href', urls.buyandsellitem + post.attachmentIdentifier).appendTo($container);
			
			$('<strong>').text(post.attachmentTitle).appendTo($attachmentA);
			$('<p class="attachment-description">').text(post.attachmentDescription).appendTo($container);
			//main category pic
			var $a2 = $('<a>').attr('href', urls.buyandsellitem + post.attachmentIdentifier).appendTo($container);
			$('<img class="category-attachment-img">').attr('src', urls.imgur + post.attachmentImgurHash + 'l.jpg').appendTo($a2);
			break;
		case 'link':
			var $container = $('<div class="post-attachment">').appendTo($dataContainer);
			var $linkImgContainer = $('<div class="link-preview-images">').appendTo($container);
			$('<img>').attr('src', post.attachmentImgurHash).appendTo($linkImgContainer);
			
			var $linkInfoContainer = $('<div class="link-info-container">').appendTo($container);
			$('<div class="bold">').text(post.attachmentTitle).appendTo($linkInfoContainer);
			if(post.attachmentIdentifier) {
				$('<a>').attr('href', post.attachmentIdentifier.indexOf('http') == 0 ? post.attachmentIdentifier : 'http://' + post.attachmentIdentifier).text(post.attachmentIdentifier).appendTo($linkInfoContainer);
			}
			$('<p class="linkdescription">').text(post.attachmentDescription).appendTo($linkInfoContainer);
			break;
		case 'none':
		default:
			//debug('No attachment.');
		}
		
		//footnote
		var $footnote = $('<div class="fromnow post-time">').html(moment(post.postTime).fromNow() + ' by ').appendTo($dataContainer);
		$('<a class="fatlink dgte-previewlink">')
			.attr('previewtype', post.type)
			.attr('href', link).text(post.posterTitle).appendTo($footnote);
		
		$('<span>').text(' (' + post.id + ') ').appendTo($footnote);
		
		//comments
		var $comments = $('<div class="post-comments">').appendTo($dataContainer);
		var $aComments = $('<a class="fatlink">').attr('href', urls.postdetails + post.id).appendTo($comments);

		$aComments.append('View ')
		var urlPostDetails = '${baseURL}${urlPosts}' + post.id;
		$('<fb:comments-count>').attr('href', urlPostDetails).appendTo($aComments);
		$aComments.append(' comments ');
		
	}
	
	$(document).on({
		click: function(){
			$(this)
				.hide()
				.parent()
				.siblings('.post-text-compressed')
				.removeClass('post-text-compressed');
			return false;
		}
	}, '.showmore');
	
	$posts.on({
		mouseover : function(){
			$(this).addClass('ui-state-highlight');
		},
		mouseout : function() {
			$(this).removeClass('ui-state-highlight');
		}
	}, '.post');
	
	//load more
	$('.loadmore').click(function(){
		$('<div class="overlay">').appendTo($loadmoreContainer);
		getPosts();
	});
	
	$(document).on({
		click: function(){
			hidePostasMenu();
			hideAttachMenu();
			$entitySuggestions.hide();
		}
	});
	
	$(window).resize(function(){
		matchWidths();
	});
});
</script>

<sec:authorize ifNotGranted="ROLE_USER">
<div class="grid_4 sidebar-section">
	<div class="sidebar-container">
		<img src="${logo }" />
	</div>
</div> 
</sec:authorize>

<sec:authorize access="hasRole('ROLE_USER')">
<div class="grid_4 sidebar-section">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Home</div>
		<div id="rdo-post-sort">
			<input type="radio" name="rdo-sort-order" value="subs" id="rdo-subs" checked="checked"/> <label for="rdo-subs">Subscriptions</label>
			<input type="radio" name="rdo-sort-order" value="newest" id="rdo-newest" /> <label for="rdo-newest">Newest</label>
			<input type="radio" name="rdo-sort-order" value="popularity" id="rdo-popularity" /> <label for="rdo-popularity">Popularity</label>
		</div>
	</div>
</div>

<!-- Notifications -->
<%@include file="./grids/notifications4.jsp"  %>

<!-- Reviews -->
<%@include file="./grids/reviewqueue.jsp" %>
</sec:authorize>

<!-- Sidebar Featured promos -->
<%@include file="./grids/sidebarpromos.jsp" %>

<!-- Top Tens -->
<%@include file="./grids/toptens.jsp" %>