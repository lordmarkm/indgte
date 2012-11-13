window.useful = {
	/* options {			: all required
	 * 		parent				: $(<input type="text">),
	 *  	minlength			: minimum len to search
	 *  	url					: search url
	 *  	resultsContainer	: results container
	 *  	descLength			: description maxlength
	 * }
	 */
	autocomplete : function(options) {
		var term = options.parent.val();
		if(term.length < options.minlength) return;
		$.get(options.url + term + '.json', function(response){
			switch(response.status) {
			case '200':
				//handle layout first
				var resultsContainer = options.resultsContainer;
				resultsContainer.html('')
				var parent = options.parent;

				//do other stuff
				var visibleResults = 0;
				function makeAutocompleteResult(result) {
					var $auto = $('<div>').appendTo(resultsContainer).click(options.onClick);
					useful.makeSummaryContainer($auto, result, options.descLength);
					++visibleResults;
				}
				
				if(response.business && response.business.length > 0) {
					$('<div class="ui-widget-header">').text('Businesses').appendTo($autocompleteResults);
					for(var i = 0, length = response.business.length; i < length; i++) {
						makeAutocompleteResult(response.business[i]);
					}
				}
				
				if(response.category && response.category.length > 0) {
					$('<div class="ui-widget-header">').text('Categories').appendTo(resultsContainer);
					for(var i = 0, length = response.category.length; i < length; i++) {
						makeAutocompleteResult(response.category[i]);
					}
				}
				
				if(response.product && response.product.length > 0) {
					$('<div class="ui-widget-header">').text('Products').appendTo(resultsContainer);
					for(var i = 0, length = response.product.length; i < length; i++) {
						makeAutocompleteResult(response.product[i]);
					}
				}
				
				if(visibleResults) {
					resultsContainer.show();
					var offset = parent.offset();
					offset.top += parent.height() + 10;
					resultsContainer.css('z-index', '9001').offset(offset);
				} else {
					resultsContainer.hide();
				}
				break;
			default:
				debug('Error' + JSON.stringify(response));
			}
		});
	},

	/* $container should be $('<div>') or $('<li>') */
	makeSummaryContainer : function($container, summary, descLength) {
		debug('desclength: ' + descLength);
		$container.addClass('summary-container')
			.attr('attachmentId', summary.id)
			.attr('attachmentTitle', summary.title)
			.attr('attachmentType', summary.type);
		
		//img
		$('<img class="summary-img">').attr('src', summary.thumbnailHash ? dgte.urls.imgur + summary.thumbnailHash + 's.jpg' : dgte.urls.blackSquareSmall)
			.appendTo($container);
		
		//title
		$('<div class="summary-title">').text(summary.title).appendTo($container);
		
		//description
		$('<div class="summary-description subtitle">')
			.html(summary.description.length > descLength ? summary.description.substring(0, descLength) + '...' : summary.description)
			.appendTo($container);
	}
};