$(function(){
	var $alphabetized = $('.alphabetized'),
		$topbusinesses = $('.topbusinesses'),
		$category = $('.category');
	
	var letters = dgte.search.letters;
	for(var i = 0, l = letters.length; i < l; ++i) {
		var letter = letters[i];
		
		var $categories = $('.category.' + letter);
		if($categories.length > 0) {
			var $group = $('<li>').appendTo($alphabetized);
			
			$('<div class="section-header">').text(letter.toUpperCase()).appendTo($group);
			
			var $sublist = $('<ul class="sublist">').appendTo($group);
			$categories.appendTo($sublist);
		}
	}
	
	$category.on({
		click: function(){
			if($topbusinesses.find('.overlay').length) return;
			$topbusinesses.spinner(true);
			var $this = $(this);
			$category.removeClass('ui-state-active');
			$this.addClass('ui-state-active');
			loadBusinesses($this.attr('categoryName'), $this.attr('categoryId'), $this.attr('businessCount'));
		},
		mouseenter: function() {
			$(this).addClass('ui-state-highlight');
		},
		mouseleave: function() {
			$(this).removeClass('ui-state-highlight');
		}
	});
	
	var responses = {};
	
	function processSuccess(categoryName, categoryId, response, businessCount) {
		$topbusinesses.find('h3,.autocomplete-container,.viewall').remove();
		$('<h3 class="category-title">').text(categoryName).appendTo($topbusinesses);
		for(var i = 0, len = response.businesses.length; i < len; ++i) {
			var business = response.businesses[i];
			addBusiness(business);
		}
		var $viewall = $('<div class="viewall">').appendTo($topbusinesses);
		var descriptionText = response.businesses.length > 1 ? 'Showing the top ' + response.businesses.length + ' businesses in ' + categoryName
				: 'Showing the only business in ' + categoryName;
		$('<div>').text(descriptionText).appendTo($viewall);
		
		var linkText = businessCount > dgte.yellowpages.preview ? 'View all ' + businessCount + ' businesses in ' + categoryName 
				: 'See additional details';
		$('<a>').attr('href', urls.categories + categoryId).text(linkText).appendTo($viewall);
		
		$('.overlay').delay(800).fadeOut(200, function() { $(this).remove(); });
	}
	
	function loadBusinesses(categoryName, categoryId, businessCount) {
		if(responses[categoryId]) {
			processSuccess(categoryName, categoryId, responses[categoryId], businessCount);
			return;
		}
		
		$.get(urls.getCategoryBusinesses + categoryId + '/' + dgte.yellowpages.preview + '.json', function(response) {
			switch(response.status) {
			case '200':
				responses[categoryId] = response;
				processSuccess(categoryName, categoryId, response, businessCount);
				debug(JSON.stringify(responses));
				break;
			default:
				debug(response);
				$topbusinesses.fadeSpinner();
			}
		});
	}
	
	function addBusiness(business) {
		var $businessContainer = $('<div class="yellowpages autocomplete-container">').attr('domain', business.identifier).appendTo($topbusinesses);
		
		$('<img class="yellowpages autocomplete-img">').attr('src', business.thumbnailHash ? urls.imgur + business.thumbnailHash + 's.jpg' : dgte.urls.blackSquareSmall).appendTo($businessContainer);
		var $title = $('<div class="yellowpages autocomplete-title">').appendTo($businessContainer);
		$('<a>').attr('href', urls.businessProfile + business.identifier).text(business.title).appendTo($title);
		$('<div class="yellowpages autocomplete-description">').html(business.description).appendTo($businessContainer);
	}
	
	$(document).on({
		click: function(){
			window.location.href = urls.businessProfile + $(this).attr('domain');
		},
		mouseenter: function() {
			$(this).addClass('ui-state-highlight');
		},
		mouseleave: function() {
			$(this).removeClass('ui-state-highlight');
		}
	}, '.yellowpages.autocomplete-container');
	
	//topbusinesses
	var originalOffsetTop = $topbusinesses.offset().top;
	var originalPositionTop = $topbusinesses.position().top;
	
	var i = 0;
	$(window).scroll(function(event) {
		if($(this).scrollTop() + 13 > originalOffsetTop) {
			$topbusinesses.css({'position': 'fixed', 'top': 0});
		} else {
			$topbusinesses.css({'position': 'relative', 'top': 0});
		}
	});
	
	//load first
	$('.category:first').click();
});