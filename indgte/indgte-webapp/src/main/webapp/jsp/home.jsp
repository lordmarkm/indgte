<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<title>${user.username } | InDumaguete</title>
<link rel="stylesheet" href="<spring:url value='/resources/css/lists.css' />" />

<div class="grid_12">

<section class="newpost" style="margin-top: 10px; height: 160px;">
<div id="tabs">
	<ul>
		<li><a href="#status">Status</a></li>
		<li><a href="#product">Promote</a></li>
	</ul>
	
	<div id="status">
		<textarea class="status-textarea" placeholder="<spring:message code="home.status.textarea" />" rows="3"></textarea>	
		
		<div class="status-options">
			<sec:authorize access="hasRole('ROLE_USER_FACEBOOK')">
			<input type="checkbox" name="toFacebook" id="toFacebook" value="true"><label for="toFacebook"><spring:message code="home.status.postfb" /></label>
			</sec:authorize>
			
			<div class="floatright">
				<div class="post-as">
					<div class="post-as-image"></div>
					<div class="arrow-container"></div>
				</div>
				<div class="button">Post</div>
			</div>
		</div>
	</div>
	
	<div id="product">New product here</div>
</div>
</section>

<section class="feedcontainer">
	<ul class="posts"></ul>
</section>

</div>
<style>
.ui-tabs {
	font-size: 0.8em;
}

.status-textarea {
	width: 540px;
	resize: none;
}

.status-options {
	width: 540px;
}
</style>

<script type="text/javascript" src="${jsApplication }"></script>
<script>
window.urls = {
	subposts : '<spring:url value="/i/subposts.json" />',
	postdetails : '<spring:url value="/i/posts/" />',
	business : '<spring:url value="/p/" />',
	imgur : 'http://i.imgur.com/',
}

$(function(){
	$('#tabs').tabs();
	
	//postsui-state-default
	var $feedcontainer = $('.feedcontainer'),
		$posts = $('.posts');
	
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
	
	function addPost(post) {
		var $post = $('<li class="post">').appendTo($posts);
		
		if(post.posterImgurHash) {
			var $picContainer = $('<div class="post-pic-container">').appendTo($post);
			$('<img class="post-pic">').attr('src', urls.imgur + post.posterImgurHash + 's.jpg').appendTo($picContainer);	
		}
		
		var $dataContainer = $('<div class="post-data-container">').appendTo($post);
		var $title = $('<strong class="post-title">').appendTo($dataContainer);
		$('<a>').attr('href', urls.postdetails + post.id).html(post.title).appendTo($title);
		var $text = $('<div class="post-text">').html(post.text).appendTo($dataContainer);
		
		var $footnote = $('<div class="fromnow post-time">').html(moment(post.postTime).fromNow() + ' by ').appendTo($dataContainer);
		
		var link;
		switch(post.type) {
		case 'business':
			link = urls.business + post.posterIdentifier;
			break;
		default:
		}
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
});
</script>