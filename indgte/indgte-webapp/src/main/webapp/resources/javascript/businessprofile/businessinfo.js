$(function(){
	var textBusinessInfo = 'txt-business-info';
	
	var 
		$infoContainer = $('.business-info-container'),
		$formContainer = $('.form-editinfo-container'),
		$formEdit = $('#form-editinfo'),
		$btnEdit = $('.btn-edit'),
		$btnCancel = $('.btn-cancel');
	
	if(businessInfo.owner) {
		$btnEdit.click(function(){
			editMode();
			return false;
		});
		
		$btnCancel.click(function(){
			viewMode();
			return false;
		});
	}
	
	function editMode() {
		$btnEdit.hide();
		$infoContainer.hide();
		$formEdit.show();
		initTiny();
	}
	
	function viewMode() {
		$infoContainer.show();
		hideTiny();
		$btnEdit.show();
	}
	
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
		    oninit : setBusinessInfoContent
		});
		
		function setBusinessInfoContent() {
			$.get(businessInfo.urlGetInfo + businessInfo.domain, function(response) {
				switch(response.status) {
				case '200':
					if(response.info) tinyMCE.activeEditor.setContent(response.info);
					break;
				default:
					debug('Error getting info');
					debug(response);
				}
				$formContainer.fadeSpinner();
			});
		}
	}
	
	function hideTiny() {
		tinyMCE.get(textBusinessInfo).hide();
		$formEdit.hide();
	}
	
	function showTiny() {
		tinymce.get(textBusinessInfo).show();
	}
});