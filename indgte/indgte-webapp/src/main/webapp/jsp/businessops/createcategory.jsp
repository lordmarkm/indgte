<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="noimage" value="/resources/images/noimage.jpg" />
<spring:url var="spinner" value="/resources/images/spinner.gif" />
<spring:url var="urlNewProduct" value="/b/newproduct/" />
<spring:url var="urlCategories" value="/b/categories/" />

<title><spring:message code="business.newcategory.title" arguments="${business.domain }"/></title>

<div class="newcategory grid_12">
	<div class="newcategory-welcome">
		<img src="${business.profilepic.smallSquare }" />
		<h3><spring:message code="business.newcategory.title" arguments="${business.fullName }"/></h3>
		<div class="description italic">${business.description }</div>
	</div>
	<div class="newcategory-form">
		<ul>
			<li>
				<label for="newcategory-name">Name</label><br/>
				<input id="newcategory-name" type="text" />
			</li>
			<li>
				<label for="newcategory-description">Description:</label><br/>
				<textarea id="newcategory-description" rows="3"></textarea>
			</li>
		</ul>
		<button class="btnCreate">Create</button>
		<span class="newcategory-message"></span>
	</div>
</div>

<style>
.newcategory {
	position: relative;
}
.newcategory-welcome {
	min-height: 50px;
}
.newcategory-welcome img {
	float: left;
	height: 50px;
	width: 50px;
	margin: 5px 5px 200px 5px;
}
.newcategory-welcome h3 {
	display: inline;
	vertical-align: top;	
}

.newcategory-form {
	width: 350px;
	position: absolute;
	margin-left: 22px;
}
.newcategory-form ul {
	list-style-type: none;
}
.newcategory-form input[type="text"], .newcategory-form textarea {
	width: 300px;
}
.newcategory-form button {
	float: right;
	font-size: 0.7em;
}

</style>

<script>
$(function(){
	var urlNewCategory = '${urlNewCategory}',
		urlCategories = '${urlCategories}';
	
	var domain = '${business.domain}';
	
	var $newCategory = $('.newcategory'),
		$name = $('#newcategory-name'),
		$description = $('#newcategory-description'),
		$btnCreate = $('.btnCreate'),
		$message = $('.newcategory-message');
	
	$btnCreate.click(function(){
		var $overlay = $('<div class="overlay">').appendTo($newCategory);
		var name = $name.val();
		var description = $description.val();
		
		$.post(urlNewCategory + domain + '.json', {name: name, description: description}, function(response) {
			switch(response.status) {
			case '200':
				window.location.replace(urlCategories + domain + '/' + response.category.id);
				break;
			default:
				$message.text('Error, please try again');
				debug(response);
			}
			$overlay.remove();
		});
	});
});
</script>

<spring:url var="jsApplication" value="/resources/javascript/application.js" />
<script type="text/javascript" src="${jsApplication }"></script>