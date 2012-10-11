$(function(){
	$('a.loadhere').on('click', function(){
		$('#body').load($(this).attr('href') + '?loadhere=true');
		return false;
	});
	
	$('button').button();
	$('.button').button();
	
	//moment.js fromNow() on time elements
	$('.fromnow').html(moment(new Date(parseInt($(this).html()))).fromNow());
});