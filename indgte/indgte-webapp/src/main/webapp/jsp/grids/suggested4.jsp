<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:if test="${fn:length(suggestions) > 0 }">


<link rel="stylesheet" href="<c:url value='/resources/css/grids/similarbusinesses.css' />" />
<div class="grid_4 sidebar-section">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Other businesses in this group</div>
		
		<c:forEach items="${suggestions }" var="suggestion">
			<div class="suggestion">
				<div class="suggestion-img-container">
					<a class="fatlink dgte-previewlink" previewtype="business" href="${urlBusiness }${suggestion.domain}">
						<img class="suggestion-img" src="${suggestion.imgur == null ? noimage50 : suggestion.imgur.smallSquare }" />
					</a>
				</div>
				
				<div class="suggestion-info">
					<div><a class="fatlink dgte-previewlink" previewtype="business" href="${urlBusiness }${suggestion.domain}">${suggestion.fullName }</a></div>
					<div class="suggestion-description">${fn:substring(suggestion.description,0,140) }<c:if test="${fn:length(suggestion.description ) > 140}">...</c:if></div>
					<div class="rating">
						<span class="visual"></span>
						<span class="numerical hide">${suggestion.averageReviewScore }</span>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
</div>

<script src="<c:url value='/resources/javascript/grids/similarbusinesses.js' />" ></script>

</script>



</c:if>