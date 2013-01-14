$(function(){
	$('#sel-lang')
		.val(manage.locale)
		.change(function(){
			window.location.href = manage.urlChangeLocale + $(this).val();
		});

	$('.btn-unsubscribe').click(function(){
		var $btn = $(this);
		var id = $btn.attr('subsId');
		var type = $btn.attr('subsType');
		$.post(dgte.urls.unsubscribe + '/' + type + '/' + id + '.json', function(response) {
			switch(response.status) {
			case '200':
				$btn.closest('tr').fadeOut();
				break;
			default:
				dgte.operationFailed();
			}
		});
	});
});