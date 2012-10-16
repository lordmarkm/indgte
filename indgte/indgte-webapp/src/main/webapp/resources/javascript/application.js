//we give 0 ***** about corrupting the global namespace
window.debug = function(m) {
	console.debug(m);
}

window.unescapeBrs = function($target) {
	var oldtext = $target.val();
	var newtext = oldtext.replace(/\<br\s*\>/g, '\n');
	$target.val(newtext);
}

$.fn.extend({
	overlay : function(target, location) {
		var $target = target instanceof jQuery ? target : $(target);
		var targetOffset = $target.offset();
		
		var topOffset = ($target.height() - this.height()) * 0.2;
		var leftOffset;
		switch(location) {
		case 'left':
			leftOffset = targetOffset.left - this.width() - 20;
			break;
		case 'right':
			leftOffset = targetOffset.left + $target.width() + 10;
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