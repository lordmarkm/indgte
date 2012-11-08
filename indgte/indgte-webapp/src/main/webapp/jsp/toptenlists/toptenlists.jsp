<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../tiles/links.jsp" %>

<title>Top Ten Lists In Dumaguete</title>
<script type="text/javascript" src="${jsApplication }"></script>

<div class="grid_12">

<section class="newlist">
Create a new list
	<div class="newlist-form-container">
		<form id="newlist-form">
			List title: <input type="text" class="newlist-title" />
			<input type="submit" />
		</form>
	</div>
</section>

<section class="lists-recent">
Recent top ten lists:
	<c:forEach items="${recentLists }" var="list">
	<div class="list-container">
		<div class="list-title"><a href="${urlToptenList }${list.id}">${list.title }</a></div>
		<ul>
			<c:forEach items="${list.candidates }" var="candidate" varStatus="i">
			<c:if test="${i.index lt 4 }">
			<li>
				<div class="candidate-title">${candidate.title }</div>
				<div class="candidate-votes">${fn:length(candidate.voters) }</div>			
			</li>
			</c:if>
			</c:forEach>
		</ul>
	</div>
	</c:forEach>
</section>

<section class="lists-popular">

</section>



</div>

<style>
</style>

<script>
window.urls = {
	newlist : '<spring:url value="/i/toptens.json" />',
	listablegroups : '<spring:url value="/s/listablegroups.json" />'
}

$(function(){
	var $newlistform = $('#newlist-form'),
		$iptNewlistTitle = $('.newlist-title');
	
	$newlistform.submit(function(){
		var $this = $(this);
		//if(!$this.valid()) return;		
		
		$.post(urls.newlist, {title: $iptNewlistTitle.val()}, function(response) {
			switch(response.status) {
			case '200':
				window.location.reload();
				break;
			default:
				debug(response);
			}
		});
		
		$.get(urls.listablegroups, function(response) {
			switch(response.status) {
			case '200':
			default:
				debug(response);
			}
		});
		
		return false;
	});
});
</script>