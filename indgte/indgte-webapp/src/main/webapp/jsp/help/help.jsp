<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@include file="../tiles/links.jsp" %>

<title>Dumaguete</title>

<section class="grid_8 maingrid">
	<iframe 
		src="https://docs.google.com/document/d/13zZn7xXo9Q3tayLMV_1p4BuRyVaa9Mx2YP_wIDk0p80/pub?embedded=true"
		width="100%"
		height="2000px"
		frameborder="0">
	</iframe>
</section>

<script>
window.urls = {};
</script>

<!-- Notifications -->
<%@include file="../grids/notifications4.jsp"  %>

<!-- Sidebar Featured promos -->
<%@include file="../grids/sidebarpromos.jsp" %>

<!-- Top Tens -->
<%@include file="../grids/toptens.jsp" %>