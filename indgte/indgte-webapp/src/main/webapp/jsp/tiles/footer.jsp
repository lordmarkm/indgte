<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<section class="footer grid_12">
	<sec:authorize access="hasRole('ROLE_USER')">
	<div class="footer-container subtitle">
		<div>Account settings (${user.username })</div>
		<label for="sel-theme-footer">Theme</label>
		<select class="sel-theme" id="sel-theme-footer">
			<c:forEach items="${themes }" var="theme">
				<option value="${theme }">${theme.name }</option>
			</c:forEach>
		</select>
		<label for="sel-bgs-footer">Background</label>
		<select class="sel-bgs" id="sel-bgs-footer">
			<c:forEach items="${backgrounds }" var="bg">
				<option value="${bg.filename }">${bg.name }</option>
			</c:forEach>
		</select>
	</div>
	</sec:authorize>
	<div>Made by <a class="dgte-previewlink fatlink" previewtype="user" href="<c:url value='/p/user/mark.martinez.986' />">Mark</a></div>
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
	current : '${user.appearanceSettings.theme}'
}
window.backgrounds = {
	changeUrl : '<c:url value="/i/bgchange/" />',
	current : '${user.appearanceSettings.background}'
}
$(function(){
	//theme change
	var $selTheme = $('.sel-theme');
	
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
	var $selBg = $('.sel-bgs');
	
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

<div id="fb-root"></div>
<script>(function(d, s, id) {
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "//connect.facebook.net/en_GB/all.js#xfbml=1&appId=270450549726411";
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));</script>