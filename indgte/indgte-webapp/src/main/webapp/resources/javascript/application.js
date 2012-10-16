//we give 0 ***** about corrupting the global namespace
window.debug = function(m) {
	console.debug(m);
}

window.unescapeBrs = function(target) {
	if(target instanceof jQuery) {
		var oldtext = target.val();
		var newtext = oldtext.replace(/\<br\s*\>/g, '\n');
		target.val(newtext);
	} else if(typeof target == 'string' || target instanceof String) {
		return target.replace(/\<br\s*\>/g, '\n');
	}
}

$.fn.extend({
	overlay : function(target, location, $triangle) {
		var $target = target instanceof jQuery ? target : $(target);
		var targetOffset = $target.offset();
		var topOffset = ($target.height() - this.height()) * 0.2;
		var leftOffset;
		
		if($triangle) {
			$triangle.show();
		}
		
		switch(location) {
		case 'left':
			leftOffset = targetOffset.left - this.width() - 22;
			
			if($triangle) {
				$triangle.css('border-left', '30px solid grey').css('border-right', '0');
				$triangle.offset({
					top: targetOffset.top,
					left: targetOffset.left - 31
				});
			}
			break;
		case 'right':
			leftOffset = targetOffset.left + $target.width() + 11;
			
			if($triangle) {
				$triangle.css('border-right', '30px solid grey').css('border-left', '0');
				$triangle.offset({
					top: targetOffset.top,
					left: leftOffset - 10
				});
			}
			break;
		}

		var newoffset = {
			top: topOffset + targetOffset.top,
			left: leftOffset
		}
		this.offset(newoffset);
		return this;
	}
});

$(function(){
	$('a.loadhere').on('click', function(){
		$('#body').load($(this).attr('href') + '?loadhere=true');
		return false;
	});
	
	//make buttons (real and fake) conform to our ui theme
	$('button').button();
	$('.button').button();
	
	//moment.js fromNow() on time elements
	$('.fromnow').html(moment(new Date(parseInt($(this).html()))).fromNow());
});