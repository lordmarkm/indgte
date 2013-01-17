$(function(){
	
	var $categoryPic = $('.category-welcome-image'),
		$businessPic = $('.category-provider-image'),
		$summaryContainer = $('.summary-container'),
		$summary = $('.products-summary'),
		$products = $('.products');
	
	$categoryPic.attr('src', category.imageUrl ? category.imageUrl : urls.noimage);
	$businessPic.attr('src', business.businessPic ? business.businessPic : noimage50);
	
	if(!category.owner) {
		if(category.imageUrl) {
			$categoryPic.click(function(){
				window.location.href = category.categoryPicImgur;
			});
		} else {
			$categoryPic.css('cursor', 'default');
		}
	}
	
	$businessPic.click(function(){
		window.location.href = urls.profile + business.domain;
	});
	
	$.get(urls.products + business.domain + '/' + category.id + '/json', function(response){
		switch(response.status){
		case '200':
			if(response.products.length < 2) {
				$summaryContainer.hide();
			}
			for(var i = 0, length = response.products.length; i < length; i++) {
				addProduct(response.products[i]);
			}
			break;
		default:
			debug(response);
		}
	});
	
	function addProduct(product, $parent, prepend) {
		if(!$parent) {
			$parent = this.$products;
		}
		var $summaryItem = $('<li>').appendTo(this.$summary);
		$('<a href="#' + product.id + '">').text(dgte.htmlDecode(product.name)).appendTo($summaryItem);
		
		var $actualItem = $('<li class="product">')
			.on('mouseover', function(){$(this).addClass('ui-state-highlight');})
			.on('mouseout', function(){$(this).removeClass('ui-state-highlight');})
			.appendTo(this.$products);
		var $container = $('<div class="product-container">').appendTo($actualItem);

		if(product.mainpic) {
			var $picContainer = $('<div class="product-pic-container">').appendTo($container);
			$('<img class="product-pic">').attr('src', product.mainpic.smallSquare).appendTo($picContainer);
		}
		
		var $title = $('<a class="fatlink dgte-previewlink"id="' + product.id + '">').attr('previewtype', 'product').attr('href', urls.urlProducts + this.domain + '/' + product.id).appendTo($container);
		var $productName = $('<strong class="product-name">').text(dgte.htmlDecode(product.name)).appendTo($title)
		
		$.get(urls.urlProducts + constants.domain + '/' + product.id + '/pics/' + constants.previewPicsCount + '.json', function(response){
			switch(response.status){
			case '200':
				for(var i = 0; i < response.imgurs.length; ++i) {
					$('<img class="product-otherpics-preview">').attr('src', response.imgurs[i].smallSquare).appendTo($productName);
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
});
