<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
.footer {
	min-height: 200px;
	text-align: center;
}
.footer-container {
	margin: 140px 0 0 0;
	vertical-align: bottom;
}
</style>

<section class="footer grid_12">
	<div class="footer-container">
		<div>
			<select id="sel-theme">
				<c:forEach items="${themes }" var="theme">
					<option value="${theme }">${theme.name }</option>
				</c:forEach>
			</select>
		</div>
		<div>Made by Mark</div>
	</div>
</section>

<script>
window.themes = {
	changeUrl : '<c:url value="/i/themechange/" />',
	current : '${user.theme}'
}
$(function(){
	var $selTheme = $('#sel-theme');
	
	if(themes.current) {
		$selTheme.val(themes.current);
	}
	$selTheme.change(function(){
		$.post(themes.changeUrl + $selTheme.val() + '.json', function(response) {
			switch(response.status) {
			case '200':
				window.location.reload();
			default:
				debug('no reload on theme change');
				debug(response);
			}
		});
	});
});
</script>