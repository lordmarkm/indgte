$(function(){
	$('a.loadhere').on('click', function(){
		$('#body').load($(this).attr('href') + '?loadhere=true');
		return false;
	});
	
	$('.button').button();
});