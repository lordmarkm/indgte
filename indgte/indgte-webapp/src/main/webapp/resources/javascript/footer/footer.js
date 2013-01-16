$(function(){
	//theme change
	var $selTheme = $('.sel-theme');
	
	if(themes.current) {
		$selTheme.val(themes.current);
	}
	$selTheme.change(function(){
		$.post(themes.changeUrl + $selTheme.val() + '.json', function(response) {
			switch(response.status) {
			case '200':
				window.location.reload();
			default:
				debug('no reload on theme change');
				debug(response);
			}
		});
	});
	
	//bg change
	var $selBg = $('.sel-bgs');
	
	if(backgrounds.current) {
		$selBg.val(backgrounds.current);
	}
	$selBg.change(function(){
		$.post(backgrounds.changeUrl + $selBg.val() + '/json', function(response) {
			switch(response.status) {
			case '200':
				window.location.reload();
			default:
				debug('no reload on background change');
				debug(response);
			}
		});
	});
});