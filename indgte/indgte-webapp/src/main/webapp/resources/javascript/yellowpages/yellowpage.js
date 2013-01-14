$(function(){
	$('.businesses').columnize({
		columns: 3,
		dontsplit: 'div',
		columnClass: 'businesses-column'
	});
	
	$('.businesses-column').each(function(i, column) {
		var $column = $(column);
		if($column.find('.business').length == 0) {
			$column.hide();
		}
	});
	
	//posts
	var 
		$posts = $('.posts'),
		$loadmoreContainer = $('.loadmoreContainer'),
		$loadmore = $('.loadmore');
	
	var startPostIndex = 0;
	function getPosts() {
		var hasSticky = $('.post.sticky').length != 0;

		$.get(urls.groupPosts, 
			{
				groupId: group.id,
				start: startPostIndex, 
				howmany: dgte.constants.postsPerPage, 
			}, 
			
			function(response){
				switch(response.status) {
				case '200':
					debug('has featured? ' + JSON.stringify(response.featured));
					if(response.featured && !hasSticky) {
						addPost(response.featured, true, startPostIndex != 0, true);
					}
					for(var i = 0, length = response.posts.length; i < length; ++i) {
						addPost(response.posts[i], false, startPostIndex != 0);
					}
					startPostIndex += response.posts.length;
					
					if(response.posts.length < dgte.constants.postsPerPage) {
						$loadmoreContainer.hide();
					}
					
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
		var urlPostDetails = urls.urlPost + post.id;
		$('<fb:comments-count>').attr('href', urlPostDetails).appendTo($aComments);
		$aComments.append(' comments ');
		
	}
	
	//load more
	$('.loadmore').click(function(){
		$loadmoreContainer.spinner(true);
		getPosts();
	});
});