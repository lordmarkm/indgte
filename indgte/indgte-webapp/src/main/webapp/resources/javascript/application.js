//we give 0 ***** about corrupting the global namespace
window.dgte = {
	domain: 'http://testfb.com:8080',
	search: {
		autocompleteMinlength: 4,
		letters: letters = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z']
	},
	review : {
		max: 5,
		previewChars: 140
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
		postsPerPage : 10,
		imgurKey: '1fbac05d6fce25d6a06a7a715b1cb2d8'
	},
	toptens: {
		genericTen : 'http://i.imgur.com/bJ2hx.png'
	},
	urls : {
		blackSquareSmall : 'http://i.imgur.com/Y0NTes.jpg',
		imgurUpload : 'http://api.imgur.com/2/upload.json',
		imgur : 'http://i.imgur.com/',
		preview : '/live/preview/'
	},
	
	upload: function(file, onComplete, title, caption) {
	    if (!file || !file.type.match(/image.*/)) return;
	    
	    debug('file: ' + file);
	    debug('callback: ' + onComplete);
	    debug('title: ' + title);
	    debug('caption: ' + caption);
	    
	    var fd = new FormData();
	    fd.append("image", file);
	    fd.append("key", dgte.constants.imgurKey);
	    if(title) fd.append("title", title);
	    if(caption) fd.append("caption", caption);
	    
	    var xhr = new XMLHttpRequest();
	    xhr.open("POST", dgte.urls.imgurUpload);
	    xhr.onload = function() {
	    	if(xhr.status != 200) {
	    		return xhr.onerror();
	    	}
	    	var response = JSON.parse(xhr.responseText);
	    	onComplete(response);
	    }
	    xhr.onerror = function(e) {
	    	debug('lol upload error');
	    	debug(e);
	    }
		xhr.send(fd);		
	},
	
	overlay : function(element, adjustheight){
		if(adjustheight) {
			if(element.height() < 48) {
				debug('adjusting ' + element.height() + ' to 48');
				element.css('min-height', '48px'); //set to height of spinner.gif
			}
		}
		$('<div class="overlay">').appendTo(element);
	},
	
	fadeOverlay : function(element, callback, options) {
		var delay = options && options.delay ? options.delay : 800;
		var fade = options && options.fade ? optiond.fade : 200;
		element.find('.overlay').delay(delay).fadeOut(fade, function(){
			$(this).remove();
			if(typeof callback == 'function') callback();
		});
	},
	
	desclength : 80,

	addBuySellItem : function($container, item, url) {
		var $li = $('<li class="buysell-item">').appendTo($container);
		
		var $img = $('<img class="buysell-img">').attr('src', item.imgur.smallSquare);
		if(url) {
			$('<a>').attr('href', url + item.id).append($img).appendTo($li)
		} else {
			$img.appendTo($li);
		}
		
		var $info = $('<div class="buysell-item-info">').appendTo($li);
		
		var $name = $('<div class="buysell-item-name">').text(item.id + ' - ' + item.name);
		if(url) {
			$('<a>').attr('href', url + item.id).append($name).appendTo($info);
		} else {
			$name.appendTo($info);
		}
		
		var description = item.description.length > dgte.desclength ? item.description.substring(0, dgte.desclength) + '...' : item.description;
		$('<div class="subtitle">').text(description).appendTo($info);
		$('<div class="subtitle">').text(moment(item.time).fromNow()).appendTo($info);
	}
}

window.debug = function(m) {
	console.debug(m);
}

window.unescapeBrs2 = function(string) {
	return string.replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/\<br\s*\>/g, '').replace(/\<br\s*\/\>/g, '');
}

window.unescapeBrs = function(target) {
	if(target instanceof jQuery) {
		var oldtext = target.val();
		var newtext = oldtext.replace(/\<br\s*\>/g, '\n');
		target.val(newtext);
	} else if(typeof target == 'string' || target instanceof String) {
		return target.replace(/\<br\s*\>/g, '\n').replace(/\<br\s*\/\>/g, '\n');
	}
}

window.removeBrs = function(target) {
	if(target instanceof jQuery) {
		var oldtext = target.val();
		var newtext = oldtext.replace(/\<br\s*\>/g, '').replace(/\<br\s*\/\>/g, '');
		target.val(newtext);
	} else if(typeof target == 'string' || target instanceof String) {
		return target.replace(/\<br\s*\>/g, '').replace(/\<br\s*\/\>/g, '');
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
	},
	
	dgteFadeIn : function() {
		this.hide().fadeIn(1000);
		return this;
	},
	
    insertAtCaret: function(myValue) {
    	debug('inserting ' + myValue);
        if (document.selection) {
                this.focus();
                sel = document.selection.createRange();
                sel.text = myValue;
                this.focus();
        }
        else if (this.selectionStart || this.selectionStart == '0') {
            var startPos = this.selectionStart;
            var endPos = this.selectionEnd;
            var scrollTop = this.scrollTop;
            this.value = this.value.substring(0, startPos)+myValue+this.value.substring(endPos,this.value.length);
            this.focus();
            this.selectionStart = startPos + myValue.length;
            this.selectionEnd = startPos + myValue.length;
            this.scrollTop = scrollTop;
        } else {
            this.value += myValue;
            this.focus();
        }
    },
    
    spinner: function(adjustheight){
		if(adjustheight) {
			if(this.height() < 48) {
				this.css('min-height', '48px'); //set to height of spinner.gif
			}
		}
		this.append($('<div class="overlay">'));
		return this;
    },
    
    fadeSpinner: function(callback, options) {
		var delay = options && options.delay ? options.delay : 800;
		var fade = options && options.fade ? optiond.fade : 200;
		this.find('.overlay').delay(delay).fadeOut(fade, function(){
			$(this).remove();
			if(typeof callback == 'function') callback();
		});
		return this;
    },
    
    preview: function() {
    	var href = this.attr('href');
    	var type = this.attr('previewtype');

    	var $preview = $('.dgte-preview');
    	$preview.addClass('dgte-preview-visible');
    	
    	var offset = this.offset();
    	offset.top = offset.top + this.height();
    	$preview.show().offset(offset);
    	
    	var oldhref = $preview.attr('href');
    	if(href === oldhref) {
    		return;
    	}
    	$preview.attr('href', href);
    	
    	function constructPreview() {
    		$preview.attr('constructed', 'true');
    		
    		$('<div class="white-arrow-up arrow">').appendTo($preview);
    		
    		var $whiteContainer = $('<div class="preview-white-container">').appendTo($preview);
    		
    		var $coverContainer = $('<div class="preview-cover-image-container relative">').appendTo($whiteContainer);
    		$('<img class="preview-cover-image">').appendTo($coverContainer);
    		
    		var $imgContainer = $('<div class="preview-image-container">').appendTo($whiteContainer);
    		$('<img class="preview-image">').appendTo($imgContainer);
    		
    		var $previewInfo = $('<div class="preview-info-container">').appendTo($whiteContainer);
    		$('<div class="preview-title">').appendTo($previewInfo);
    		$('<div class="preview-description subtitle">').appendTo($previewInfo);
    	}
    	
    	function fillPreview(preview) {
    		if($preview.attr('constructed') != 'true') {
    			constructPreview();
    		}

    		$preview
    			.find('.preview-cover-image').attr('src', preview.cover.source).end()
    			.find('.preview-image').attr('src', preview.image).load(function(){$(this).show()}).end()
    			.find('.preview-title').text(preview.title).end()
    			.find('.preview-description').text(preview.description.length > 80 ? preview.description.substring(0, 80) + '...' : preview.description);
    		
    		//set cover offset (facebook only)
    		if(preview.type == 'user' && preview.cover.offsetY != 0) {
    			var $cover = $preview.find('.preview-cover-image');
    			$cover.load(function(){
    				var height = $cover.height();
    				var offsetY = preview.cover.offsetY;
    				var offset = parseInt((preview.cover.offsetY/100)*height);
    				$cover.css('position', 'absolute').css('top', -(offset));
    				$cover.show();
    			})
    		}
    	}
    	
    	$preview.find('.preview-cover-image').hide().end()
    		.find('.preview-image').hide().end()
    		.find('.preview-info-container div').text(' ');
    	
    	$.get(dgte.domain + dgte.urls.preview + 'json',
    		{
    			href: href,
    			type: type
    		},
    		function(response) {
		    	switch(response.status) {
		    	case '200':
		    		fillPreview(response.preview);
		    		break;
		    	default:
		    		debug(response);
		    	}
    		}
    	)
    	
    	return this;
    }
});

$(function(){
	//preview links
	var $preview = $('.dgte-preview'),
		closetimeout;
	
	$(document).on({
		mouseenter: function() {
			setOpenTimeout(this);
			$(this).preview();
		},
		mouseleave: setCloseTimeout
	}, '.dgte-previewlink');
	
	$(document).on({
		mouseenter: function() {
			setOpenTimeout(this);
		},
		mouseleave: setCloseTimeout
	}, '.dgte-preview');
	
	function setOpenTimeout(a) {
		var newhref = $(a).attr('href');
		debug('setting href: ' + newhref);
		$preview.attr('newhref', newhref);
	}
	
	function setCloseTimeout() {
		$preview.attr('newhref', '');

		if(closetimeout) {
			clearTimeout(closetimeout);
		}
		closetimeout = setTimeout(function(){
			var newhref = $preview.attr('newhref');
			var oldhref = $preview.attr('href');
			
			debug('comparing hrefs: ' + newhref + ', ' + oldhref);
			if(newhref != oldhref) {
				$preview.hide();
			}
		}, 400);
	}
	
	//make buttons (real and fake) conform to our ui theme
	$('button').button();
	$('.button').button();
	
	//moment.js fromNow() on time elements
	$('.fromnow').html(moment(new Date(parseInt($(this).html()))).fromNow());
});