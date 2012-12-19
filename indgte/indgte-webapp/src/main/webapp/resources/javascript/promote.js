$(function(){
	var 
		$btnPromote = $('.btn-promote'),
		$dlgPromote = $('.dialog-promote'),
		$frmPromote = $('.form-promote'),
		$startPromote = $('#start-date'),
		$endPromote = $('#end-date'),
		$coconutCost = $('.coconut-cost');
	
	$frmPromote.validate({
		rules: {
			start: {
				required: true
			},
			end: {
				required: true
			}
		}
	});
	
	$startPromote.datepicker({
		minDate: '0'
	}).change(function(){
		var startDate = new Date($startPromote.val());
		var maxdays = user.coconuts - 1;
		var lastPossibleDate = new Date(startDate);
		debug('last possible date: ' + lastPossibleDate);
		lastPossibleDate.setTime(lastPossibleDate.getTime() + (maxdays * 24 * 60 * 60 * 1000));
		$endPromote.datepicker('option', 'minDate', startDate)
				.datepicker('option', 'maxDate', lastPossibleDate);
		computeCoconutCost();
	});
	
	$endPromote.datepicker({
		minDate: '0',
		maxDate: '+' + (user.coconuts - 1) + 'd'
	}).change(computeCoconutCost);
	
	function computeCoconutCost() {
		var startDate = new Date($startPromote.val());
		var endDate = new Date($endPromote.val());
		if(!isNaN(startDate.getTime()) && !isNaN(endDate.getTime())) {
			var coconutCost = ((endDate - startDate) / 1000 / 60 / 60 / 24) + 1;
			$coconutCost.text('This promotion would cost ' + coconutCost + ' coconuts.');
		}
	}
	
	$btnPromote.click(function(){
		$dlgPromote.dialog({
			buttons: {
				'Promote' : function(){
					if($frmPromote.valid()) {
						$frmPromote.submit();
					}
				},
				
				'Cancel' : function() {
					$(this).dialog('close');
				}
			}
		});
	});
});