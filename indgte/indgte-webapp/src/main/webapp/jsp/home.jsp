<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<section class="grid_12 newpost" style="margin-top: 10px; height: 160px;">
<div id="tabs">
	<ul>
		<li><a href="#status">Status</a></li>
		<li><a href="#product">Promote</a></li>
	</ul>
	
	<div id="status">
		<textarea class="status-textarea" placeholder="<spring:message code="home.status.textarea" />" rows="3"></textarea>	
		
		<div class="status-options">
			<sec:authorize access="hasRole('ROLE_USER_FACEBOOK')">
			<input type="checkbox" name="toFacebook" id="toFacebook" value="true"><label for="toFacebook"><spring:message code="home.status.postfb" /></label>
			</sec:authorize>
			
			<div class="floatright">
				<div class="post-as">
					<div class="post-as-image"></div>
					<div class="arrow-container"></div>
				</div>
				<div class="button">Post</div>
			</div>
		</div>
	</div>
	
	<div id="product">New product here</div>
</div>
</section>

<section class="grid_12 feed">
	Feed feed feed
</section>

<style>
.ui-tabs {
	font-size: 0.8em;
}

.status-textarea {
	width: 540px;
	resize: none;
}

.status-options {
	width: 540px;
}

</style>

<script type="text/javascript" src="${jsApplication }"></script>
<script>
$(function(){
	$('#tabs').tabs();
	$('.button').button();
});
</script>