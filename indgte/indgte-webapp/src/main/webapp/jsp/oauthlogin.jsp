<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>
<head>

<link rel="icon" href="<c:url value='/resources/images/favicon.ico?v=4' />" type="image/x-icon" />
<title>Connect &mdash; Dumaguete</title>

<link rel="stylesheet" href="<c:url value='/resources/css/login.css' />" />
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>

</head>
<body>
	<c:url value='/signin/facebook' var="signin_facebook" />
	<c:url value='/signin/twitter' var="signin_twitter" />
	<c:url value='/resources/images/signin/creepychicken.gif'  var="nosignin"/>
	<c:url value="/resources/images/signin/facebook.png" var="facebookIcon" />
	<c:url value="/resources/images/signin/twitter.png" var="twitterIcon" />
	<c:url value='/' var="back" />
	
	<sec:authorize ifNotGranted="ROLE_USER">
		<table id="table">
			<tr>		
				<td><img class="signin-option signin-facebook" src="${facebookIcon }" /></td>
				<td><img class="signin-option signin-twitter" src="${twitterIcon }" /></td>
				<td><img class="signin-option signin-none" src="${nosignin }" /></td>
			</tr>
			<tr>
				<td class="signin-option signin-facebook">Facebook</td>
				<td class="signin-option signin-twitter">Twitter</td>
				<td class="signin-option signin-none">Nevermind</td>
			</tr>
		</table>
		<p>Please connect with a third-party authentication provider</p>
		
		<form class="login" id="signin-facebook" action="${signin_facebook }" method="POST">
		</form>
		<form class="login" id="signin-twitter" action="${signin_twitter}" method="post">
		</form>
	</sec:authorize>

	<sec:authorize access="hasRole('ROLE_USER')">
		You are already logged in
	</sec:authorize>
</body>
</html>

<style>
body {
	text-align: center;
}

#table {
	margin: auto;
	text-align: center;
	margin-top: 10%;
}

#table td {
	min-width: 100px;
	font-family: Helvetica, Arial;
	font-weight: bold;
}

.signin-option {
	cursor: pointer;
}

img.signin-option {
	width: 50px;
	height: 50px;
}

</style>

<script>
$(function(){
	$('.signin-facebook').click(function(){
		$('#signin-facebook').submit();
	});
	
	$('.signin-twitter').click(function(){
		$('#signin-twitter').submit();
	});
	
	$('.signin-none').click(function(){
		window.location.href = '<c:url value="/" />';
	});
});
</script>