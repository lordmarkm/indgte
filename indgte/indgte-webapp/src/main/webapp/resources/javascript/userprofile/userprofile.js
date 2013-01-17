$(function(){
	//subscribe
	var subscribed = poster.subscribed;
	var $btnSubscribe = $('.btn-subscribe-toggle');
	
	function refreshSubsButton() {
		if(subscribed) {
			$btnSubscribe
				.button({label: 'Subscribed', icons: {secondary: 'ui-icon-circle-check'}})
				.unbind('hover')
				.hover(function(){
					$btnSubscribe.button({label: 'Unsubscribe', icons: {}});
				}, function(){
					$btnSubscribe.button({label: 'Subscribed', icons:{secondary: 'ui-icon-circle-check'}});
				});
		} else {
			$btnSubscribe
				.button({label: 'Not subscribed', icons: {secondary: 'ui-icon-circle-close'}})				
				.unbind('hover')
				.hover(function(){
					$btnSubscribe.button({label: 'Subscribe', icons: {}});
				}, function(){
					$btnSubscribe.button({label: 'Not subscribed', icons:{secondary: 'ui-icon-circle-close'}});
				});
		}
	}
	refreshSubsButton();

	$btnSubscribe.click(function(){
		var url = subscribed ? urls.unsubscribe : urls.subscribe;
		$.post(url, function(response) {
			switch(response.status) {
			case '200':
				subscribed = !subscribed;
				refreshSubsButton();
				break;
			default:
				debug(response);
			}
		});
	});
	
	//edit self-description
	var 
		$btnEdit = $('.btn-edit-description')
		$btnCancel = $('.btn-cancel-edit-description'),
		$formContainer = $('.form-edit-container'),
		$frmEdit = $('#form-edit-description'),
		$description = $('.userinfo-description');
	
	$btnEdit.click(function(){
		$btnEdit.hide();
		$formContainer.show();
		initTiny();
	});
		
	$btnCancel.click(function(){
		$formContainer.hide();
		$btnEdit.show();
		return false;
	});
		
	function initTiny() {
		$formContainer.spinner();
		
		tinyMCE.init({
		    // General options
		    mode : "specific_textareas",
		    editor_selector : "mceEditor",
		    theme : "advanced",
		    plugins : "pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras",

		    // Theme options
		    theme_advanced_buttons1 : "save,newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,formatselect,fontselect,fontsizeselect",
		    theme_advanced_buttons2 : "cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code,|,preview,|,forecolor,backcolor",
		    theme_advanced_buttons3 : "tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,media,advhr,|,print,|,ltr,rtl,|,fullscreen",
		    theme_advanced_buttons4 : "insertlayer,moveforward,movebackward,absolute,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,|,visualchars,nonbreaking,pagebreak",
		    theme_advanced_toolbar_location : "top",
		    theme_advanced_toolbar_align : "left",
		    theme_advanced_statusbar_location : "bottom",
		    theme_advanced_resizing : true,
		    oninit : setUserDescriptionContent
		});
		
		function setUserDescriptionContent() {
			$.get(urls.getDescription + target.username, function(response) {
				switch(response.status) {
				case '200':
					if(response.description) {
						tinyMCE.activeEditor.setContent(response.description)
					} else {
						error('Error getting user description');
						error(response);
					}
					break;
				default:
					error('Error getting info');
					error(response);
				}
				$formContainer.fadeSpinner();
			});
		}
	}	
});