<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@include file="../tiles/links.jsp" %>

<link rel="stylesheet" href="<c:url value='/resources/css/businessops.css' />" />
<link rel="stylesheet" href="<spring:url value='/resources/css/category/viewcategory.css' />" />

<script src="http://ajax.aspnetcdn.com/ajax/jQuery.Validate/1.6/jQuery.Validate.min.js"></script>
<script src="<spring:url value='/resources/javascript/viewcategory/viewcategory.js' />" ></script>

<title>${category.name } &mdash; Dumaguete</title>

<div class="category-container grid_8 maingrid">
	<section class="category-welcome">
		<div class="category-welcome-category">
			<div class="category-welcome-image-container relative">
				<img class="category-welcome-image" />
			</div>
			<h3 class="category-name">${category.name }</h3>
			
			<% pageContext.setAttribute("newLineChar", "\n"); %>
			<div class="category-welcome-description italic">${fn:replace(category.description, newLineChar, "<br>")}</div>
		</div>
		<div class="category-welcome-provider">
			<img class="category-provider-image" />
			<div class="category-provider-info">
				<h4><a href="${urlProfile }${business.domain}">${business.fullName }</a></h4>
				<div>Provider</div>
			</div>
		</div>
	</section>
	
	<section class="summary-container">
		<h5>Products - Summary</h5>
		<ul class="products-summary"></ul>
	</section>
	
	<div class="products-container">
		<ol class="products"></ol>
	</div>
	
	<section>
		<div class="section-header">Comments</div>
		<div class="mt5">
			<div class="fb-like" data-send="true" data-width="450" data-show-faces="true"></div>
		</div>
		<div class="fb-comments" data-href="${baseURL}/b/categories/${category.business.domain}/${category.id}" data-width="620"></div>
	</section>
</div>

<c:if test="${not empty category.mainpic }">
	<c:set var="categoryPic" value="${category.mainpic.largeThumbnail }" />
	<c:set var="categoryPicImgur" value="${category.mainpic.imgurPage }" />
</c:if>
<c:if test="${not empty business.profilepic }">
	<c:set var="businessPic" value="${business.profilepic.smallSquare }" />
	<c:set var="businessPicImgur" value="${business.profilepic.imgurPage }" />
</c:if>
<script>
window.urls = {
	urlProducts : '${urlProducts}',
	deleteCategory: '<spring:url value="/b/categories/delete/" />',
	profile: '${urlProfile}',
	products: '${urlProducts}',
	noimage50: '${noimage50}',
	noimage: '${noimage}',
	newproduct: '${urlNewProduct}',
	categories: '${urlCategories}',
	spinner: '${spinner}'
}

window.constants = {
	domain : '${business.domain}',
	previewPicsCount : 5,
	imgurKey: '${imgurKey}'
}

window.user = {
	coconuts: '${user.billingInfo.coconuts}'
}

window.products = {
	domain : '${business.domain}',
	$summary : $('.products-summary'),
	$products : $('.products-container')
}

window.business = {
	domain: '${business.domain}',
	businessPic: '${businessPic}',
	businessPicImgur: '${businessPicImgur}'
}

window.category = {
	id: '${category.id}',
	name: '<c:out value="${category.name}" />',
	imageUrl: '${categoryPic}',
	categoryPicImgur: '${categoryPicImgur}',
	owner: '${owner}' === 'true',
	urlParent: '${urlProfile}${category.business.domain}'
}

window.messages = {
	uploadtitle: '<spring:message code="category.mainpic.upload.title" />',
	uploadbutton: "<spring:message code='category.mainpic.upload.button' />",
	uploadcancel: "<spring:message code='category.mainpic.upload.cancel' />",
	newproducttitle: '<spring:message code="category.newproduct.title" />',
	newproductsave: "<spring:message code='category.newproduct.save' />"
}
</script>

<sec:authorize access="hasRole('ROLE_MODERATOR')">
	<c:set var="moderator" value="true" />
</sec:authorize>

<c:if test="${owner || moderator}">

<div class="owner-operations-container grid_4 sidebar-section">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Category operations</div>
		<c:if test="${moderator }">
			<div class="ui-state-highlight pd5"><spring:message code="mod.warning" /></div>
		</c:if>
		<button class="btn-edit">Edit</button>
		<div class="dialog-edit hide" title="Edit ${category.name }">
			<form id="form-edit" method="post" action="<spring:url value='/b/categories/edit/${category.id }' />">
				<div>Name</div>
				<div><input type="text" name="name" value="${category.name }" /></div>
				<div>Description</div>
				<div><textarea name="description" rows="4">${category.description }</textarea></div>
			</form>
		</div>
		
		<button class="btn-create-product">New product</button>
		<button class="btn-promote">Promote</button>
		<div class="dialog-promote hide" title="Promote this Category">
			<span><spring:message code="entity.promote.dialog" arguments="${user.billingInfo.coconuts },${user.billingInfo.coconuts / 10 },${category.name }" /></span>
			<form class="form-promote" method="post" action="<c:url value='/o/sidebar/category/${category.id }' />" >
				<table>
					<tr>
						<td><label for="start-date">Promote from</label></td>
						<td><input type="text" id="start-date" name="start" readonly="readonly" placeholder="Click to choose" /></td>
					</tr>
					<tr>
						<td><label for="end-date">Promote until</label></td>
						<td><input type="text" id="end-date" name="end" readonly="readonly" placeholder="Click to choose"/></td>
					</tr>
				</table>
			</form>
			<span class="coconut-cost"><spring:message code="promote.dialog.comp" /></span>
		</div>
		<button class="btn-delete">Delete</button>
	</div>
</div>
<script src="<spring:url value='/resources/javascript/promote.js' />" ></script>

<!-- Image upload -->
<div id="image-upload" style="display: none;">
	<div class="form-pic">
		<input class="image-upload-file" type="file" />
	</div>
	<div class="loading hide">
		<img src="${spinner }" />
	</div>
</div>

<div class="newproduct" style="display: none;">
	<div class="newproduct-pic-container">
		<img src="${noimage }" class="newproduct-pic-display" />
		<input type="file" class="newproduct-pic" />
	</div>
	<div class="newproduct-form">
		<div class="newproduct-field">Name</div>
		<input type="text" class="newproduct-name" />
		<div class="newproduct-field">Description</div>
		<textarea class="newproduct-description" rows="3"></textarea>
		<div class="newproduct-field">Main pic</div>
		<button class="btn-newproduct-pic">Choose</button>
		<input type="hidden" class="newproduct-pic-hash" />
		<input type="hidden" class="newproduct-pic-deletehash" />
	</div>
</div>

<script src="<spring:url value='/resources/javascript/viewcategory/viewcategory-owner.js' />" ></script>
</c:if>

<!-- Notifications -->
<%@include file="../grids/notifications4.jsp"  %>

<!-- Sidebar Featured promos -->
<%@include file="../grids/sidebarpromos.jsp" %>