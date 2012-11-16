$(function(){
	var $watchedTagItems = $('.watched-tag-items');
	
	waiting = false;
	$(document).on({
		click: function(){
			var $this = $(this);
			
			if(waiting || $this.hasClass('selected')) return false;
			waiting = true;
			var tag = $this.attr('tag');
			
			$('a.watched-tag').removeClass('selected');
			$this.addClass('selected');
			getTags(tag);
			
			return false;
		}
	}, 'a.watched-tag');
	
	function getTags(tag) {
		dgte.overlay($watchedTagItems.parent());
		var url = (tag && tag != 'all') ? urls.watchedtags + '/' + tag + '.json' : urls.watchedtags + '.json';
		$.get(url, function(response) {
			switch(response.status) {
			case '200':
				$watchedTagItems.html('');
				for(var i = 0, len = response.items.length; i < len; ++i) {
					dgte.addBuySellItem($watchedTagItems, response.items[i], urls.buysellitem);
				}
				break;
			default:
				debug('exception')
			}
		}).complete(function(){
			dgte.fadeOverlay($watchedTagItems.parent(), function(){
				waiting = false;
			});
		});
	}
	getTags();
});