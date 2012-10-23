<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="paperclip" value="/resources/images/icons/paperclip.png" />

<title>${user.username } | InDumaguete</title>
<link rel="stylesheet" href="<spring:url value='/resources/css/lists.css' />" />
<link rel="stylesheet" href="<spring:url value='/resources/css/home.css' />" />

<script src="http://ajax.microsoft.com/ajax/jQuery.Validate/1.6/jQuery.Validate.min.js"></script>

<div class="grid_7">

<section class="newpost">
	<form id="form-newpost">
		<div class="status-title-container">
			<input name="title" class="status-title ui-state-active hide" type="text" placeholder="title" />
		</div>
		<textarea name="text" class="status-textarea noattachment" maxlength="140" rows="1" placeholder="<spring:message code="home.status.textarea" />"></textarea>	
	
		<div class="attach-input-container">
			<input class="posterId" type="hidden" value="${user.id }" />
			<input class="posterType" type="hidden" value="user" />
			<input class="iptType" type="hidden" value="none"/>
			<input class="iptFile" type="file" />
			<input class="iptUrl" type="text" placeholder="Paste URL"/>
			<input class="iptEntity" type="text" placeholder="Enter Entity name" />
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
					<c:forEach items="${businesses }" var="business">
					<div class="post-as-option" posterId="${business.id }" posterType="business">
						<img src="${business.profilepic.smallSquare }" />
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
	</div>
</section>

<section class="feedcontainer">
	<ul class="posts"></ul>
</section>

</div>

<script type="text/javascript" src="${jsApplication }"></script>
<script>
window.constants = {
	imgurKey : '${imgurKey}'
}
window.urls = {
	status : '<spring:url value="/i/newstatus.json" />',
	subposts : '<spring:url value="/i/subposts.json" />',
	postdetails : '<spring:url value="/i/posts/" />',
	user : '<spring:url value="/p/user/" />',
	business : '<spring:url value="/p/" />',
	imgur : 'http://i.imgur.com/',
	imgurPage : 'http://imgur.com/'
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
		$statusCounter = $('.status-counter'),
		$statusOptions = $('.status-options'),
		$menubutton = $('.menubutton'),
		$postas = $('.post-as'),
		$postasVisibles = $('.post-as-visibles'),
		$postasMenu = $('.post-as-menu').hide(),
		$attach = $('.attach'),
		$attachVisibles = $('.attach-visibles'),
		$attachMenu = $('.attach-menu').hide(),
		$btnPost = $('.btn-post');
	
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
		$statusOptions.hide();
	}
	
	function matchWidths() {
		$title.css('width', $status.width());
		$errors.css('width', $status.width() + 4);
		$attachInputContainer.css('width', $status.width() + 4)
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
		matchWidths();
		checkActiveAttachment();
	}
	
	function checkStatus() {
		var status = $status.val();
		if(!status.trim()) {
			shrinkStatus();
		} else {
			expandStatus();
		}
	}
	checkStatus();
	
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
	}
	
	$('.attach-option').click(function(){
		$('<div class="overlay">').appendTo($newpost).delay(800).fadeOut(200, function() { $(this).remove(); });
	});
	
	$('.attach-option.image').click(function(){
		$attachInputContainer.show();
		hideAttachInputs();
		$attachType.val('image');
		$status.removeClass('noattachment');
		$iptFile.show();
	});
	
	$('.attach-option.video').click(function(){
		$attachInputContainer.show();
		hideAttachInputs();
		$attachType.val('video');
		$status.removeClass('noattachment');
		$iptUrl.attr('placeholder', 'Paste video URL').show();
	});

	$('.attach-option.link').click(function(){
		$attachInputContainer.show();
		hideAttachInputs();
		$attachType.val('link');
		$status.removeClass('noattachment');
		$iptUrl.attr('placeholder', 'Paste link URL').show();
	});
	
	$('.attach-option.entity').click(function(){
		$attachInputContainer.show();
		hideAttachInputs();
		$attachType.val('entity');
		$status.removeClass('noattachment');
		$iptEntity.show();
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
			case 'image':
				$iptFile.show();
				break;
			case 'video':
				$iptUrl.attr('placeholder', 'Paste video URL').show();
				break;
			case 'link':
				$iptUrl.attr('placeholder', 'Paste link URL').show();
				break;
			case 'entity':
				$iptEntity.show();
				break;
			}
		}
	}
	checkActiveAttachment();
	
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
	
	$form.validate({
		rules: {
			title: {
				required: true,
				rangelength: [1, 45]
			},
			text: {
				required: true,
				maxlength: 140
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
		case 'image':
			data.attachmentType = 'image';
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
			break;
		case 'entity':
			debug('attaching entity.');
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
				addPost(response.post, true);
				break;
			default:
				debug(response);
			}
		});
		
		$newpost.find('.overlay').delay(800).fadeOut(200, function() { $(this).remove(); });
	}
	
	//posts
	$.get(urls.subposts, {start: 0, howmany: dgte.constants.postsPerPage}, function(response){
		switch(response.status) {
		case '200':
			for(var i = 0, length = response.posts.length; i < length; ++i) {
				addPost(response.posts[i]);
			}
			break;
		default:
			debug(response);
		}
	});
	
	function addPost(post, prepend) {
		var posterImgSrc;
		var link;
		switch(post.type) {
		case 'user':
			posterImgSrc = post.posterImgurHash; //probably something like http://graph.facebook.com/123/picture
			link = urls.user + post.posterIdentifier;
			break;
		case 'business':
			posterImgSrc = post.posterImgurHash ? urls.imgur + post.posterImgurHash  + 's.jpg' : null; //something like H4qu1
			link = urls.business + post.posterIdentifier;
			break;
		default:
			debug('Illegal post type: ' + post.type);
			return;
		}
		
		var $post = $('<li class="post">').attr('postId', post.id);
		
		if(prepend) {
			$post.prependTo($posts).hide().fadeIn(1500);;
		} else {
			$post.appendTo($posts);
		}
		
		if(posterImgSrc) {
			var $picContainer = $('<div class="post-pic-container">').appendTo($post);
			$('<img class="post-pic">').attr('src', posterImgSrc).appendTo($picContainer);	
		}
		
		//title and text
		var $dataContainer = $('<div class="post-data-container">').appendTo($post);
		var $title = $('<strong class="post-title">').appendTo($dataContainer);
		$('<a>').attr('href', urls.postdetails + post.id).html(post.title).appendTo($title);
		var $text = $('<div class="post-text">').html(post.text).appendTo($dataContainer);
		
		//attachment, if any
		switch(post.attachmentType) {
		case 'image':
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
		case 'none':
		default:
			//debug('No attachment.');
		}
		
		//footnote
		var $footnote = $('<div class="fromnow post-time">').html(moment(post.postTime).fromNow() + ' by ').appendTo($dataContainer);
		$('<a>').attr('href', link).text(post.posterTitle).appendTo($footnote);
	}
	
	$posts.on({
		mouseover : function(){
			$(this).addClass('ui-state-highlight');
		},
		mouseout : function() {
			$(this).removeClass('ui-state-highlight');
		}
	}, '.post');
	
	$(document).click(function(){
		hidePostasMenu();
		hideAttachMenu();
	});
	
	$(window).resize(function(){
		matchWidths();
	});
});
</script>