$(function(){
	var $btnShowUsermenu = $('.menu-container'),
		$userMenuTopBorder = $('.user-menu-partialborder');
		$userMenu = $('.user-menu')
		$userMenuItem = $('.user-menu-item'),
		$btnSearch = $('.search-container button'),
		$searchInput = $('.search-input'),
		$autocompleteResults = $('.autocomplete-results')
		$navigation = $('.navigation'),
		$mainSearchForm = $('#main-search');
	
	$mainSearchForm.submit(function(){
		var term = $searchInput.val();
		if(term.length < navbar.search.minlength) return false;
		
		window.location.href = navbar.search.url + term;
		
		return false;
	});
		
	$btnSearch.button({ icons: {primary: 'ui-icon-search'}}).click(function(){
		$mainSearchForm.submit();
	});
	
	var searchtimeout;
	$searchInput.bind({
		keyup: startTimeout,
		paste: startTimeout,
		focus: startTimeout
	});
	
	function startTimeout() {
		if(searchtimeout) {
			clearTimeout(searchtimeout);
		}
		searchtimeout = setTimeout(autocomplete, 500);
	}
	
	function autocomplete() {
		var term = $searchInput.val();
		if(term.length < navbar.search.minlength) return;
		$.get(navbar.search.url + term + '/json', function(response){
			switch(response.status) {
			case '200':
				$autocompleteResults.html('');
				
				debug(response);
				var visibleResults = 0;
				
				function makeAutocompleteResult(result, urlRoot) {
					var $auto = $('<div class="autocomplete-container">').appendTo($autocompleteResults);
					$('<img class="autocomplete-img">').attr('src', result.thumbnailHash ? navbar.search.urlImgur + result.thumbnailHash + 's.jpg' : dgte.urls.blackSquareSmall).appendTo($auto);
					$('<div class="autocomplete-title">').append($('<a>').attr('href', urlRoot + result.identifier).text(result.title)).appendTo($auto);
					
					if(result.description.length < 80) {
						$('<div class="autocomplete-description">').text(result.description).appendTo($auto);
					} else {
						var $morelink = $('<a style="font-weight: bold;">').attr('href', urlRoot + result.identifier).text('more...');
						$('<div class="autocomplete-description">').text(result.description.substring(0, 80) + '... ')
							.append($morelink)
							.appendTo($auto);
					}
					++visibleResults;
				}
				
				if(response.business && response.business.length > 0) {
					$('<div class="ui-widget-header">').text('Businesses').appendTo($autocompleteResults);
					for(var i = 0, length = response.business.length; i < length; i++) {
						makeAutocompleteResult(response.business[i], navbar.search.urlBusiness);
					}
				}
				
				if(response.category && response.category.length > 0) {
					$('<div class="ui-widget-header">').text('Categories').appendTo($autocompleteResults);
					for(var i = 0, length = response.category.length; i < length; i++) {
						makeAutocompleteResult(response.category[i], navbar.search.urlCategory);
					}
				}
				
				if(response.product && response.product.length > 0) {
					$('<div class="ui-widget-header">').text('Products').appendTo($autocompleteResults);
					for(var i = 0, length = response.product.length; i < length; i++) {
						makeAutocompleteResult(response.product[i], navbar.search.urlProduct);
					}
				}
				
				var $deepSearch = $('<div class="autocomplete-allresults">').appendTo($autocompleteResults);
				$('<a class="bold">').attr('href', navbar.search.url + term).text('See all results').appendTo($deepSearch);
				$('<span class="normalweight">').text(' or ').appendTo($deepSearch);
				$('<a class="bold">').attr('href', navbar.search.filter + term).text('filter posts for tag "' + term + '"').appendTo($deepSearch);
				
				
				$autocompleteResults.show();
				//if(visibleResults) {
				//	$autocompleteResults.show();
				//} else {
				//	$autocompleteResults.hide();
				//}
				break;
			default:
				debug('Error' + JSON.stringify(response));
			}
		});
	}
	
	$autocompleteResults.on({
		mouseover: function(){$(this).addClass('ui-state-active')}, 
		mouseout:  function(){$(this).removeClass('ui-state-active')}
	}, '.autocomplete-container' );
	
	
	
	$(document).on({
		mouseenter : function(){
			var $this = $(this);
			if($this.hasClass('ui-state-active')) return;
			$this.addClass('ui-state-highlight');
		},
		mouseleave : function(){
			$(this).removeClass('ui-state-highlight');
		},
		click : function(event){
			event.stopPropagation();
			$userMenuTopBorder.toggle();
			$userMenu.toggle();
			$(this).removeClass('ui-state-highlight').addClass('ui-state-active');
			$userMenu.addClass('ui-state-active');
			$userMenuTopBorder.addClass('ui-state-active');
		}
	}, '.menu-container');
	
	$(document).on({
		mouseenter : function(){
			$(this).addClass('ui-state-highlight');
		},
		mouseleave : function(){
			$(this).removeClass('ui-state-highlight');
		},
		click : function(){
			window.location.href = $(this).find('a:only-child').attr('href');	
		}
	}, '.user-menu-item');
	
	$(document).on({
		mouseenter : function(){
			$(this).addClass('ui-state-highlight');
		},
		mouseleave : function(){
			$(this).removeClass('ui-state-highlight');
		},
		click: function(event){
			event.stopPropagation();
			$(this).addClass('ui-state-active').removeClass('ui-state-highlight');
			window.location.href = $(this).find('a:only-child').attr('href');
		}
	}, '.navigation')
	
	//hide popups
	$(document).click(function(){
		$btnShowUsermenu.removeClass('ui-state-active');
		
		$userMenuTopBorder.hide();
		$userMenu.hide();
		$autocompleteResults.hide();
	});
});