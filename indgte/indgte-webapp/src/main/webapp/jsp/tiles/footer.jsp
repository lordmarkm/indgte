<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<section class="footer grid_12">
	<sec:authorize access="hasRole('ROLE_USER')">
	<div class="footer-container subtitle">
		<div>Account settings (${user.username })</div>
		<label for="sel-theme">Theme</label>
		<select id="sel-theme">
			<c:forEach items="${themes }" var="theme">
				<option value="${theme }">${theme.name }</option>
			</c:forEach>
		</select>
		<label for="sel-bgs">Background</label>
		<select id="sel-bgs">
			<c:forEach items="${backgrounds }" var="bg">
				<option value="${bg.filename }">${bg.name }</option>
			</c:forEach>
		</select>
	</div>
	</sec:authorize>
	<div>Made by Mark</div>
</section>

<style>
.footer {
	min-height: 100px;
	text-align: center;
	padding-bottom: 20px;
}
.footer-container {
	margin: 140px 0 0 0;
	vertical-align: bottom;
	text-align: left;
}
.footer-container select {
	border: 1px solid black;
}
</style>

<script>
window.themes = {
	changeUrl : '<c:url value="/i/themechange/" />',
	current : '${user.theme}'
}
window.backgrounds = {
	changeUrl : '<c:url value="/i/bgchange/" />',
	current : '${user.background}'
}
$(function(){
	//theme change
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
	
	//bg change
	var $selBg = $('#sel-bgs');
	
	if(backgrounds.current) {
		$selBg.val(backgrounds.current);
	}
	$selBg.change(function(){
		$.post(backgrounds.changeUrl + $selBg.val() + '/json', function(response) {
			switch(response.status) {
			case '200':
				window.location.reload();
			default:
				debug('no reload on background change');
				debug(response);
			}
		});
	});
});
</script>