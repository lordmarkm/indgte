<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="noimage" value="/resources/images/noimage.jpg" />
<spring:url var="spinner" value="/resources/images/spinner.gif" />
<spring:url var="urlProfileRoot" value="/p/" />

<title>${business.fullName }</title>

<section class="grid_8">
	<img class="profilepic" src="${noimage }" />
	<div class="ui-widget-header">${business.fullName }</div>
	<div class="biz-description">${business.description }</div>
	<div class="biz-url">www.indumaguete.com/p/${business.domain }</div>
	<div class="biz-owner"><a href="${urlUserProfile}${business.owner.username}">${business.owner.username }</a></div>
</section>

<style>
.profilepic {
	cursor: pointer;
}
</style>

<c:if test="${owner }">
<!-- Image upload -->
<div id="profilepic-image-upload" style="display: none;"title="<spring:message code='business.profile.profilepic.upload.title' /> ${business.fullName }">
	<div class="form-profilepic">
		<input class="profilepic-file" type="file" />
		<button class="btn-profilepic-upload"><spring:message code='business.profile.profilepic.upload.button' /></button>
		<button class="btn-profilepic-cancel"><spring:message code='business.profile.profilepic.upload.cancel' /></button>
	</div>
	<div class="loading-profilepic hide">
		<img src="${spinner }" />
	</div>
</div>

<style>
div[aria-labelledby="ui-dialog-title-profilepic-image-upload"] a.ui-dialog-titlebar-close {
	display:none;	
}
</style>

<script>
$(function(){
	//WARNING: owner-only script!
	
	var $upload = $('#profilepic-image-upload'),
		$uploadBtnUpload = $('.btn-profilepic-upload'),
		$uploadBtnCancel = $('.btn-profilepic-cancel'),
		$file = $('.profilepic-file');
	
	$('.profilepic').click(function(){
		$upload.dialog({
			width: 500, height: 150,
			modal: true,
			closeOnEscape: false,
			resizable: false
		});
	});
	
	$uploadBtnCancel.click(function(){
		$upload.dialog('close');
	});
	
	$uploadBtnUpload.click(function(){
		$upload.find('div').toggle();
		upload($file[0].files[0]);
	});
	
	//from http://paulrouget.com/miniuploader/
	function upload(file) {
	    if (!file || !file.type.match(/image.*/)) return;

	    var fd = new FormData();
	    fd.append("image", file);
	    fd.append("key", "${imgurKey}");
	    
	    var xhr = new XMLHttpRequest();
	    xhr.open("POST", "http://api.imgur.com/2/upload.json");
	    xhr.onload = function() {
	    	var response = JSON.parse(xhr.responseText);
	    	document.querySelector("#link").href = JSON.parse(xhr.responseText).upload.links.imgur_page;
	        document.body.className = "uploaded";
	        console.debug(response);
	    }

		xhr.send(fd);
	}
});
</script>
</c:if>