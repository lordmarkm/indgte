<div class="reviewqueue grid_4 sidebar-section">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Recently Viewed Business for Review</div>
		<div class="review-container">
			<div>Please take some time to review the businesses below if you have completed any transactions with them or have knowledge of their operations.</div>
			<ul class="reviewlist"></ul>
		</div>
	</div>
</div>
<script>
window.urls.reviewqueue = '<spring:url value="/i/reviewqueue.json" />';
window.urls.noreview = '<spring:url value="/i/noreview/" />';
window.urls.neverreview = '<spring:url value="/i/neverreview/" />';
window.urls.business = '${urlProfile}';
</script>
<script src="${jsReviewQueue }"></script>
<link rel="stylesheet" href="<spring:url value='/resources/css/grids/reviewqueue.css' />" />