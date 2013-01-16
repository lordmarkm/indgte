<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<link rel="stylesheet" href="<spring:url value='/resources/css/footer/footer.css' />" />
<script src="<spring:url value='/resources/javascript/footer/footer.js' />" ></script>

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

<script>
window.themes = {
	changeUrl : '<c:url value="/i/themechange/" />',
	current : '${user.appearanceSettings.theme}'
}
window.backgrounds = {
	changeUrl : '<c:url value="/i/bgchange/" />',
	current : '${user.appearanceSettings.background}'
}
</script>

<div id="fb-root"></div>
<script>(function(d, s, id) {
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "//connect.facebook.net/en_GB/all.js#xfbml=1&appId=${facebookClientId}";
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));</script>