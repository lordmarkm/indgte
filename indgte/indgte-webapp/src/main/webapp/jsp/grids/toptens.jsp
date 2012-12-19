<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="toptens-container grid_4 sidebar-section">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Top Tens</div>
		<div class="toptens">
			Popular:
			<ul class="popular"></ul>
			Recent:
			<ul class="recent"></ul>
			
			<a href="<spring:url value='/i/toptens/' />">View all...</a>
		</div>
	</div>
</div>
<link rel="stylesheet" href="<spring:url value='/resources/css/grids/toptens.css' />" />
<script>
window.urls.topTens = '<spring:url value="/i/toptens.json" />',
window.urls.topTenLeader = '<spring:url value="/i/toptens/leader/" />',
window.urls.topTensPage = '<spring:url value="/i/toptens/" />'
</script>
<script src="${jsTopTens }"></script>