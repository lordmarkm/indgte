<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<link rel="stylesheet" href="<spring:url value='/resources/css/grids/sidebarpromos.css' />" />

<div class="sidebar-promos-container grid_4 sidebar-section">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Featured</div>
		<ul>
			<c:forEach items="${sidebarPromos}" var="promo">
				<li>
					<div class="sidebar-promo-image-container">
						<a href="${promo.summary.url }" class="dgte-previewlink" previewtype="${promo.summary.type}"><img src="${promo.summary.imgur == null ? noimage50 : promo.summary.imgur.smallSquare }" /></a>
					</div>
					<div class="sidebar-promo-info">
						<div class="sidebar-promo-title"><a href="${promo.summary.url }" class="fatlink dgte-previewlink" href="javascript:;" previewtype="${promo.summary.type}">${promo.summary.title }</a></div>
						<div class="sidebar-promo-description description">${fn:substring(promo.summary.description, 0, 80) }<c:if test="${fn:length(promo.summary.description) > 80 }">...</c:if></div>
					</div>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>

<div class="sidebar-new-container grid_4 sidebar-section">
	<div class="sidebar-container">
		<div class="sidebar-section-header">New</div>
		<ul>
			<c:forEach items="${newEntities}" var="summary">
				<li>
					<div class="sidebar-new-image-container">
						<a href="${summary.url }" class="dgte-previewlink" previewtype="${summary.type}"><img src="${summary.imgur == null ? noimage50 : summary.imgur.smallSquare }" /></a>
					</div>
					<div class="sidebar-new-info">
						<div class="sidebar-new-title"><a href="${summary.url }" class="fatlink dgte-previewlink" href="javascript:;" previewtype="${summary.type}">${summary.title }</a> (${summary.type })</div>
						<div class="sidebar-new-description description">${fn:substring(summary.description, 0, 80) }<c:if test="${fn:length(summary.description) > 80 }">...</c:if></div>
						<div class="newfromnow subtitle">${summary.time.time }</div>
					</div>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>

<script>
$(function(){
	$('.newfromnow').each(function(){
		var $this = $(this);
		var millis = $this.html();
		$this.html(moment(parseInt(millis)).fromNow());
	});
});
</script>