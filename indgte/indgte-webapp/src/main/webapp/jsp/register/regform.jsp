<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<title>Dumaguete &mdash; Register a Business</title>

<spring:url value="/r/save/1/" var="urlSubmit" />

<link rel="stylesheet" href="<spring:url value='/resources/css/registration/regform.css' />" />
<script src="<spring:url value='/resources/javascript/registration/regform.js' />" ></script>
<script src="http://ajax.aspnetcdn.com/ajax/jQuery.Validate/1.6/jQuery.Validate.min.js"></script>

<div class="grid_12">
<div class="form-container">
<form:form method="post" commandName="regform" action="${urlSubmit }">
	<table id="regtable">
		<tr>
			<td>
				<div class="registration-label">
				<form:label path="domain">Domain:</form:label>
				</div>
			</td>
			<td>
				<form:input class="ui-state-active" path="domain" maxlength="20" />
			</td>
		</tr>
		<tr><td></td><td class="error"></td></tr>
		<tr>
			<td>
				<div class="registration-label">
				<form:label path="fullName">Business Name:</form:label>
				</div>
			</td>
			<td>
				<form:input class="ui-state-active" path="fullName" maxlength="45"/>
			</td>
		</tr>
		<tr><td></td><td class="error"></td></tr>
		<tr>
			<td>
				<div class="registration-label">
				<form:label path="description">Description</form:label>
				</div>
			</td>
			<td>
				<form:textarea class="ui-state-active" rows="5" path="description" />
			</td>
		</tr>
		<tr>
			<td>
				<div class="registration-label">
				<form:label path="address">Address:</form:label>
				</div>
			</td>
			<td>
				<form:textarea path="address" class="ui-state-active" rows="3" maxlength="140"/>
			</td>
		</tr>
		<tr><td></td><td class="error"></td></tr>
		<tr>
			<td>
				<div class="registration-label">
				<form:label path="email">Email:</form:label>
				</div>
			</td>
			<td>
				<form:input class="ui-state-active" path="email" />
			</td>
		</tr>
		<tr><td></td><td class="error"></td></tr>
		<tr>
			<td>
				<div class="registration-label">
				<form:label path="cellphone">Cellphone Number:</form:label>
				</div>
			</td>
			<td>
				<form:input class="ui-state-active" path="cellphone" />
			</td>
		</tr>
		<tr>
			<td>
				<div class="registration-label">
				<form:label path="landline">Landline Number:</form:label>
				</div>
			</td>
			<td>
				<form:input class="ui-state-active" path="landline" />
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div class="registration-label">
				<button class="submit">Open!</button>
				</div>
			</td>
		</tr>
	</table>
	</form:form>
</div>
<div class="bigimg-container">
	<img src="<c:url value='/resources/images/openforbusiness.jpg' />" />
</div>
</div>