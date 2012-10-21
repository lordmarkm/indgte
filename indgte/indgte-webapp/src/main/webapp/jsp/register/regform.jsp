<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<spring:url value="/r/save/1/" var="urlSubmit" />

<script src="http://ajax.microsoft.com/ajax/jQuery.Validate/1.6/jQuery.Validate.min.js"></script>

<style>
.form-container, .bigimg-container {
	display: inline-block;
	vertical-align: top;
}
#regtable td:nth-child(2) {
	width: 320px;
}
#regform textarea, #regform input[type="text"] {
	width: 98%;
	resize: none;
}
#regform input[type="text"].ui-state-error {
	border: 1px solid #CD0A0A;
}

.registration-label {
	text-align: right;
}

label.ui-state-error {
	background: 0;
	background-color: white;
    border: 0;
	overflow: visible;
	font-size: 0.8em;
	color: #CD0A0A;
}

.bigimg-container {
}
.bigimg-container img {
	max-width: 400px;
	max-height: 400px;
	margin-left: 40px;
}
</style>

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
				<form:input class="ui-state-active" path="address" />
			</td>
		</tr>
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

<script>
window.urls = {
	uniqueDomain : '<spring:url value="/r/uniquedomain/" />'
}

$(function(){
	var $form = $('#regform');
	
	function matchRegexp(value, element, regexp) {
		return value.match(new RegExp(regexp));
	}
	$.validator.addMethod("domain", matchRegexp, 'Domain requires alphanumeric characters only (lowercase, no spaces)');
	$.validator.addMethod("fullName", matchRegexp, 'Business Name accepts alphanumeric characters only (upper or lower case, spaces allowed)');
	
	$form.validate({
		rules: {
			domain: {
				required: true,
				rangelength: [4, 20],
				domain: '^[a-z0-9]+$',
				remote: {
					url: urls.uniqueDomain,
					type: 'post',
					data: {domain: function(){return $('#domain').val()}}
				} 
			},
			fullName: {
				required: true,
				rangelength: [4, 45],
				fullName: '^[a-zA-Z0-9 ]+$'
			},
			email: {
				email: true
			}
		},
		messages: {
			domain : {
				remote: function(){
					return 'The domain \'' + $('#domain').val() + '\' is already taken. Sorry.'
				}
			}
		},
		errorPlacement: function(error, element) {
			element.closest('tr').next().find('.error').html('').append(error);
		},
		errorClass: 'ui-state-error'
	});
	
	$('button.submit').click(function(){
		$form.submit();
	});
	
	$form.submit(function(){
		if(!$form.valid()) {
			return false;
		}
	});
});
</script>