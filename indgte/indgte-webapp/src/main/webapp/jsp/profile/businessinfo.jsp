<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@include file="../tiles/links.jsp" %>

<spring:url var="urlSaveInfo" value="/b/editinfo/" />

<div class="ui-state-highlight">
<table class="tbl-business-info">
	<tr><td>Group</td>			 <td class="capitalize">${business.category.name }</td></tr>
	<tr><td>Description</td>     <td>${business.description }</td></tr>
	<tr><td>URL</td>		 	 <td><a class="fatlink" href="http://dgte.info/${business.domain }">http://dgte.info/${business.domain }</a></td>
	<tr><td>Owner</td>           <td><a class="dgte-previewlink fatlink" previewtype="user" href="${urlProfileRoot}user/${business.owner.username}">${business.owner.username }</a></td></tr>
</table>
</div>

<div class="business-info-container">
	${business.info }
	<c:if test="${empty business.info }">
		<spring:message code="business.profile.noinfo" />
	</c:if>
</div>

<c:if test="${owner }">
	<div class="relative form-editinfo-container">
		<form id="form-editinfo" class="hide" action="${urlSaveInfo }${business.domain }" method="post">
			<textarea name="info" id="txt-business-info" class="mceEditor"></textarea>
			<button>Update</button>
			<button class="btn-cancel">Cancel</button>
		</form>
		<button class="btn-edit">Edit</button>
	</div>
</c:if>

<style>
.tbl-business-info td {
	vertical-align: top;
}

.business-info-container {
	margin-top: 5px;
}

.form-editinfo-container {
	margin-top: 5px;
}

.btn-edit, .btn-cancel {
	margin-top: 5px;
}

textarea[name="info"] {
	width: 670px;
	height: 400px;
}

#business-info {
	padding: 1em !important;
}
</style>

<c:if test="${owner }">
	<spring:url var="urlTinyMce" value="/resources/tinymce/jscripts/tiny_mce/tiny_mce.js" />
	<script type="text/javascript" src="${urlTinyMce }"></script>
</c:if>

<script>
window.businessInfo = {
	owner : '${owner}' === 'true',
	domain : '${business.domain}',
	urlGetInfo : '<spring:url value="/b/getinfo/" />'
}
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
</script>