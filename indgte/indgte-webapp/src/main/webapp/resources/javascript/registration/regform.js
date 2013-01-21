window.urls = {
	uniqueDomain : '<spring:url value="/r/uniquedomain/" />'
}

$(function(){
	var $form = $('#regform');
	
	function matchRegexp(value, element, regexp) {
		return value.match(new RegExp(regexp));
	}
	$.validator.addMethod("domain", matchRegexp, 'Domain requires alphanumeric characters only (lowercase, no spaces)');
	$.validator.addMethod("fullName", matchRegexp, 'Business Name accepts alphanumeric characters only (upper or lower case, spaces allowed)');
	
	$form.validate({
		rules: {
			domain: {
				required: true,
				rangelength: [4, 20],
				domain: '^[a-z0-9]+$',
				remote: {
					url: urls.uniqueDomain,
					type: 'post',
					data: {domain: function(){return $('#domain').val()}, editDomain: '${editdomain}'}
				} 
			},
			fullName: {
				required: true,
				rangelength: [4, 45],
				fullName: '^[a-zA-Z0-9 ]+$'
			},
			address: {
				maxlength: 140
			},
			email: {
				email: true
			}
		},
		messages: {
			domain : {
				remote: function(){
					return 'The domain \'' + $('#domain').val() + '\' is already taken. Sorry.'
				}
			}
		},
		errorPlacement: function(error, element) {
			element.closest('tr').next().find('.error').html('').append(error);
		},
		errorClass: 'ui-state-error'
	});
	
	$('button.submit').click(function(){
		$form.submit();
	});
	
	$form.submit(function(){
		if(!$form.valid()) {
			return false;
		}
	});
	
	//unescape brs for edit
	unescapeBrs($('#address'));
	unescapeBrs($('#description'));
});