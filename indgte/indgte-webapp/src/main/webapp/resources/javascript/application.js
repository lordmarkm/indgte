//we give 0 ***** about corrupting the global namespace
window.dgte = {
	search: {
		autocompleteMinlength: 4,
		letters: letters = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z']
	},
	home: {
		postMaxlength: 140,
		statusColsCollapsed: 30,
		statusColsExpanded: 60,
		productPreviews: 5,
		attchDescLength: 240 //max length of category/product description
	},
	yellowpages: {
		preview: 4
	},
	constants : {
		postsPerPage : 10
	},
	urls : {
		blackSquareSmall : 'http://i.imgur.com/Y0NTes.jpg'
	}
}

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

//override jquery ui dialog defaults
$.extend($.ui.dialog.prototype.options, {
    modal: true,
    resizable: false,
    maxHeight: 250,
    width:500,
	closeOnEscape: false,
	hide: {effect: "fade", duration: 200}
});

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
	//make buttons (real and fake) conform to our ui theme
	$('button').button();
	$('.button').button();
	
	//moment.js fromNow() on time elements
	$('.fromnow').html(moment(new Date(parseInt($(this).html()))).fromNow());
});