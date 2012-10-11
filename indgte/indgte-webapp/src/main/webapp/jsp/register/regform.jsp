<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<spring:url value="/r/save/1/" var="urlSubmit" />

<form:form method="post" commandName="regform" action="${urlSubmit }">
<ul>
	<li>
		<form:label path="domain">Domain:</form:label>
		<form:input path="domain" maxlength="20" />
	</li>
	
	<li>
		<form:label path="fullName">Business Name:</form:label>
		<form:input path="fullName" maxlength="45"/>
	</li>
	
	<li>
		<form:label path="description">Description</form:label>
		<form:input path="description" maxlength="150"/>
	</li>
	
	<li>
		<form:label path="address">Address:</form:label>
		<form:input path="address" />
	</li>
	
	<li>
		<form:label path="email">Email:</form:label>
		<form:input path="email" />
	</li>
	
	<li>
		<form:label path="cellphone">Cellphone Number:</form:label>
		<form:input path="cellphone" />
	</li>
	
	<li>
		<form:label path="landline">Landline Number:</form:label>
		<form:input path="landline" />
	</li>
	
	<li>
		<input type="submit" />
	</li>
</ul>
</form:form>