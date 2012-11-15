$(function(){
	var $watchedTagItems = $('.watched-tag-items');
	
	var desclength = 80;
	
	waiting = false;
	$(document).on({
		click: function(){
			if(waiting) return;
			waiting = true;
			var tag = $(this).attr('tag');
			getTags(tag);
		}
	}, 'a.watched-tag');
	
	function getTags(tag) {
		dgte.overlay($watchedTagItems);
		var url = tag ? urls.tag + '/' + tag + '/0/5.json' : urls.watchedtags + '.json';
		$.get(url, function(response) {
			switch(response.status) {
			case '200':
				for(var i = 0, len = response.items.length; i < len; ++i) {
					additem($watchedTagItems, response.items[i]);
				}
				break;
			default:
				debug('exception')
			}
		}).complete(function(){
			dgte.fadeOverlay($watchedTagItems, function(){
				waiting = false;
			});
		});
	}
	getTags();
	
	function addItem($container, item) {
		var $li = $('<li class="watched-tag-item">');
		
		$('<img class="watched-tag-img">').attr('src', item.imgur.smallSquare).appendTo($li);
		
		var $info = $('<div class="watched-tag-item-info">').appendTo($li);
		$('<div class="watched-tag-item-name">').text(item.name).appendTo($info);
		var description = item.description.length > desclength ? item.description.substring(0, desclength) + '...' : item.description;
		$('<div class="subtitle">').text(description).appendTo($info);
	}
});