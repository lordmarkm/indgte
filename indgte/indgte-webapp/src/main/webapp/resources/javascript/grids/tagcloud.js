$(function(){
	//build tag cloud
	var $tagcloud = $('.tagcloud');
	
    $.fn.tagcloud.defaults = {
    	size: {start: 8, end: 18, unit: 'pt'},
    	//color: {start: '#cde', end: '#f52'}
   		color: {start: '#FF9999', end: '#000099'}
    };
	
	$.get(urls.tagweights, function(response) {
		switch(response.status) {
		case '200':
			for(var i = 0, len = response.tags.length; i < len; ++i) {
				var tag = response.tags[i];
				var $div = $('<div class="taglink">').text(' ').appendTo($tagcloud);
				$('<a>').attr('rel', tag.items).attr('href', urls.tag + tag.tag).text(tag.tag).appendTo($div);
			}
			$tagcloud.find('a').tagcloud();
			break;
		default:
			debug('Error retrieving weighted tags');
			debug(response);
		}
	});
	
	$(document).on({
		mouseenter: function(){
			$(this).addClass('ui-state-highlight');
		},
		mouseleave: function(){
			$(this).removeClass('ui-state-highlight');
		}
	}, '.taglink');
});