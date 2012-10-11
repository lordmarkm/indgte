<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="noimage" value="/resources/images/noimage.jpg" />
<spring:url var="spinner" value="/resources/images/spinner.gif" />
<spring:url var="urlProfileRoot" value="/p/" />
<spring:url var="urlPosts" value="/i/posts/" />
<spring:url var="urlBusinessPost" value="/i/posts/business/" />

<link rel="stylesheet" href="<spring:url value='/resources/css/businessprofile.css' />" />

<c:choose>
<c:when test="${not empty business.profilepic }">
<spring:url var="urlSmallProfilepic" value="http://i.imgur.com/${business.profilepic.hash }s.jpg" />
</c:when>
<c:otherwise>
<spring:url var="urlSmallProfilepic" value="/resources/images/noimage.jpg" />
</c:otherwise>
</c:choose>

<title>${business.fullName }</title>

<div class="cover grid_10">
	<c:if test="${not empty business.coverpic }">
	<a href="http://imgur.com/${business.coverpic.hash }"><img class="coverpic" src="http://i.imgur.com/${business.coverpic.hash }l.jpg" /></a>
	</c:if>
</div>

<div class="rightbar grid_2">&nbsp;
</div>

<div class="content grid_10">
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
</div>

<div class="content-info">
	<h2 class="ui-widget-header fullname">${business.fullName }</h2>
	<div class="content-info-indented grid_7">
		<div class="description">${business.description }</div>
		<div class="biz-url">www.indumaguete.com/p/${business.domain }</div>
		<div class="biz-owner"><a href="${urlProfileRoot}user/${business.owner.username}">${business.owner.username }</a></div>
	</div>
</div>

<div class="tabs">
	<ul>
		<li><a href="#feed"><spring:message code="business.profile.tabs.feed" /></a></li>
		<li><a href="#catalog"><spring:message code="business.profile.tabs.products" /></a></li>
		<li><a href="#reviews"><spring:message code="business.profile.tabs.reviews" /></a></li>
		<li><a href="#comments"><spring:message code="business.profile.tabs.comments" /></a>
	</ul>
	
	<!-- Business's feed - shows posts by the business -->
	<div id="feed">
		<c:if test="${owner }">
		<div class="newpost">
			<img class="newpost-poster-pic" src=${urlSmallProfilepic } />
			<div class="newpost-poster">
				<div class="newpost-posting-as"><spring:message code="business.newpost.postingas" /></div>
				<strong class="newpost-poster-name">${business.fullName }</strong>
			</div>
			<div class="newpost-action">
				<div class="newpost-title-label"><spring:message code="business.newpost.title" /></div>
				<input type="text" class="newpost-title" maxlength="250" />
				<div><textarea class="newpost-text"></textarea></div>
				<button class="newpost-submit"><spring:message code="business.newpost.buttontext" /></button>
			</div>
		</div>
		</c:if>
		
		<div class="feed-posts-container">
			<ul class="feed-posts"></ul>
		</div>
	</div>
	
	<div id="catalog">
		<c:if test="${owner }">
		</c:if>	
	</div>
	
	<div id="reviews">Reviews</div>
	<div id="comments">Comments</div>
</div>

</div>

<script>
$(function(){
	var domain = '${business.domain}',
		fullName = '${business.fullName}',
		businessId = '${business.id}';
	
	var hasPic = '${not empty business.profilepic }' === 'true';
		
	var urlPosts = '${urlPosts}',
		urlSmallProfilepic = '${urlSmallProfilepic}';
	
	var $posts = $('.feed-posts');
	
	//tabs lol
	$('.tabs').tabs();
	
	//load the last 10 posts by this business
	function reloadPosts() {
		$.get(urlPosts, 
			{
				posterId : businessId,
				type : 'business',
				start : 0,
				howmany: 15
			},
			function(response) {
				switch(response.status) {
				case '200':
					if(response.posts.length === 0) {
						$posts.append($('<span class="removeme">').text('No posts yet from ' + fullName));
					} else {
						for(var i = response.posts.length - 1; i > -1; --i) {
							addPost(response.posts[i]);
						}
					}
					break;
				default:
					debug(response);
				}
			}
		);
	}
	reloadPosts();
	
	function addPost(post) {
		var $post = $('<li class="post">').appendTo($posts);
		if(hasPic) {
			var $picContainer = $('<div class="post-pic-container">').appendTo($post);
			$('<img class="post-pic">').attr('src', urlSmallProfilepic).appendTo($picContainer);
		}
		
		var $dataContainer = $('<div class="post-data-container">').appendTo($post);
		var $title = $('<strong class="post-title">').appendTo($dataContainer);
		$('<a>').attr('href', urlPosts + post.id).html(post.title).appendTo($title);
		var $text = $('<div class="post-text">').html(post.text).appendTo($dataContainer);
		var $date = $('<div class="fromnow post-time">').html(moment(post.postTime).fromNow()).appendTo($dataContainer);
	}
	
	function debug(m) {
		console.debug(m);
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
	
	//posts vars
	var $posts = $('.feed-posts'),
		$btnNewpostSubmit = $('.newpost-submit'),
		$newpostTitle = $('.newpost-title'),
		$newpostText = $('.newpost-text');
	
	//override jquery ui dialog defaults
	$.extend($.ui.dialog.prototype.options, {
	    modal: true,
	    resizable: false,
	    maxHeight: 250,
	    width:500,
		closeOnEscape: false,
		hide: {effect: "fade", duration: 500}
	});
	
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
	
	//from http://paulrouget.com/miniuploader/
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
	        		window.location.replace(urlProfileRoot + domain);
		        });
	        }, 2000);
	    }
		xhr.send(fd);
	}
	
	//new post
	$btnNewpostSubmit.click(function(){
		var title = $newpostTitle.val();
		var text = $newpostText.val();
		
		var post = {posterId:businessId, title:title, text:text};
		$.post(urlBusinessPost, post, function(response) {
			switch(response.status){
			case '200':
				addPost(response.post);
				$newpostTitle.val('');
				$newpostText.val('');
				break;
			default:
				debug(response);
			}
		});
	});
	
	//is different from the other one because latest post needs to be PREpended to $posts and also have 
	//the fadeIn effect and also remove the no-posts-yet span if it's there
	//yeah this separate function is totally justified now
	function addPost(post) {
		$posts.find('.removeme').remove();
		var $post = $('<li class="post">').prependTo($posts).hide().fadeIn(1500);
		if(hasPic) {
			var $picContainer = $('<div class="post-pic-container">').appendTo($post);
			$('<img class="post-pic">').attr('src', urlSmallProfilepic).appendTo($picContainer);
		}
		
		var $dataContainer = $('<div class="post-data-container">').appendTo($post);
		var $title = $('<strong class="post-title">').appendTo($dataContainer);
		$('<a>').attr('href', urlPosts + post.id).html(post.title).appendTo($title);
		var $text = $('<div class="post-text">').html(post.text).appendTo($dataContainer);
		var $date = $('<div class="fromnow post-time">').html(moment(post.postTime).fromNow()).appendTo($dataContainer);
	}
});
</script>
</c:if>

<spring:url var="jsApplication" value="/resources/javascript/application.js" />
<script type="text/javascript" src="${jsApplication }"></script>