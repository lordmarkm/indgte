<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../tiles/links.jsp" %>

<title>${group.name } In Dumaguete</title>
<script type="text/javascript" src="${jsApplication }"></script>

<div class="yellowpages-content grid_9 ui-corner-all">
	<h1>${group.name }</h1>

	<ul class="businesses">
		<c:forEach items="${businesses }" var="business" varStatus="i">
		<li class="business ui-widget">
			<div class="name ui-widget-header" ><strong>
				<a href="${urlProfile }${business.domain}">${business.name }</a>
			</strong></div>
			
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
		</li>
		</c:forEach>
	</ul>

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
	padding: 2px;
	z-index: 1;
}
.business-details {
	font-size: 0.9em;
	position: relative;
}

ul.businesses {
	list-style-type: none;
	padding: 0;
}

li.business {
	float: left;
	width: 30%;
	min-height: 150px;
	max-height: 150px;
	margin: 0 5px 10px 5px;
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

});
</script>