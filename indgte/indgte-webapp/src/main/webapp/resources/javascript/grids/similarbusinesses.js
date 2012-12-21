$(function(){
	var $rating = $('.rating');
	
	$rating.each(function(i, rating) {
		var $this = $(this);
		var $stars = $this.find('.visual');
		
		var rating = parseFloat($this.find('.numerical').text());
		rating = Math.round(rating*2)/2;
		
		var whole = Math.floor(rating);
		var half = Math.ceil(rating - whole);
		var none = 5 - (whole + half);
		
		for(var i = 0; i < whole; ++i) {
			$('<div class="star">').appendTo($stars);
		}
		if(half) {
			$('<div class="star halfstar">').appendTo($stars);
		}
		for(var i = 0; i < none; ++i) {
			$('<div class="star nullstar">').appendTo($stars);
		}
	});
});