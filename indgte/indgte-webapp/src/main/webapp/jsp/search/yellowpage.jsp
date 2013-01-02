<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../tiles/links.jsp" %>

<title>${group.name } In Dumaguete</title>
<script type="text/javascript" src="${jsColumnize }"></script>
<link rel="stylesheet" href="<c:url value='/resources/css/yellowpages/yellowpage.css' />" />

<div class="grid_8 ui-corner-all maingrid">
	<div class="yellowpages-content">
		<div class="page-header capitalize">${group.name }</div>
		<a href="<spring:url value='/i/toptens/businessgroup/${group.id }' />">View Ranking</a>
		
		<div class="businesses">
			<c:forEach items="${businesses }" var="business" varStatus="i">
			<div class="business">
				<div class="name" >
					<strong>
						<a class="dgte-previewlink fatlink" previewtype="business" href="${urlProfile }${business.domain}">${business.name }</a>
					</strong>
				</div>
				
				<c:if test="${not empty business.address }">
				<div class="business-details"><strong>Address:</strong> ${business.address }</div>
				</c:if>
				
				<c:if test="${not empty business.landline }">
				<div class="business-details"><strong>Landline:</strong> ${business.landline }</div>
				</c:if>
				
				<c:if test="${not empty business.cellphone }">
				<div class="business-details"><strong>Cellphone:</strong> ${business.cellphone }</div>
				</c:if>
				
				<c:if test="${not empty business.email }">
				<div class="business-details"><strong>Email:</strong> ${business.email }</div>
				</c:if>
			</div>
			<c:if test="${(i.index+1)%3 == 0 && i.index != 0 }">
				<div class="clear"></div>
			</c:if>
			</c:forEach>
	</div>
	</div>
</div>

<!-- Notifications -->
<%@include file="../grids/notifications4.jsp"  %>

<script>
$(function(){
	$('.businesses').columnize({
		columns: 3,
		dontsplit: 'div',
		columnClass: 'businesses-column'
	});
	
	$('.businesses-column').each(function(i, column) {
		var $column = $(column);
		if($column.find('.business').length == 0) {
			$column.hide();
		}
	});
});
</script>