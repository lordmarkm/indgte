$(function(){
	//check if we have somewhere to load the toptens
	var $toptens = $('.toptens');
	
	if(!valid()) return;
	
	function valid() {
		if($toptens.length === 0) {
			debug('Toptens container not available.');
			return false;
		}
		if(!urls || !urls.topTens) {
			debug('Toptens URL not available.');
			return false;
		}
		if(urls.topTenLeaders) {
			debug('Leaders URL not available.');
			return false;
		}
		return true;
	}
	
	//load them
	var $popular = $('.toptens ul.popular');
	var $recent = $('.toptens ul.recent');
	
	var listed = {};
	function loadTopTens() {
		$.get(urls.topTens, function(response) {
			switch(response.status) {
			case '200':
				for(var i = 0, len = response.popular.length; i < len; ++i) {
					if(listed[response.popular[i].title]) continue;
					listed[response.popular[i].title] = true;
					addTopTenList($popular, response.popular[i]);
				}
				for(var i = 0, len = response.recent.length; i < len; ++i) {
					if(listed[response.recent[i].title]) continue;
					listed[response.recent[i].title] = true;
					addTopTenList($recent, response.recent[i]);
				}
				break;
			default:
				debug('Error getting topten lists');
				debug(response);
			}
		});
	}
	
	function addTopTenList($container, topten) {
		var $topten = $('<li class="topten">').appendTo($container);
		var imgSrc = topten.leader && topten.leader.imgur ? topten.leader.imgur.smallSquare : dgte.toptens.genericTen;
		$('<img class="topten-list-img">').attr('src', imgSrc).appendTo($topten);
		$('<a>').attr('href', urls.topTensPage + topten.id).text(topten.title).appendTo($topten);
		$('<div class="subtitle">').text(topten.totalVotes + ' votes cast, created ' + moment(topten.time).fromNow()).appendTo($topten);
	}
	
	loadTopTens();
});