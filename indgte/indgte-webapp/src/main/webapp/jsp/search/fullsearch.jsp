<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../tiles/links.jsp" %>

<title>${term } &mdash; Dumaguete Search</title>

<div class="grid_8 maingrid search-results-maingrid">
	
	<div class="page-header">All Results</div>
	
	<c:if test="${total == 0 }">
		No results for your term "${term }". 
		<div class="subtitle">Check your spelling or try another term</div>
	</c:if>
	
	<c:if test="${not empty BUSINESS }">
		<section class="result-group">
			<div class="section-header">Pages</div>
			<ul>
			<c:forEach items="${BUSINESS }" var="b">
				<li class="result" type="business" identifier="${b.identifier }">
					<c:choose>
						<c:when test="${not empty b.thumbnailHash }">
							<img class="result-img" src="${urlImgRoot }${b.thumbnailHash }.jpg">
						</c:when>
						<c:otherwise>
							<img class="result-img" src="${noimage50 }">
						</c:otherwise>
					</c:choose>
					<div class="result-info">
						<div class="result-title bold">${b.title }</div>
						<div class="result-description subtitle">
							${fn:substring(b.description, 0, 80) }<c:if test="${fn:length(b.description) > 80 }">...</c:if>
						</div>
					</div>
				</li>
			</c:forEach>
			</ul>
		</section>
	</c:if>
	
	<c:if test="${not empty USER }">
		<section class="result-group mt10">
			<div class="section-header">People</div>
			<ul>
			<c:forEach items="${USER }" var="b">
				<li class="result" type="user" identifier="${b.identifier }">
					<img class="result-img" src="${b.thumbnailHash }">
					<div class="result-info">
						<div class="result-title bold">${b.title }</div>
						<div class="result-description subtitle">${b.rank }</div>
					</div>
				</li>
			</c:forEach>
			</ul>
		</section>
	</c:if>
	
	<c:if test="${not empty CATEGORY }">
		<section class="result-group">
			<div class="section-header">Categories</div>
			<ul>
			<c:forEach items="${CATEGORY }" var="b">
				<li class="result" type="category" identifier="${b.identifier }">
					<c:choose>
						<c:when test="${not empty b.thumbnailHash }">
							<img class="result-img" src="${urlImgRoot }${b.thumbnailHash }.jpg">
						</c:when>
						<c:otherwise>
							<img class="result-img" src="${noimage50 }">
						</c:otherwise>
					</c:choose>
					<div class="result-info">
						<div class="result-title bold">${b.title }</div>
						<div class="result-description subtitle">
							${fn:substring(b.description, 0, 80) }<c:if test="${fn:length(b.description) > 80 }">...</c:if>
						</div>
					</div>
				</li>
			</c:forEach>
			</ul>
		</section>
	</c:if>
	
	<c:if test="${not empty PRODUCT }">
		<section class="result-group">
			<div class="section-header">Products</div>
			<ul>
			<c:forEach items="${PRODUCT }" var="b">
				<li class="result" type="product" identifier="${b.identifier }">
					<c:choose>
						<c:when test="${not empty b.thumbnailHash }">
							<img class="result-img" src="${urlImgRoot }${b.thumbnailHash }.jpg">
						</c:when>
						<c:otherwise>
							<img class="result-img" src="${noimage50 }">
						</c:otherwise>
					</c:choose>
					<div class="result-info">
						<div class="result-title bold">${b.title }</div>
						<div class="result-description subtitle">
							${fn:substring(b.description, 0, 80) }<c:if test="${fn:length(b.description) > 80 }">...</c:if>
						</div>
					</div>
				</li>
			</c:forEach>
			</ul>
		</section>
	</c:if>
	
</div>

<style>
.search-results-maingrid ul {
	list-style-type: none;
	margin: 0;
	padding: 0;
}

.page-header {
	margin-bottom: 10px;
}

.result {
	padding: 5px;
	cursor: pointer;
}
.result:not(.ui-state-highlight) {
	border-top: 1px solid #e9e9e9;
	border-right: 1px solid transparent;
	border-bottom: 1px solid transparent;
	border-left: 1px solid transparent;
}
.result:not(.ui-state-highlight):first-child {
	border-top: 1px solid transparent;
}

.result-img {
	width: 50px;
	height: 50px;
	vertical-align: top;
}

.result-info {
	display: inline-block;
	margin-top: 4px;
}
</style>

<script>
window.constants = {
	maxresults : '${maxresults}' || 0
}

window.urls = {
	businessprofile : '<spring:url value="/" />',
	userprofile : '<spring:url value="/p/user/" />',
	categoryprofile : '<spring:url value="/b/categories/" />',
	productprofile : '<spring:url value="/b/products/" />'
}

$(function(){
	var $resultgroups = $('.result-group');
	
	$resultgroups.each(function(){
		var $this = $(this);
		
		if($this.find('.result').length > constants.maxresults) {
			$('<button mt10>').text('Load ' + constants.maxresults + ' more').button().appendTo($this);
		}
	})
	
	.on({
		mouseenter: function(){
			$(this).addClass('ui-state-highlight');
		},
		mouseleave: function(){
			$(this).removeClass('ui-state-highlight');
		},
		click: function(){
			var $this = $(this);
			var identifier = $this.attr('identifier');
			var type = $this.attr('type');
			
			switch(type) {
			case 'business':
				window.location.href = urls.businessprofile + identifier;
				break;
			case 'user':
				window.location.href = urls.userprofile + identifier;
				break;
			case 'category':
				window.location.href = urls.categoryprofile + identifier;
				break;
			case 'product':
				window.location.href = urls.productprofile + identifier;
				break;
			default:
				debug('Unsupported type: ' + type);
			}
		}
	}, '.result');
});
</script>