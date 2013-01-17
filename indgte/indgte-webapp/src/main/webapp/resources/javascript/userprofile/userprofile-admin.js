$(function(){
	var 
		$btnGrant = $('.btn-grant-coconuts'),
		$dlgGrant = $('.dlg-grant-coconuts'),
		$frmGrant = $('#frm-grant-coconuts');
	
	$frmGrant.validate({
		rules : {
			howmany: {
				required: true,
				number: true,
				range: [1, 1000]
			}
		}
	});
	
	$btnGrant.click(function(){
		$dlgGrant
			.attr('title', 'Grant coconuts to ' + target.username)
			.dialog({
				buttons: {
					'Grant' : function(){
						if(!$frmGrant.valid()) {
							return false;
						}
						$frmGrant.submit();
					},
					'Cancel': function(){
						$(this).dialog('close');
					}
				}
			});
		
	});
});