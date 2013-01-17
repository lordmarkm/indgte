<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" href="<spring:url value='/resources/css/yellowpages/yellowpages.css' />" />
<script src="<spring:url value='/resources/javascript/yellowpages/yellowpages.js' />"></script>

<title>Dumaguete Yellow Pages</title>

<ul class="hide">
<c:forEach items="${businesses }" var="entry">
	<li class="category ${fn:toLowerCase(fn:substring(entry.key, 0, 1))}" categoryName="${entry.key }" categoryId="${entry.value[0] }" businessCount="${entry.value[1] }">
		<span class="category-name">${entry.key } (${entry.value[1] })</span>
	</li>
</c:forEach>
</ul>

<div class="grid_12">

<div class="grid_6 maingrid">
	<ul class="alphabetized"></ul>
</div>

<div class="topbusinesses grid_5"></div>

</div>

<script>
window.urls = {
	getCategoryBusinesses: '<spring:url value="/s/businesses/" />',
	imgur: 'http://i.imgur.com/',
	businessProfile: '<spring:url value="/" />',
	categories : '<spring:url value="/s/categories/" />'
}
</script>