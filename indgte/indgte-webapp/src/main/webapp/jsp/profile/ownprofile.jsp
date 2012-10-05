<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<section class="grid_12">
	<h3>This part uses no javascript:</h3>
	<p>User: ${user}
	<p><img src="${user.imageUrl }" /> ${user.displayName }
</section>