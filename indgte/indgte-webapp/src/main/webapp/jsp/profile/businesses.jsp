<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="urlRegister" value="/r/" />
<spring:url var="urlBizProfile" value="/p/" />
<spring:url var="jsApplication" value="/resources/javascript/application.js" />

<section class="grid_12">
	<c:choose>
	<c:when test="${fn:length(businesses) gt 0 }">
	<c:forEach items="${businesses }" var="business">
	<div class="business">
		<div class="business-title"><a href="${urlBizProfile}${business.domain}">${business.fullName }</a></div>
		<div class="business-description">${business.description }</div>
	</div>
	</c:forEach>
	</c:when>
	<c:otherwise>
	<div class="grid_8">
		<spring:message code="businesses.nobusiness" />
	</div>
	</c:otherwise>
	</c:choose>
	
	<div class="grid_8">
		<a class="loadhere" href="${urlRegister }"><spring:message code="businesses.register" /></a>
	</div>
</section>

<script type="text/javascript" src="${jsApplication }"></script>