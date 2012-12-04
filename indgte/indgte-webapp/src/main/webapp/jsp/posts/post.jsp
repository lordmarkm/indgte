<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../tiles/links.jsp" %>

<title>${post.title }</title>
<script type="text/javascript" src="${jsApplication }"></script>
<link rel="stylesheet" href="<spring:url value='/resources/css/lists.css' />" />

<div class="grid_8">

	<div class="post">
		<div class="post-pic-container">
			<c:if test="${post.type eq 'business' }">
				<img class="post-pic" src="${urlImgRoot }${post.posterImgurHash}s.jpg" />
			</c:if>
			<c:if test="${post.type eq 'user' }">
				<img class="post-pic" src="${post.posterImgurHash}" />
			</c:if>
		</div>
				
		<div class="post-data-container">
			<strong class="post-title">${post.title }</strong>
			<div class="post-text">${post.text }</div>
		</div>
		
		<div class="post-fb">
			<div class="fb-like" data-send="true" data-width="450" data-show-faces="true"></div>
			<div class="fb-comments" data-href="${domain}${urlPosts }${post.id}" data-width="540"></div>
		</div>
	</div>

</div>

<script>
window.user = {
	username : '${user.username}'	
}

window.post = {
	id : '${post.id}',
	type : '${post.type}',
	attachmentType : '${post.attachmentType}',
	attachmentImgurHash : '${post.attachmentImgurHash}',
	attachmentIdentifier : '${post.attachmentIdentifier}',
	postTime : '${post.postTime.time }',
	posterIdentifier : '${post.posterIdentifier}',
	posterTitle: '${post.posterTitle }'
}

window.urls = {
	user : '<spring:url value="/p/user/" />',
	business : '<spring:url value="/" />',
	imgur : 'http://i.imgur.com/',
	imgurPage : 'http://imgur.com/',
	categoryWithProducts: '<spring:url value="/b/categories/" />',
	productwithpics: '<spring:url value="/b/products/withpics/" />',
	commentNotify: '<spring:url value="/i/commentnotify/post/" />',
	likeNotify: '<spring:url value="/i/likenotify/post/" />'
}

$(function(){
	var $dataContainer = $('.post-data-container');
	
	//attachment, if any
	switch(post.attachmentType) {
	case 'imgur':
		var $container = $('<div class="post-attachment">').appendTo($dataContainer);
		var $attachmentImgA = $('<a>').attr('href', urls.imgurPage + post.attachmentImgurHash).appendTo($container);
		$('<img class="attachment-img-large">').attr('src', urls.imgur + post.attachmentImgurHash + 'l.jpg').appendTo($attachmentImgA);
		break;
	case 'video':
		var $container = $('<div class="post-attachment">').appendTo($dataContainer);
		var $player = $('<div class="player">').appendTo($container);
		$player.html(post.attachmentIdentifier);
		$player.find('iframe').attr('width', '540').attr('height', '405');
		break;
	case 'category':
		var $container = $('<div class="post-attachment">').appendTo($dataContainer);
		//title
		var $attachmentA = $('<a>').attr('href', urls.category + post.attachmentIdentifier).appendTo($container);
		$('<h4>').text(post.attachmentTitle).appendTo($attachmentA);
		//main category pic
		var $a2 = $('<a>').attr('href', urls.category + post.attachmentIdentifier).appendTo($container);
		$('<img class="category-attachment-img">').attr('src', urls.imgur + post.attachmentImgurHash + 'l.jpg').appendTo($a2);
		//product previews			
		$.get(urls.categoryWithProducts + post.attachmentIdentifier + '/' + dgte.home.productPreviews + '.json', function(response) {
			switch(response.status) {
			case '200':
				var description = response.category.description.length < dgte.home.attchDescLength ? response.category.description : response.category.description.substring(0, dgte.home.attchDescLength) + '...';
				$('<div class="attachment-description">')
					.text(description)
					.insertAfter($attachmentA);
				
				if(response.products && response.products.length > 0) {
					var $products = $('<div>').appendTo($container);
					var visibleProducts = 0;
					for(var prodIterator = 0, prodLength = response.products.length; prodIterator < prodLength; ++prodIterator) {
						var attachedProduct = response.products[prodIterator];
						if(attachedProduct.mainpic) {
							var $productA = $('<a>').attr('href', urls.product + attachedProduct.id).appendTo($products);
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
		var $attachmentA = $('<a>').attr('href', urls.product + post.attachmentIdentifier).appendTo($container);
		$('<h4>').text(post.attachmentTitle).appendTo($attachmentA);
		//mainpicproduct
		if(post.attachmentImgurHash) {
			var $attachmentImgA = $('<a>').attr('href', urls.product + post.attachmentIdentifier).appendTo($container);
			$('<img class="attachment-img">').attr('src', urls.imgur + post.attachmentImgurHash + 'l.jpg').appendTo($attachmentImgA);
		}
			
		$.get(urls.productwithpics + post.attachmentIdentifier + '/' + dgte.home.productPreviews + '.json', function(response) {
			switch(response.status) {
			case '200':
				if(response.pics.length > 0) {
					var description = response.product.description.length < dgte.home.attchDescLength ? response.product.description : response.product.description.substring(0, dgte.home.attchDescLength) + '...';
					$('<div class="attachment-description">')
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
	case 'link':
		var $container = $('<div class="post-attachment">').appendTo($dataContainer);
		$('<a>').attr('href', post.attachmentIdentifier).text(post.attachmentIdentifier).appendTo($container);
		break;
	case 'none':
	default:
		//debug('No attachment.');
	}
	
	//footnote
	var link;
	switch(post.type) {
		case 'user':
			link = urls.user + post.posterIdentifier;
			break;
		case 'business':
			link = urls.business + post.posterIdentifier;
			break;
		default:
			debug('Illegal post type: ' + post.type);
			return;
	}
	var $footnote = $('<div class="fromnow post-time">').html(moment(parseInt(post.postTime)).fromNow() + ' by ').appendTo($dataContainer);
	$('<a>').attr('href', link).text(post.posterTitle).appendTo($footnote);
});
</script>

<div id="fb-root"></div>
<script>
(function(d, s, id) {
	var js, fjs = d.getElementsByTagName(s)[0];
	if (d.getElementById(id))
		return;
	js = d.createElement(s);
	js.id = id;
	js.src = "//connect.facebook.net/en_GB/all.js#xfbml=1&appId=270450549726411";
	fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

window.fbAsyncInit = function() {
	FB.Event.subscribe('comment.create', function() {
		FB.getLoginStatus(function(loginStatus) {
			if (loginStatus.status === 'connected') {
					//user is logged in. get details and notify poster of new comment
				FB.api('/me', function(response) {
						$.post(urls.commentNotify + post.id + '/json', {
						providerUserId : response.id,
						providerUsername : response.name
					}, function(response) {
						//do nothing
					});
				});
			}
		});
	});
	
	FB.Event.subscribe('edge.create', function() {
		FB.getLoginStatus(function(loginStatus) {
			if (loginStatus.status === 'connected') {
					//user is logged in. get details and notify poster of new comment
				FB.api('/me', function(response) {
						$.post(urls.likeNotify + post.id + '/json', {
						providerUserId : response.id,
						providerUsername : response.name
					}, function(response) {
						//do nothing
					});
				});
			}
		});
	});
}
</script>