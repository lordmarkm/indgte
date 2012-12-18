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
		var $linkImgContainer = $('<div class="link-preview-images">').appendTo($container);
		$('<img>').attr('src', post.attachmentImgurHash).appendTo($linkImgContainer);
		
		var $linkInfoContainer = $('<div class="link-info-container">').appendTo($container);
		$('<div class="bold">').html(post.attachmentTitle).appendTo($linkInfoContainer);
		$('<a>').attr('href', post.attachmentIdentifier.indexOf('http') == 0 ? post.attachmentIdentifier : 'http://' + post.attachmentIdentifier).html(post.attachmentIdentifier).appendTo($linkInfoContainer);
		$('<p class="linkdescription">').html(post.attachmentDescription).appendTo($linkInfoContainer);
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
	
	//promote
	var 
		$btnPromote = $('.btn-promote'),
		$dlgPromote = $('.dialog-promote'),
		$frmPromote = $('.form-promote'),
		$startDate = $('#start-date'),
		$endDate = $('#end-date'),
		$coconutCost = $('.coconut-cost');
	
	$frmPromote.validate({
		rules: {
			startDate: {
				required: true
			},
			endDate: {
				required: true
			}
		}
	});
	
	$startDate.datepicker({
		minDate: '0'
	}).change(function(){
		var startDate = new Date($startDate.val());
		var maxdays = user.coconuts - 1;
		var lastPossibleDate = new Date(startDate);
		lastPossibleDate.setTime(lastPossibleDate.getTime() + (maxdays * 24 * 60 * 60 * 1000));
		$endDate.datepicker('option', 'minDate', startDate)
				.datepicker('option', 'maxDate', lastPossibleDate);
		computeCoconutCost();
	});
	
	$endDate.datepicker({
		minDate: '0',
		maxDate: '+' + (user.coconuts - 1) + 'd'
	}).change(computeCoconutCost);
	
	function computeCoconutCost() {
		var startDate = new Date($startDate.val());
		var endDate = new Date($endDate.val());
		if(!isNaN(startDate.getTime()) && !isNaN(endDate.getTime())) {
			var coconutCost = ((endDate - startDate) / 1000 / 60 / 60 / 24) + 1;
			$coconutCost.text('This promotion would cost ' + coconutCost + ' coconuts.');
		}
	}
	
	$btnPromote.click(function(){
		$dlgPromote.dialog({
			buttons: {
				'OK'	 : function(){
					$frmPromote.submit();
				},
				'Cancel' : function(){
					$(this).dialog('close');
				}
			}
		});
	});
});

window.fbAsyncInit = function() {
	FB.Event.subscribe('comment.create', function(event) {
		debug(event);
		FB.getLoginStatus(function(loginStatus) {
			if (loginStatus.status === 'connected') {
					//user is logged in. get details and notify poster of new comment
				FB.api('/me', function(response) {
						$.post(urls.commentNotify + post.id + '/json', {
						name : response.name,
						providerUserId : response.id,
						providerUsername : response.name
					}, function(response) {
						//do nothing for now
					});
				});
			} else {
				debug('Somebody has commented with Yahoo!, AOL, or some other non-Facebook account');
				$.post(urls.commentNotify + post.id + '/json', {
					name : 'Somebody',
					providerUserId : 'none',
					providerUsername : 'none'
				});
			}
		});
	});
	
	FB.Event.subscribe('comment.remove', function(event) {
		$.post(urls.commentRemove + post.id + '/json');
	});
	
	FB.Event.subscribe('edge.create', function() {
		FB.getLoginStatus(function(loginStatus) {
			if (loginStatus.status === 'connected') {
					//user is logged in. get details and notify poster of new comment
				FB.api('/me', function(response) {
						$.post(urls.likeNotify + post.id + '/json', {
						name : response.name,
						providerUserId : response.id,
						providerUsername : response.name
					}, function(response) {
						//do nothing for now
					});
				});
			} else {
				debug('Somebody has commented with Yahoo!, AOL, or some other non-Facebook account');
				$.post(urls.likeNotify + post.id + '/json', {
					name : 'Somebody',
					providerUserId : 'none',
					providerUsername : 'none'
				});
			}
		});
	});
	
	FB.Event.subscribe('edge.remove', function(event) {
		$.post(urls.unlike + post.id + '/json');
	});
}