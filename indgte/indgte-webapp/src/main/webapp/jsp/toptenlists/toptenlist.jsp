<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../tiles/links.jsp" %>

<title>Top Ten Lists In Dumaguete</title>
<script type="text/javascript" src="${jsApplication }"></script>

<div class="grid_12">

<section class="list-details">
	<div class="list-title">${topten.title }</div>
	<div class="list-totalVotes">${topten.totalVotes }</div>
	<div class="list-creator">
		<div class="list-creator-img"><img src="${topten.creator.imageUrl }" /></div>
		<div class="list-creator-name">${topten.creator.username }</div>
	</div>
</section>

<section class="candidates">
	<ul>
		<c:forEach items="${topten.ordered }" var="candidate">
		<li class="candidate-container">
			<c:if test="${not empty candidate.attachment }">
				<img src="${candidate.attachment.imgur.smallSquare }" />
				<div class="candidate-attachment-name">${candidate.attachment.name }</div>
			</c:if>
			<div class="candidate-title">Title: ${candidate.title }</div>
			<div class="candidate-votes">Votes: ${fn:length(candidate.voters) }</div>
			<div class="link-vote-container"><a href="javascript:;" candidateId="${candidate.id }" class="link-vote">Vote</a></div>
		</li>
		</c:forEach>
	</ul>
</section>

<section class="newcandidate">
Add a new option
	<div class="newcandidate-form-container">
		<form id="newcandidate-form">
			List title: <input type="text" class="newcandidate-title" />
			<input type="submit" />
		</form>
	</div>
</section>

</div>

<style>
</style>

<script>
window.urls = {
	toptens : '<spring:url value="/i/toptens/" />'
}

window.topten = {
	id : '${topten.id}'	
}

$(function(){
	var $newcandidateform = $('#newcandidate-form'),
		$iptNewcandidateTitle = $('.newcandidate-title');
	
	$newcandidateform.submit(function(){
		var $this = $(this);
		//if(!$this.valid()) return;		
		
		$.post(urls.toptens + topten.id + '.json', {title: $iptNewcandidateTitle.val()}, function(response) {
			switch(response.status) {
			case '200':
				window.location.reload();
				break;
			default:
				debug(response);
			}
		});
		
		return false;
	});
	
	$('.link-vote').click(function(){
		var candidateId = $(this).attr('candidateId');
		$.post(urls.toptens + topten.id + '/' + candidateId + '.json', function(response) {
			switch(response.status) {
			case '200':
				window.location.reload();
				break;
			default:
				debug(response);
			}
		});
	});
});
</script>