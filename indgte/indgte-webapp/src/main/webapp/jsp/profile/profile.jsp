<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<table>
	<tr>
		<th>Domain</th>
		<th>${business.domain}</th>
	</tr>
	<tr>
		<td>Name</td>
		<td>${business.fullName }</td>
	</tr>
	<tr>
		<td>Description</td>
		<td>${business.description }</td>
	</tr>
	<tr>
		<td>Owner</td>
		<td>${business.owner }</td>
	</tr>
</table>