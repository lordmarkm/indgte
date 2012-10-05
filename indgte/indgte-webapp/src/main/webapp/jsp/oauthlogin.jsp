<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>
<head>
<link rel="stylesheet" href="<c:url value='/resources/css/login.css' />" />
</head>
<body>
	<c:url value='/signin/facebook' var="signin_facebook" />
	
	<sec:authorize ifNotGranted="ROLE_USER">
		<p>Please log in with a third party provider</p>
		<form class="login" action="http://localhost:8080/signin/twitter" method="POST">
			<p>
				<input type="submit" value="Login with Twitter" />
			</p>
		</form>

		<form class="login" action="${signin_facebook}" method="post">
			<p>
				<input type="submit" value="Login with Facebook" />
			</p>
		</form>
	</sec:authorize>

	<sec:authorize access="hasRole('ROLE_USER')">
		You are already logged in
	</sec:authorize>
</body>
</html>