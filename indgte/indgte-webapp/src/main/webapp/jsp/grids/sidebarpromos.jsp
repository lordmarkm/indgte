<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

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
						<div class="sidebar-promo-title"><a href="${promo.summary.url }" class="fatlink dgte-previewlink" href="javascript:;" previewtype="${promo.summary.type}">${promo.summary.title }</a> (${promo.type })</div>
						<div class="sidebar-promo-description description">${fn:substring(promo.summary.description, 0, 80) }<c:if test="${fn:length(promo.summary.description) > 80 }">...</c:if></div>
					</div>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>

<style>
.sidebar-promos-container ul {
	list-style-type: none;
	padding: 0;
	margin: 0 0 0 5px;
}

.sidebar-promo-image-container {
	display: inline-block;
	vertical-align: top;
}

.sidebar-promo-info {
	width: 200px;
	display: inline-block;
	max-height: 77px;
	overflow-y: hidden;
}

.sidebar-promos-container img {
	width: 80px;
	height: 80px;
	vertical-align: bottom;
}

.sidebar-promos-container li:not(:first-child) {
	margin-top: 4px;
}
</style>