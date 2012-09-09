<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url value="/r/save/2/" var="urlSubmit" />

<form:form method="post" commandName="regform" action="${urlSubmit }">
	<form:hidden path="latitude" value="0" />
	<form:hidden path="longitude" value="0" />
	<input type="submit" value="Submit fake LatLng" />
</form:form>