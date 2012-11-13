$(function(){
	
	var 
		$iptSearch = $('.ipt-toptens-search'),
		$btnSearch = $('.btn-toptens-search'),
		$linkMore = $('.toptens-search-more'),
		$resultsContainer = $('.toptens-search-results-container'),
		$results = $('.toptens-search-results');
	
	var howmany = 5;
	var start = 0, lastTerm = '';
	
	function search(term, initial) {
		$btnSearch.button('disable');
		$('<div class="overlay">').appendTo($resultsContainer);
		
		function fail(message) {
			$results.text(message);
			$resultsContainer.find('.overlay').fadeOut(200, function() { 
				$(this).remove();
				$btnSearch.button('enable');
			});
			return false;
		}
		
		if(!term || term.length < 2) {
			return fail('Please be more specific.');
		}
		
		$.get(urls.toptensearch + term + '/' + start + '/' + howmany + '.json', function(response) {
			switch(response.status) {
			case '200':
				if(initial) {
					$results.html('');
				}
				
				if(!response.lists || response.lists.length < 1) {
					if(initial) {
						return fail('No results for ' + term);
					} else {
						$linkMore.hide();
					}
				} else if(response.lists.length === howmany) {
					$linkMore.show();
				} else {
					$linkMore.hide();
				}
				
				for(var i = 0, len = response.lists.length; i < len; ++i) {
					addTopTenList($results, response.lists[i]);
				}
				
				break;
			default:
				debug('Error searching topten lists');
				debug(response);
			}
		}).complete(function(){
			$resultsContainer.find('.overlay').delay(800).fadeOut(200, function() { 
				$(this).remove();
				$btnSearch.button('enable');
			});
		});
	}
	
	$btnSearch.click(function(){
		var term = $iptSearch.val();
		start = 0;
		lastTerm = term;
		search(term, true);
	});
	
	$linkMore.click(function(){
		start += howmany;
		search(lastTerm, false);
	});
	
	$('#topten-search-form').submit(function(){
		return false;
	});
	
	/*
	 * $container : <ol>/<ul>
	 */
	function addTopTenList($container, topten) {
		var $topten = $('<li class="topten">').appendTo($container);
		var imgSrc = dgte.toptens.genericTen;
		try {
			imgSrc = topten.leader.attachmentSummary.thumbnailHash ? 
					dgte.urls.imgur + topten.leader.attachmentSummary.thumbnailHash + 's.jpg' :
						dgte.toptens.genericTen;
		} catch(e) {
			debug('Error setting leader imgur');
		}
		$('<img class="topten-list-img">').attr('src', imgSrc).appendTo($topten);
		$('<a>').attr('href', urls.topTensPage + topten.id).text(topten.title).appendTo($topten);
		$('<div class="subtitle">').text(topten.totalVotes + ' votes cast, created ' + moment(topten.time).fromNow()).appendTo($topten);
	}
});