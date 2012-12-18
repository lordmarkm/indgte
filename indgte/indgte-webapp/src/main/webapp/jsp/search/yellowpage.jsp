<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../tiles/links.jsp" %>

<title>${group.name } In Dumaguete</title>
<script type="text/javascript" src="${jsColumnize }"></script>

<div class="yellowpages-content grid_9 ui-corner-all">
	<div class="page-header capitalize">${group.name }</div>
	<a href="<spring:url value='/i/toptens/businessgroup/${group.id }' />">View Ranking</a>
	
	<div class="businesses">
		<c:forEach items="${businesses }" var="business" varStatus="i">
		<div class="business">
			<div class="name" >
				<strong>
					<a href="${urlProfile }${business.domain}">${business.name }</a>
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

<style>
.yellowpages-content {
	background-color: #ffffb2;
	padding: 5px 5px 20px 5px;
}

h1 {
	text-transform: capitalize;
}

.ui-widget {
	font-size: 1em;
}
.name {
}
.name a {
	text-decoration: none;
}
.business-details {
	font-size: 0.9em;
	position: relative;
}

.businesses-column {
	width: 30%;
	padding: 1.5%;
	height: 100%;
	display: inline-block;
	vertical-align: text-top;
}
.businesses-column:not(:first-child) {
	border-left: 1px solid black;
}

.businesses {
	margin-top: 10px;
}

.business {
	margin: 0 0 10px 0;
	padding: 2px;
}
.business div {
	white-space: normal;
	word-wrap: break-word;
	word-break: normal|break-all|hyphenate;
}
</style>

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