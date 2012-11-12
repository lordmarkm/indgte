$(function(){
	$(document).on({
		dragover: function(e){
	    	e.preventDefault();
	    	var $this = $(this);
	    	if($this.hasClass('waiting')) return;
	    	$this.addClass('hungry');
		},
		dragleave: function(e) {
			e.preventDefault();
			var $this = $(this);
			if($this.hasClass('waiting')) return;
			$this.removeClass('hungry');
		},
		click: function() {
			var $this = $(this);
			if($this.hasClass('waiting')) return;
			$this.siblings('.image-upload-file').click();
		},
		drop: function() {
			var $this = $(this);
			if($this.hasClass('waiting')) return;
			$this.removeClass('hungry')
				.append($('<div class="overlay">'))
				.addClass('waiting');
		}
	}, '.image-upload-dropbox');
});