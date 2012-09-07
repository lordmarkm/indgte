<%@ include file="/jsp/tags.jsp" %>

<spring:url value="/register/save/" var="urlSubmit" />
<form:form method="post" commandName="regForm" action="${urlSubmit }">
<ul>
	<li>
		<form:label path="domain">Domain:</form:label>
		<form:input path="domain" />
	</li>
	
	<li>
		<form:label path="fullName">Business Name:</form:label>
		<form:input path="fullName" />
	</li>
	
	<li>
		<form:label path="category">Category:</form:label>
		<form:select path="category" items="${categories }" />
	</li>
	
	<li>
		<form:label path="street">Street:</form:label>
		<form:input path="street" />
	</li>
	
	<li>
		<form:label path="city">City:</form:label>
		<form:input path="city" />
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