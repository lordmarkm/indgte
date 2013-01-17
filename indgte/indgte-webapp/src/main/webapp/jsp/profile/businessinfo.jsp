<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@include file="../tiles/links.jsp" %>

<spring:url var="urlSaveInfo" value="/b/editinfo/" />

<link rel="stylesheet" href="<spring:url value='/resources/css/businessprofile/businessinfo.css' />" />
<script src="<spring:url value='/resources/javascript/businessprofile/businessinfo.js' />" ></script>

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
</script>